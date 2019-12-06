package com.hcmunre.apporderfoodserver.views.activities;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.RestaurantData;
import com.hcmunre.apporderfoodserver.models.Entity.ManagementRestaurant;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;

import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.edit_name_restaurant)
    TextInputEditText edit_name_restaurant;
    @BindView(R.id.edit_phone)
    TextInputEditText edit_phone;
    @BindView(R.id.edit_name_user)
    TextInputEditText edit_name_user;
    @BindView(R.id.edit_email)
    TextInputEditText edit_email;
    @BindView(R.id.edit_pass)
    TextInputEditText edit_pass;
    @BindView(R.id.edit_opening)
    TextInputEditText edit_opening;
    @BindView(R.id.edit_closing)
    TextInputEditText edit_closing;
    @BindView(R.id.edit_address)
    TextInputEditText edit_address;
    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static final int PLACE_PICKER_REQUEST = 1;
    RestaurantData restaurantData = new RestaurantData();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Restaurant restaurant;
    RestaurantOwner restaurantOwner;
    ManagementRestaurant managementRestaurant;
    double mLat, mLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        eventClick();
    }

    private void init() {
        ButterKnife.bind(this);
        toolbar.setTitle("Đăng kí nhà hàng");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    private void eventClick(){
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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void createNewRestaurant() {
        String name_restaurant=edit_name_restaurant.getText().toString().trim();
        String phone=edit_phone.getText().toString().trim();
        String address=edit_address.getText().toString().trim();
        String name_user=edit_name_user.getText().toString().trim();
        String email=edit_email.getText().toString().trim();
        String password=edit_pass.getText().toString().trim();
        if(TextUtils.isEmpty(name_restaurant)){
            edit_name_restaurant.setError("Nhâp tên cửa hàng");
        }else if(TextUtils.isEmpty(phone)){
            edit_phone.setError("Nhập số điện thoại");
        }else if(TextUtils.isEmpty(name_user)){
            edit_name_user.setError("Nhập tên người dùng");
        }else if(TextUtils.isEmpty(address)){
            edit_address.setError("Nhập địa chỉ");
        }else if(TextUtils.isEmpty(email)){
            edit_email.setError("Nhập email");
        }else if(TextUtils.isEmpty(password)){
            edit_pass.setError("Nhập mật khẩu");
        }else if (!Common.EMAIL_PATTERN.matcher(email).matches()) {
            edit_email.setError("Vui lòng nhập đúng email");
        } else if (password.length() <= 8) {
            edit_pass.setError("Mật khẩu ít nhất 8 kí tự");
        }else
        {
            managementRestaurant = new ManagementRestaurant();
            managementRestaurant.setRestaurantName(edit_name_restaurant.getText().toString());
            managementRestaurant.setPhone(edit_phone.getText().toString());
            Time opening = Time.valueOf(edit_opening.getText().toString());
            Time closing = Time.valueOf(edit_closing.getText().toString());
            managementRestaurant.setOpening(opening);
            managementRestaurant.setClosing(closing);
            managementRestaurant.setAddress(edit_address.getText().toString());
            managementRestaurant.setUsername(edit_name_user.getText().toString());
            managementRestaurant.setEmail(edit_email.getText().toString());
            managementRestaurant.setPassword(edit_pass.getText().toString());
            managementRestaurant.setStatus(0);
            managementRestaurant.setLat(mLat);
            managementRestaurant.setLng(mLng);
            managementRestaurant.setPermission("Chủ cửa hàng");
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Vui lòng chờ ...");
            progressDialog.show();
            boolean success=restaurantData.insertNewRegisterRestaurant(managementRestaurant);
            if (success == true) {
                progressDialog.dismiss();
                Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Không thể tạo tài khoản", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
