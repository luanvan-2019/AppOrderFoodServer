package com.hcmunre.apporderfoodserver.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.hcmunre.apporderfoodserver.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    double mLat, mLng;
    private static final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        init();
        eventClick();
    }

    private void init() {
        edit_phone.setText(PreferenceUtilsServer.getPhone(this));
        edit_name.setText(PreferenceUtilsServer.getName(this));
        edit_email.setText(PreferenceUtilsServer.getEmail(this));
        edit_address.setText(PreferenceUtilsServer.getAddress(this));
        edit_password.setText(PreferenceUtilsServer.getPassword(this));
        toolbar.setTitle(PreferenceUtilsServer.getName(this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void eventClick() {
        edit_address.setOnClickListener(view -> {
            setAddress();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
