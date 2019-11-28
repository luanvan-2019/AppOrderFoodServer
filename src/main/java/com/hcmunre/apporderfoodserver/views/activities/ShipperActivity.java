package com.hcmunre.apporderfoodserver.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.commons.Progress;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;
import com.hcmunre.apporderfoodserver.views.adapters.ShipperAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ShipperActivity extends AppCompatActivity {
    @BindView(R.id.action_add_shipper)
    FloatingActionButton action_add_shipper;
    @BindView(R.id.recyc_shipper)
    RecyclerView recyc_shipper;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @BindView(R.id.relative_layout)
    RelativeLayout relative_layout;
    Button btn_select_image, btn_add_shipper;
    Shipper shipper;
    TextView edit_address;
    EditText edit_name, edit_phone, edit_password, edit_email;
    View view;
    ImageView image_shipper;
    ShipperData shipperData = new ShipperData();
    double mLat, mLng;
    byte[] byteArray;
    String encodedImage, address;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PLACE_PICKER_REQUEST = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);
        ButterKnife.bind(this);
        init();
        evenClick();
    }

    private void init() {
        toolbar.setTitle("Quản lý shipper");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        action_add_shipper.setOnClickListener(view -> openDialogAddShipper());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyc_shipper.setLayoutManager(layoutManager);
        recyc_shipper.setItemAnimator(new DefaultItemAnimator());
        //swipe
        swipe_layout.setColorSchemeResources(R.color.colorPrimary);
        //swipe

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDb8vrfFWCuSNigtxvfM-zC-4MvNQlGIFQ");
        }

    }

    private void evenClick() {
        swipe_layout.setOnRefreshListener(() -> {
            new getShipperOfRestaurant().execute();
        });
        new getShipperOfRestaurant().execute();
    }
    public class getShipperOfRestaurant extends AsyncTask<String,String,ArrayList<Shipper>>{
        @Override
        protected void onPreExecute() {
            if(!swipe_layout.isRefreshing()){
                swipe_layout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Shipper> shippers) {
            if (shippers==null) {
                swipe_layout.setRefreshing(false);
                main_layout.setVisibility(View.VISIBLE);
            } else {
                swipe_layout.setRefreshing(false);
                ShipperAdapter shipperAdapter = new ShipperAdapter(ShipperActivity.this, shippers);
                recyc_shipper.setAdapter(shipperAdapter);
                main_layout.setVisibility(View.GONE);
            }


        }

        @Override
        protected ArrayList<Shipper> doInBackground(String... strings) {
            ArrayList<Shipper> shippers=null;
            if (Common.isConnectedToInternet(ShipperActivity.this)){
                shippers=shipperData.getAllShipper(PreferenceUtilsServer.getUserId(ShipperActivity.this));
            }else {
                Snackbar.make(relative_layout, "Vui lòng kiểm tra kết nối mạng", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
            return shippers;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = null;
            Uri uri = data.getData();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                image_shipper.setImageBitmap(bitmap);
                try {
                    Bitmap image = ((BitmapDrawable) image_shipper.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    address = place.getAddress();
                    mLat = place.getLatLng().latitude;
                    mLng = place.getLatLng().longitude;
                    edit_address.setText(address);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("BBB", status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Đã hủy", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            Toast.makeText(this, "Đã hủy", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialogAddShipper() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.dialog_add_shipper, null);
        alertDialog.setView(add_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        //
        edit_name = dialog.findViewById(R.id.edit_name_shipper);
        edit_phone = dialog.findViewById(R.id.edit_phone_shipper);
        edit_address = dialog.findViewById(R.id.edit_address_shipper);
        edit_email = dialog.findViewById(R.id.edit_email_shipper);
        edit_password = dialog.findViewById(R.id.edit_password);
        image_shipper = dialog.findViewById(R.id.image_shipper);
        btn_select_image = dialog.findViewById(R.id.btn_select_image);
        btn_add_shipper = dialog.findViewById(R.id.btn_add_shipper);
        ImageView close = dialog.findViewById(R.id.txt_close);
        view = dialog.findViewById(R.id.main_layout);
        //

        edit_address.setText(address);
        //
        btn_add_shipper.setOnClickListener(v -> {

            creatNewShipper(dialog);

        });
        //
        chooseImage();
        setAddress();
        close.setOnClickListener(v -> dialog.dismiss());
    }

    private void creatNewShipper(AlertDialog dialog) {
        shipper = new Shipper();
        shipper.setName(edit_name.getText().toString());
        shipper.setPhone(edit_phone.getText().toString());
        shipper.setAddress(edit_address.getText().toString());
        shipper.setLat(mLat);
        shipper.setLng(mLng);
        shipper.setImage(encodedImage);
        shipper.setEmail(edit_email.getText().toString());
        shipper.setPassword(edit_password.getText().toString());
        shipper.setRestaurantId(PreferenceUtilsServer.getUserId(this));
        Observable<Boolean> createShipper = Observable.just(shipperData.createNewShipper(shipper));
        compositeDisposable.add(
                createShipper
                        .observeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> {
                            if (aBoolean == true) {
                                Snackbar snackbar=Snackbar.make(view, "Đã lưu", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null);
                                snackbar.show();
                                View view=snackbar.getView();
                                TextView textView=view.findViewById(com.google.android.material.R.id.snackbar_text);
                                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                dialog.dismiss();
                            } else {
                                Snackbar.make(view, "Không thể lưu", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }, throwable -> {

                        })
        );
    }

    private void setAddress() {
        edit_address.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields).setCountry("VN")
                    .build(ShipperActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        });

    }

    private void chooseImage() {
        btn_select_image.setOnClickListener(view -> {
            //Mở gallery và chọn hình trong media
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            } else {
                Toast.makeText(this, "Không tìm thấy media", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
