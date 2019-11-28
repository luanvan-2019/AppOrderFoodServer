package com.hcmunre.apporderfoodserver.views.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Database.SignInData;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.btnSigin)
    Button btnSigin;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPass)
    EditText editPass;
    @BindView(R.id.txtSignup)
    TextView txtSignup;
    @BindView(R.id.txtForgetPass)
    TextView txtForgetPass;
    String usernam, passwordd;
    RestaurantOwner restaurantOwner;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        //
        btnSigin.setOnClickListener(v -> {
            usernam = editEmail.getText().toString();
            passwordd = editPass.getText().toString();
            new CheckLoginBK(SignInActivity.this).execute();
        });
        listenClickSignup();
        listenClickForgetPass();
        PreferenceUtilsServer utils = new PreferenceUtilsServer();

        if (utils.getEmail(this) != null) {
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
        }
    }

    private void listenClickSignup() {
        txtSignup.setOnClickListener(view -> {
            Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(i);
        });

    }

    private void listenClickForgetPass() {
        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });
    }



    public class CheckLoginBK extends AsyncTask<String, String, RestaurantOwner>//<params:giá trị truyền vào phần xử lý logic,progress,result>
    {
        //progress:giá trị mà xử lý logic nó bắn ra cho onProgressUpdate
        //resul: khi thực thi xong luong tra ve gì thì truyền qua cho onPostExecute
        private ProgressDialog mDialog;
        private Context mContext = null;

        public CheckLoginBK(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Đang đăng nhập ...");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(RestaurantOwner restaurantOwner) {
            mDialog.dismiss();
            if (usernam.trim().equals("") || passwordd.trim().equals("")) {
                Toast.makeText(mContext, "Vui lòng nhập tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            } else if (restaurantOwner != null) {
                Common.currentRestaurantOwner = restaurantOwner;
                PreferenceUtilsServer.saveEmail(usernam, SignInActivity.this);
                PreferenceUtilsServer.savePassword(passwordd, SignInActivity.this);
                PreferenceUtilsServer.saveUserId(Common.currentRestaurantOwner.getRestaurantId(), SignInActivity.this);
                PreferenceUtilsServer.saveName(Common.currentRestaurantOwner.getName(), SignInActivity.this);
                PreferenceUtilsServer.saveRestaurantName(Common.currentRestaurantOwner.getRestaurantName(),SignInActivity.this);
                PreferenceUtilsServer.saveRestaurantAddress(Common.currentRestaurantOwner.getRestaurantAddress(),SignInActivity.this);
                PreferenceUtilsServer.savePhone(Common.currentRestaurantOwner.getPhone(), SignInActivity.this);
                PreferenceUtilsServer.saveAddress(Common.currentRestaurantOwner.getAddress(), SignInActivity.this);
                Intent intent=new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(mContext, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected RestaurantOwner doInBackground(String... params)//...mảng dạng array
        {
            RestaurantOwner restaurantOwner1;
            SignInData signInData = new SignInData();
            RestaurantOwner restaurantOwner = new RestaurantOwner();
            restaurantOwner.setEmail(usernam);
            restaurantOwner.setPassword(passwordd);
            restaurantOwner1 = new RestaurantOwner();
            restaurantOwner1 = signInData.getInforRestaurant(restaurantOwner);
            return restaurantOwner1;
        }
    }
}
