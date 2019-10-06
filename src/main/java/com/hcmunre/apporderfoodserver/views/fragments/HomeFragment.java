package com.hcmunre.apporderfoodserver.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.views.activities.SearchActivity;
import com.hcmunre.apporderfoodserver.views.adapters.TabFragmentAdapter;

public class HomeFragment extends Fragment {
    LinearLayout layout_search;
    TextView txtsearch;
    TabLayout tabLayout;
    ViewPager viewPager,bannerslider;
    TabFragmentAdapter tabFragmentAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,
                container, false);
        layout_search = rootView.findViewById(R.id.layout_search);
        txtsearch=rootView.findViewById(R.id.txtsearch);
        tabLayout=rootView.findViewById(R.id.tablayout);
        viewPager=rootView.findViewById(R.id.viewpager);
        listMenu();
        layout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        txtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchtxt();
            }
        });
        return rootView;
    }

    public void search() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }
    public void searchtxt() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    private void listMenu(){
        tabLayout.addTab(tabLayout.newTab().setText("Đơn mới"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã nhận"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã hủy"));
        tabFragmentAdapter = new TabFragmentAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(tabFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
