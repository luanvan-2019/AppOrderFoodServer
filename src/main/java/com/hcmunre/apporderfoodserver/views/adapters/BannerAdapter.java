package com.hcmunre.apporderfoodserver.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Entity.BannerModel;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {
    private ArrayList<BannerModel> mBannerModels;

    public BannerAdapter(ArrayList<BannerModel> mBannerModels) {
        this.mBannerModels = mBannerModels;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout,container,false);
        ImageView banner=view.findViewById(R.id.banner_slider);
        banner.setImageResource(mBannerModels.get(position).getImage());
        container.addView(view,0);
        return view;
    }

    @Override
    public int getCount() {
        return mBannerModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
