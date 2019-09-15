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
import com.hcmunre.apporderfoodserver.models.BannerModel;
import com.hcmunre.apporderfoodserver.views.SearchActivity;
import com.hcmunre.apporderfoodserver.views.adapters.TabFragmentAdapter;

import java.util.ArrayList;
import java.util.Timer;

public class HomeFragment extends Fragment {
    LinearLayout layout_search;
    TextView txtsearch;
    TabLayout tabLayout;
    ViewPager viewPager,bannerslider;
    TabFragmentAdapter tabFragmentAdapter;
//    int currentpage=2;
//    private Timer timer;
//    final private long DELAY_TIME=2000;
//    final private long PERIOD_TIME=2000;
//    private ArrayList<BannerModel> mBannerModels;
//    SliderLayout sliderLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,
                container, false);
        layout_search = rootView.findViewById(R.id.layout_search);
        txtsearch=rootView.findViewById(R.id.txtsearch);
        tabLayout=rootView.findViewById(R.id.tablayout);
        viewPager=rootView.findViewById(R.id.viewpager);

        ////banner slider
//        bannerslider=rootView.findViewById(R.id.viewpager_slider);
//        mBannerModels=new ArrayList<BannerModel>();
//        mBannerModels.add(new BannerModel(1,R.drawable.ic_eat));
//        mBannerModels.add(new BannerModel(1,R.drawable.ic_eat));
//        mBannerModels.add(new BannerModel(1,R.drawable.ic_eat));
//        mBannerModels.add(new BannerModel(1,R.drawable.ic_eat));
//        BannerAdapter bannerAdapter=new BannerAdapter(mBannerModels);
//        bannerslider.setAdapter(bannerAdapter);
//        bannerslider.setClipToPadding(false);
//        bannerslider.setPageMargin(10);
//        ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                currentpage=position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                if(state==ViewPager.SCROLL_STATE_IDLE){
//                    pageLooper();
//                }
//            }
//        };
//        bannerslider.addOnPageChangeListener(onPageChangeListener);
//        startBannerSlideShow();
//        bannerslider.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                pageLooper();
//                stopBannerSlideShow();
//                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
//                    startBannerSlideShow();
//                }
//                return false;
//            }
//        });

        ///banner slider
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
//    private void pageLooper(){
//        if(currentpage==mBannerModels.size()-2){
//            currentpage=2;
//            bannerslider.setCurrentItem(currentpage,false);
//        }
//        if(currentpage==1){
//            currentpage=mBannerModels.size()-3;
//            bannerslider.setCurrentItem(currentpage,false);
//        }
//    }
//    private void startBannerSlideShow(){
//        final Handler handler=new Handler();
//        final Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
//                if(currentpage>=mBannerModels.size()){
//                    currentpage=1;
//                }
//                bannerslider.setCurrentItem(currentpage++,true);
//            }
//        };
//        timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(runnable);
//            }
//        },DELAY_TIME,PERIOD_TIME);
//    }
//    private void stopBannerSlideShow(){
//        timer.cancel();
//    }

}
