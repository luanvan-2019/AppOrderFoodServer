package com.hcmunre.apporderfoodserver.views.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hcmunre.apporderfoodserver.views.fragments.CancelFragment;
import com.hcmunre.apporderfoodserver.views.fragments.NewOrderFragment;
import com.hcmunre.apporderfoodserver.views.fragments.ReceivedFragment;

public class TabOrderFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumofTab;

    public TabOrderFragmentAdapter(FragmentManager fm, int mNumofTab) {
        super(fm);
        this.mNumofTab = mNumofTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewOrderFragment orderFragment = new NewOrderFragment();
                return orderFragment;
            case 1:
                ReceivedFragment receivedFragment = new ReceivedFragment();
                return receivedFragment;
            case 2:
                CancelFragment cancelFragment=new CancelFragment();
                return cancelFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumofTab;
    }
}
