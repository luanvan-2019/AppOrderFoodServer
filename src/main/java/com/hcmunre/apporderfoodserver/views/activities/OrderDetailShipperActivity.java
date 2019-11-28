package com.hcmunre.apporderfoodserver.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Entity.OrderDetail;
import com.hcmunre.apporderfoodserver.views.adapters.OrderDetailAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailShipperActivity extends AppCompatActivity {
    @BindView(R.id.txt_phone)
    TextView txt_phone;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.txt_confirm)
    TextView txt_confirm;
    @BindView(R.id.recyc_order_detail)
    RecyclerView recyc_order_detail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_phone_call)
    ImageView image_phone_call;
    OrderData orderData=new OrderData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_shipper);
        ButterKnife.bind(this);
        init();
        eventClick();
        contactCustomer();
        new getOrderDetailShipper().execute();
    }

    private void init() {
        toolbar.setTitle(Common.currrentShipperOrder.getOrderName());
        toolbar.setSubtitle("Id#"+Common.currrentShipperOrder.getOrderID());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_phone.setText(Common.currrentShipperOrder.getOrderPhone());
        txt_total_price.setText(new StringBuilder(Common.currrentShipperOrder.getTotalPrice()+"").append("đ"));
        //
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyc_order_detail.setLayoutManager(linearLayoutManager);
        recyc_order_detail.setItemAnimator(new DefaultItemAnimator());
    }
    private void eventClick()
    {
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetailShipperActivity.this,ShipperMapActivity.class));
            }
        });
    }
    private void contactCustomer(){
        image_phone_call.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+ Common.currrentShipperOrder.getOrderPhone()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }else {
                startActivity(intent);
            }

        });
    }
    public class getOrderDetailShipper extends AsyncTask<String,String,ArrayList<OrderDetail>> {
        @Override
        protected void onPostExecute(ArrayList<OrderDetail> orderDetails) {
            OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(OrderDetailShipperActivity.this, orderDetails);
            recyc_order_detail.setAdapter(orderDetailAdapter);
            orderDetailAdapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<OrderDetail> doInBackground(String... strings) {
            ArrayList<OrderDetail> orderDetails;
            orderDetails=orderData.getOrderDetail(Common.currrentShipperOrder.getOrderID());
            return orderDetails;
        }
    }

}
