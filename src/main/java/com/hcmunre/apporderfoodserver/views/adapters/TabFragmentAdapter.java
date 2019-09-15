package com.hcmunre.apporderfoodserver.views.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hcmunre.apporderfoodserver.views.fragments.ConfirmedFragment;
import com.hcmunre.apporderfoodserver.views.fragments.OrderListFragment;
import com.hcmunre.apporderfoodserver.views.fragments.CancelledFragment;

public class TabFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumofTabs;
    public TabFragmentAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.mNumofTabs = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OrderListFragment orderListFragment = new OrderListFragment();
                return orderListFragment;

            case 1:
                ConfirmedFragment confirmedFragment = new ConfirmedFragment();
                return confirmedFragment;

            case 2:
                CancelledFragment cancelledFragment = new CancelledFragment();
                return cancelledFragment;

            default:
                return null;
        }
    }
        @Override
        public int getCount()

        { return mNumofTabs;}

}
