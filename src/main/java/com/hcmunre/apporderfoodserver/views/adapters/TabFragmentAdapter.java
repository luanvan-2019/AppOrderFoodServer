package com.hcmunre.apporderfoodserver.views.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hcmunre.apporderfoodserver.views.fragments.ConfirmedTabFragment;
import com.hcmunre.apporderfoodserver.views.fragments.OrderListTabFragment;
import com.hcmunre.apporderfoodserver.views.fragments.CancelledTabFragment;

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
                OrderListTabFragment orderListTabFragment = new OrderListTabFragment();
                return orderListTabFragment;

            case 1:
                ConfirmedTabFragment confirmedTabFragment = new ConfirmedTabFragment();
                return confirmedTabFragment;

            case 2:
                CancelledTabFragment cancelledTabFragment = new CancelledTabFragment();
                return cancelledTabFragment;

            default:
                return null;
        }
    }
        @Override
        public int getCount()

        { return mNumofTabs;}

}
