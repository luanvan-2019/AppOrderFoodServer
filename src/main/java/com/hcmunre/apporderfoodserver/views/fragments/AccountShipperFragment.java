package com.hcmunre.apporderfoodserver.views.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.views.activities.CategoryActivity;
import com.hcmunre.apporderfoodserver.views.activities.OrderHistoryActivity;
import com.hcmunre.apporderfoodserver.views.activities.PreferenceUtilsServer;
import com.hcmunre.apporderfoodserver.views.activities.ShipperActivity;
import com.hcmunre.apporderfoodserver.views.activities.SignInActivity;
import com.hcmunre.apporderfoodserver.views.activities.UserInfoActivity;

import butterknife.BindView;
import butterknife.Unbinder;

public class AccountShipperFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.linear_order_history)
    LinearLayout linear_order_history;
    @BindView(R.id.btn_sign_out)
    TextView btn_sign_out;
    @BindView(R.id.txt_name_user)
    TextView txt_name_user;
    public AccountShipperFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_account_shipper, container, false);
        eventClick();
        signOut();
        return view;
    }
    private void eventClick() {

        txt_name_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
            }
        });
        linear_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
            }
        });

    }
    private void signOut() {
        btn_sign_out.setOnClickListener(view1 -> {
            AlertDialog.Builder comfirmSignOut = new AlertDialog.Builder(view1.getContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có muốn đăng xuất không ?")
                    .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        Common.currentRestaurantOwner = null;
                        PreferenceUtilsServer.savePassword(null, getActivity());
                        PreferenceUtilsServer.saveEmail(null, getActivity());
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                        //logout phone
                    });
            comfirmSignOut.show();
        });
    }
}
