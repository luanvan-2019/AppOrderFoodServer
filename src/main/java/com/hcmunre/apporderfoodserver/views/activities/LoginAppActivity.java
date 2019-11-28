package com.hcmunre.apporderfoodserver.views.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmunre.apporderfoodserver.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginAppActivity extends AppCompatActivity {
    @BindView(R.id.btn_shipper)
    Button btn_shipper;
    @BindView(R.id.btn_restaurant)
    Button btn_restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app);
        ButterKnife.bind(this);
        Dexter.withActivity(this)
                .withPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
        btn_shipper.setOnClickListener(v -> {
            startActivity(new Intent(LoginAppActivity.this, SignInShipperActivity.class));
        });
        btn_restaurant.setOnClickListener(v -> startActivity(new Intent(LoginAppActivity.this, SignInActivity.class)));
    }
}
