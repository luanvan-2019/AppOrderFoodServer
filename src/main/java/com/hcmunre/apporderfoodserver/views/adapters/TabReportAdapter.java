package com.hcmunre.apporderfoodserver.views.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hcmunre.apporderfoodserver.views.fragments.FoodReportFragment;
import com.hcmunre.apporderfoodserver.views.fragments.ResReportTabFragment;

public class TabReportAdapter extends FragmentStatePagerAdapter {
    int mNumberTabs;
    public TabReportAdapter(FragmentManager fm, int mNumberTabs){
        super(fm);
        this.mNumberTabs=mNumberTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ResReportTabFragment tabReportFragment=new ResReportTabFragment();
                return tabReportFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumberTabs;
    }
}
