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
import com.hcmunre.apporderfoodserver.views.adapters.CustomViewPager;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.views.adapters.TabOrderFragmentAdapter;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment  {
    Unbinder unbinder;
    @BindView(R.id.tablayout_report)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_report)
    ViewPager viewPager;
    TabOrderFragmentAdapter tabOrderFragmentAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        unbinder= ButterKnife.bind(this,view);
        tabOrder();
        return view;
    }
    private void tabOrder(){
        tabLayout.addTab(tabLayout.newTab().setText("Đơn mới"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã nhận"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã hủy"));
        tabOrderFragmentAdapter = new TabOrderFragmentAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(tabOrderFragmentAdapter);
        CustomViewPager customViewPager=new CustomViewPager(getActivity());
        customViewPager.setPagingEnabled(false);
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
