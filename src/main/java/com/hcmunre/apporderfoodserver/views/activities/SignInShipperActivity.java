package com.hcmunre.apporderfoodserver.views.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInShipperActivity extends AppCompatActivity {
    @BindView(R.id.btn_signin_shipper)
    Button btn_signin_shipper;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPass)
    EditText editPass;
    String usernam, passwordd;
    Shipper shipperLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_shipper);
        ButterKnife.bind(this);
        btn_signin_shipper.setOnClickListener(v -> {
            usernam = editEmail.getText().toString();
            passwordd = editPass.getText().toString();
            new CheckLoginShiper(SignInShipperActivity.this).execute();
        });
    }

    public class CheckLoginShiper extends AsyncTask<String, String, Shipper> {

        private ProgressDialog mDialog;
        private Context mContext = null;

        public CheckLoginShiper(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Đang đăng nhập ...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Shipper shipper) {
            mDialog.dismiss();
            if (usernam.trim().equals("") || passwordd.trim().equals("")) {
                Toast.makeText(mContext, "Vui lòng nhập tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            } else if (shipper != null) {
                Common.currentShiper = shipper;
                PreferenceUtilsShipper.saveEmail(usernam, SignInShipperActivity.this);
                PreferenceUtilsShipper.savePassword(passwordd, SignInShipperActivity.this);
                PreferenceUtilsShipper.saveUserId(Common.currentShiper.getId(), SignInShipperActivity.this);
                PreferenceUtilsShipper.saveName(Common.currentShiper.getName(), SignInShipperActivity.this);
                PreferenceUtilsShipper.savePhone(Common.currentShiper.getPhone(), SignInShipperActivity.this);
                PreferenceUtilsShipper.saveAddress(Common.currentShiper.getAddress(), SignInShipperActivity.this);
                startActivity(new Intent(SignInShipperActivity.this, HomeShipperActivity.class));
                finish();
            } else {
                Toast.makeText(mContext, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        }

        //đây là luồng bên ngoài để xử lý logic
        @SuppressLint("WrongThread")
        @Override
        protected Shipper doInBackground(String... params)//...mảng dạng array
        {
            Shipper shipper= new Shipper();
            ShipperData shipperData = new ShipperData();
            shipperLogin = new Shipper();
            shipperLogin.setEmail(usernam);
            shipperLogin.setPassword(passwordd);
            shipper = shipperData.getInforShipper(shipperLogin);

            return shipper;
        }
    }

}
