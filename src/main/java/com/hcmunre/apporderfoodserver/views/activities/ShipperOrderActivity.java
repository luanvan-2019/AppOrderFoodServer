package com.hcmunre.apporderfoodserver.views.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.Interfaces.LoadMore;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Entity.MaxOrder;
import com.hcmunre.apporderfoodserver.models.Entity.ShipperOrder;
import com.hcmunre.apporderfoodserver.views.adapters.ShipperOrderAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ShipperOrderActivity extends AppCompatActivity implements LoadMore {
    @BindView(R.id.recyc_shiper_order)
    RecyclerView recyc_shiper_order;
    @BindView(R.id.linear_order_new)
    LinearLayout linear_order_new;
    ShipperData shipperData = new ShipperData();
    int maxData = 0;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ShipperOrderAdapter shipperOrderAdapter;
    private ArrayList<ShipperOrder> shipperOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_order);
        ButterKnife.bind(this);
        init();
        getMaxOrder();
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyc_shiper_order.setLayoutManager(linearLayoutManager);
        recyc_shiper_order.setHasFixedSize(true);
        recyc_shiper_order.setItemAnimator(new DefaultItemAnimator());
    }

    private void getMaxOrder() {
        Observable<MaxOrder> maxNumOrder = Observable.just(shipperData.getMaxOrderForShipper(Common.currentRestaurant.getmId()));
        compositeDisposable.add(
                maxNumOrder
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(maxOrder -> {
                            maxData = maxOrder.getMaxRowNum();
                            getAllOrder(0, 10);
                        }, throwable -> {
                            Toast.makeText(this, "Lỗi MaxOrder " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
    }

    private void getAllOrder(int startIndex, int endIndex) {
        Observable<ArrayList<ShipperOrder>> shipperOrder = Observable.just(shipperData.getOrderForShipper(2, startIndex, endIndex));
        compositeDisposable.add(
                shipperOrder
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(orders -> {
                            if (orders.size() > 0) {
                                shipperOrders = new ArrayList<>();
                                shipperOrders = orders;
                                shipperOrderAdapter = new ShipperOrderAdapter(this, shipperOrders);
                                recyc_shiper_order.setAdapter(shipperOrderAdapter);
                                shipperOrderAdapter.setLoadMore(this);
                                linear_order_new.setVisibility(View.GONE);
                            } else {
                                linear_order_new.setVisibility(View.VISIBLE);
                                shipperOrders.remove(shipperOrders.size() - 1);
                                shipperOrders = orders;
                                shipperOrderAdapter.addItem(shipperOrders);
                            }
                        }, throwable -> {
                            linear_order_new.setVisibility(View.VISIBLE);
                            Log.d(Common.TAG, "Lỗi" + throwable.getMessage());
                        })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onLoadMore() {
        if (shipperOrderAdapter.getItemCount() < maxData) {
            shipperOrders.add(null);//add null để show loading progress
            shipperOrderAdapter.notifyItemInserted(shipperOrders.size() - 1);
            getAllOrder(shipperOrders.size() + 1, shipperOrders.size() + 10);//get next 10 item
        } else {
            Toast.makeText(this, "Max to data", Toast.LENGTH_SHORT).show();
        }
    }
}
