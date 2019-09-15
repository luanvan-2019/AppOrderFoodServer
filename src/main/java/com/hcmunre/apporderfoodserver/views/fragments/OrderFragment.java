package com.hcmunre.apporderfoodserver.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.views.adapters.TabFragmentAdapter;


public class OrderFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabFragmentAdapter tabFragmentAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order,
                container, false);
        tabLayout=rootView.findViewById(R.id.tab_order);
        viewPager=rootView.findViewById(R.id.viewpager_order);
//        listOrder();
        return rootView;
    }
//    private void listOrder(){
//        tabLayout.addTab(tabLayout.newTab().setText("Lịch sử"));
//        tabLayout.addTab(tabLayout.newTab().setText("Đơn nháp"));
//        tabFragmentAdapter= new TabFragmentAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(tabFragmentAdapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
}
