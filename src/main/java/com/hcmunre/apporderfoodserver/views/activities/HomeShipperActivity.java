package com.hcmunre.apporderfoodserver.views.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.views.fragments.AccountFragment;
import com.hcmunre.apporderfoodserver.views.fragments.HomeShipperFragment;

public class HomeShipperActivity extends AppCompatActivity {
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_shipper);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_shipper);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeShipperFragment()).commit();
        }
        FirebaseMessaging.getInstance().subscribeToTopic(Common.getTopicChannelShippper(PreferenceUtilsShipper.getUserId(this)));

        }

    private BottomNavigationView.OnNavigationItemSelectedListener nav =
            menuItem -> {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_hơme:
                        selectedFragment = new HomeShipperFragment();
                        break;
                    case R.id.nav_user:
                        selectedFragment = new AccountFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            };

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thoát ứng dụng");
        builder.setMessage("Bạn có muốn thoát không?");
        builder.setCancelable(true);
        builder.setPositiveButton("Thoát", (dialog, which) -> finish());
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
