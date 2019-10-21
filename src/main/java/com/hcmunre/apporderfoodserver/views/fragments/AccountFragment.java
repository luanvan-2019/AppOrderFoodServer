package com.hcmunre.apporderfoodserver.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.entity.RestaurantOwner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AccountFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.btn_update)
    Button btn_update;
    @BindView(R.id.btn_sign_out)
    Button btn_sign_out;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.edit_address)
    EditText edit_address;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_account,container,false);
      unbinder= ButterKnife.bind(this,view);
      btn_update.setOnClickListener(view1 -> edit_name.setEnabled(true));
        init();
      return view;
    }
    private void init(){
        getInforRestaurantOwner();
    }
    private void getInforRestaurantOwner(){
        edit_name.setText(Common.currentRestaurantOwner.getName());
        edit_email.setText(Common.currentRestaurantOwner.getEmail());
        edit_phone.setText(Common.currentRestaurantOwner.getPhone());
        edit_address.setText(Common.currentRestaurantOwner.getAddress());
    }
}
