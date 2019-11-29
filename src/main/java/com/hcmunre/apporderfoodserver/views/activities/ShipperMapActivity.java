package com.hcmunre.apporderfoodserver.views.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.directionHelper.DirectionFinder;
import com.hcmunre.apporderfoodserver.directionHelper.DirectionFinderListener;
import com.hcmunre.apporderfoodserver.directionHelper.Route;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.notifications.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipperMapActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {
    @BindView(R.id.txt_name_restaurant)
    TextView txt_name_restaurant;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_contact)
    ImageView image_contact;
    @BindView(R.id.btn_completed)
    TextView btn_completed;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    LocationManager locationManager;
    double lat, lng;
    String address;
    private static final int REQUEST_LOCATION = 1;
    ShipperData shipperData=new ShipperData();
    OrderData orderData=new OrderData();
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_map);
        ButterKnife.bind(this);
        init();
        eventClick();
        sendRequest();
        contactCustomer();

    }
    private void init(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        toolbar.setTitle(Common.currrentShipperOrder.getOrderName());
        toolbar.setSubtitle("Id#"+Common.currrentShipperOrder.getOrderID());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void eventClick(){
        btn_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success=shipperData.updateStatusShipping(Common.currrentShipperOrder.getOrderID(),3);
                Order order=new Order();
                order.setOrderId(Common.currrentShipperOrder.getOrderID());
                order.setOrderStatus(4);
                boolean success_update_order=orderData.updateOrder(order);
                if(success==true){
                    if(success_update_order==true){
                        Common.showToast(ShipperMapActivity.this,"Đã giao hàng xong");
                        NOTIFICATION_TITLE = "Giao hàng";
                        NOTIFICATION_MESSAGE ="Đơn hàng "+Common.currentOrder.getOrderId()+" đã giao xong ?";

                        JSONObject notification = new JSONObject();
                        JSONObject notifcationBody = new JSONObject();
                        try {
                            notifcationBody.put("title", NOTIFICATION_TITLE);
                            notifcationBody.put("message", NOTIFICATION_MESSAGE);

                            notification.put("to", Common.createTopicSender(Common.getTopicChannelUser(Common.currrentShipperOrder.getUserId())));
                            notification.put("data", notifcationBody);
                        } catch (JSONException e) {
                            Log.e(TAG, "Lỗi " + e.getMessage());
                        }
                        Common.sendNotification(notification,ShipperMapActivity.this);
                    }
                }else {
                    Common.showToast(ShipperMapActivity.this,"Không thể hoàn tất");
                }
            }
        });
    }
    private void sendRequest() {

        GetCurrentUser getCurrentUser = new GetCurrentUser(ShipperMapActivity.this);
        address = getCurrentUser.getuser();
        try {
            new DirectionFinder(this, address, Common.currrentShipperOrder.getOrderAddress()).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void contactCustomer(){
        image_contact.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+Common.currrentShipperOrder.getOrderPhone()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }else {
                startActivity(intent);
            }

        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            onGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
            {
                Location locationGps = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationGps != null) {
                    lat = locationGps.getLatitude();
                    lng = locationGps.getLongitude();
                } else {
                    Toast.makeText(this, "Không thể lấy vị trí của bạn", Toast.LENGTH_SHORT).show();
                }
                mMap = googleMap;
                LatLng currentLocation = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(address)
                        .position(currentLocation)));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        }
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tìm vị trí...");

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.txt_duration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.txt_km)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
