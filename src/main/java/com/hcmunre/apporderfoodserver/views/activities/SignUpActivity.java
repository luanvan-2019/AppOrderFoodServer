package com.hcmunre.apporderfoodserver.views.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Database.RestaurantData;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;

import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.edit_name_restaurant)
    EditText edit_name_restaurant;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.edit_name_user)
    EditText edit_name_user;
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_pass)
    EditText edit_pass;
    @BindView(R.id.edit_opening)
    EditText edit_opening;
    @BindView(R.id.edit_closing)
    EditText edit_closing;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    private static final int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    RestaurantData restaurantData = new RestaurantData();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Restaurant restaurant;
    RestaurantOwner restaurantOwner;
    double mLat, mLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        edit_opening.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minutes) -> {
                edit_opening.setText(String.format("%02d:%02d:00", hourOfDay, minutes));
            }, hour, minute, true);
            timePickerDialog.show();
        });
        edit_closing.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minutes) -> {
                edit_closing.setText(String.format("%02d:%02d:00", hourOfDay, minutes));
            }, hour, minute, true);
            timePickerDialog.show();
        });
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDb8vrfFWCuSNigtxvfM-zC-4MvNQlGIFQ");
        }
        edit_address.setOnClickListener(view -> {
            setAddress();
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_sign_up.setOnClickListener(v -> {
            createNewRestaurant();
        });

    }

    private void setAddress() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("VN")
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String address = place.getAddress();
                    mLat = place.getLatLng().latitude;
                    mLng = place.getLatLng().longitude;
                    edit_address.setText(address);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("BBB", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                }

            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void searchLocation() {
        String location = edit_address.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Rỗng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void createNewRestaurant() {
        restaurant = new Restaurant();
        restaurant.setmName(edit_name_restaurant.getText().toString());
        restaurant.setPhone(edit_phone.getText().toString());
        Time opening = Time.valueOf(edit_opening.getText().toString());
        Time closing = Time.valueOf(edit_closing.getText().toString());
        restaurant.setOpening(opening);
        restaurant.setClosing(closing);
        restaurant.setAddress(edit_address.getText().toString());
        restaurant.setLat(mLat);
        restaurant.setLng(mLng);
        restaurantOwner = new RestaurantOwner();
        restaurantOwner.setName(edit_name_user.getText().toString());
        restaurantOwner.setEmail(edit_email.getText().toString());
        restaurantOwner.setPassword(edit_pass.getText().toString());
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ ...");
        progressDialog.show();
        Observable<Boolean> createNewRes = Observable.
                just(restaurantData.createNewRestaurant(restaurant, restaurantOwner));
        compositeDisposable.add(
                createNewRes
                        .delay(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> {
                                    compositeDisposable.add(
                                            restaurantData.checkExistRestaurantOwner(restaurantOwner.getEmail())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(aBoolean1 -> {
                                                        if (aBoolean == true) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(this, "Không thể tạo tài khoản", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }, throwable -> {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(this, "Lỗi hệ thống " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                    })
                                    );
                                }
                        )
        );
    }
}
