package com.hcmunre.apporderfoodserver.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hcmunre.apporderfoodserver.R;

public class NewOrderShipperFragment extends Fragment {


    public NewOrderShipperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_new_order_shipper, container, false);
        return view;
    }

}
