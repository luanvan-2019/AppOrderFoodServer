package com.hcmunre.apporderfoodserver.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Database.DataConnetion;
import com.hcmunre.apporderfoodserver.models.Database.User;
import com.hcmunre.apporderfoodserver.models.SignInModel;

import java.sql.Connection;

public class SignInActivity extends AppCompatActivity {
    Button btnSigin;
    LoginButton loginButton;
    EditText editEmail, editPass,editEmailFB,editBrithday,editName;
    TextView txtSignup,txtForgetPass;
    String usernam, passwordd;
    User user;
    CallbackManager callbackManager;
    ProgressBar progressBar;
    ImageView imageView;
    private Object LoginResult;
    Connection con;
    DataConnetion dataCon = new DataConnetion();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSigin = findViewById(R.id.btndangnhap);
        txtSignup = findViewById(R.id.txtDangki);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editMatkhau);
        txtForgetPass=findViewById(R.id.txtforget);
        //
        progressBar = findViewById(R.id.progress_login);
        progressBar.setVisibility(View.GONE);
        btnSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernam = editEmail.getText().toString();
                passwordd = editPass.getText().toString();
                new CheckLogin(SignInActivity.this).execute();
            }
        });
        listenClickSignup();
        listenClickForgetPass();
    }
    private void listenClickSignup(){
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

    }
    private void listenClickForgetPass(){
        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignInActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });
    }

    public class CheckLogin extends AsyncTask<String,String,String>//<params:giá trị truyền vào phần xử lý logic,progress,result>
    {
        //progress:giá trị mà xử lý logic nó bắn ra cho onProgressUpdate
        //resul: khi thực thi xong luong tra ve gì thì truyền qua cho onPostExecute
        private ProgressDialog mDialog;
        private Context mContext=null;
        public CheckLogin(Context context){
            mContext=context;
        }
        String z="";
        boolean isSuccess=false;
        @Override
        protected void onPreExecute()
        {
            mDialog=new ProgressDialog(mContext);
            mDialog.setMessage("Đang đăng nhập ...");
            mDialog.show();
        }
        @Override
        protected void onPostExecute(String r)
        {
            mDialog.dismiss();
            if (z!="success"){
                Toast.makeText(SignInActivity.this, r, Toast.LENGTH_SHORT).show();
            }else {
                Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
        //đây là luồng bên ngoài để xử lý logic
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params)//...mảng dạng array
        {

            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                z = "Vui lòng nhập tên đăng nhập hoặc mật khẩu";
            else{
                SignInModel userModel = new SignInModel();
                user = new User(usernam, passwordd);
                z = userModel.DangNhap(user);
            }
            return z;
        }
    }
}
