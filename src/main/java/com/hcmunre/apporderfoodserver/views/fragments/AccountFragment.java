package com.hcmunre.apporderfoodserver.views.fragments;

import android.app.AlertDialog;
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

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.views.activities.CategoryActivity;
import com.hcmunre.apporderfoodserver.views.activities.OrderHistoryActivity;
import com.hcmunre.apporderfoodserver.views.activities.PreferenceUtilsServer;
import com.hcmunre.apporderfoodserver.views.activities.ShipperActivity;
import com.hcmunre.apporderfoodserver.views.activities.SignInActivity;
import com.hcmunre.apporderfoodserver.views.activities.UserInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AccountFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.linear_order_history)
    LinearLayout linear_order_history;
    @BindView(R.id.btn_sign_out)
    TextView btn_sign_out;
    @BindView(R.id.btn_shipper)
    TextView btn_shipper;
    @BindView(R.id.btn_staff)
    TextView btn_staff;
    @BindView(R.id.btn_category)
    TextView btn_category;
    @BindView(R.id.txt_name_user)
    TextView txt_name_user;
    @BindView(R.id.linear_shipper)
    LinearLayout linear_shipper;
    @BindView(R.id.linear_management_category)
    LinearLayout linear_management_category;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        eventClick();
        signOut();
        return view;
    }

    private void eventClick() {
        txt_name_user.setText(PreferenceUtilsServer.getName(getActivity()));
        String permission=PreferenceUtilsServer.getPermission(getActivity());
        if(permission.equals("Nhân viên")){
            linear_shipper.setVisibility(View.GONE);
            linear_management_category.setVisibility(View.GONE);
        }
        if(permission.equals("Chủ cửa hàng")){
            linear_management_category.setVisibility(View.GONE);
        }
        linear_shipper.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ShipperActivity.class));
        });
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CategoryActivity.class));
            }
        });
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
