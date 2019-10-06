package com.hcmunre.apporderfoodserver.views.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.views.fragments.AccountFragment;
import com.hcmunre.apporderfoodserver.views.fragments.HomeFragment;
import com.hcmunre.apporderfoodserver.views.fragments.MenuFragment;
import com.hcmunre.apporderfoodserver.views.fragments.ReportFragment;

public class HomeActivity extends AppCompatActivity {
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_hơme:
                            selectedFragment = new HomeFragment();

                            break;
                        case R.id.nav_assignment:
                            selectedFragment = new MenuFragment();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new ReportFragment();
                            break;
                        case R.id.nav_user:
                            selectedFragment = new AccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}
