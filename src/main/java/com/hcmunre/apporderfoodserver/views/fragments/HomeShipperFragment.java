package com.hcmunre.apporderfoodserver.views.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Entity.ShipperOrder;
import com.hcmunre.apporderfoodserver.views.activities.PreferenceUtilsServer;
import com.hcmunre.apporderfoodserver.views.activities.PreferenceUtilsShipper;
import com.hcmunre.apporderfoodserver.views.adapters.ShipperOrderAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeShipperFragment extends Fragment{
    Unbinder unbinder;
    @BindView(R.id.recyc_shiper_order)
    RecyclerView recyc_shiper_order;
    @BindView(R.id.linear_order_new)
    LinearLayout linear_order_new;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    ShipperData shipperData = new ShipperData();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ShipperOrderAdapter shipperOrderAdapter;
    private ArrayList<ShipperOrder> shipperOrderArrayList;

    public HomeShipperFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_shipper, container, false);
        unbinder=ButterKnife.bind(this,view);
        init();
        return view;
    }
    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyc_shiper_order.setLayoutManager(linearLayoutManager);
        recyc_shiper_order.setItemAnimator(new DefaultItemAnimator());
        swipe_layout.setColorSchemeResources(R.color.colorPrimary);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getOrderForShipper(0, 10,swipe_layout);
            }
        });
        new getOrderForShipper(0, 10,swipe_layout);
    }

//    private void getAllOrder(int startIndex, int endIndex) {
//        Observable<ArrayList<ShipperOrder>> shipperOrder = Observable.just(shipperData.getOrderForShipper(PreferenceUtilsServer.getUserId(getActivity()), startIndex, endIndex));
//        compositeDisposable.add(
//                shipperOrder
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(orders -> {
//                            if (orders.size() > 0) {
//                                shipperOrders = new ArrayList<>();
//                                shipperOrders = orders;
//                                shipperOrderAdapter = new ShipperOrderAdapter(getActivity(), shipperOrders);
//                                recyc_shiper_order.setAdapter(shipperOrderAdapter);
////                                shipperOrderAdapter.setLoadMore(getActivity());
//                                linear_order_new.setVisibility(View.GONE);
//                            } else {
//                                linear_order_new.setVisibility(View.VISIBLE);
////                                shipperOrders.remove(shipperOrders.size() - 1);
////                                shipperOrders = orders;
////                                shipperOrderAdapter.addItem(shipperOrders);
//                            }
//                        }, throwable -> {
//                            linear_order_new.setVisibility(View.VISIBLE);
//                            Log.d(Common.TAG, "Lỗi" + throwable.getMessage());
//                        })
//        );
//    }

    public class getOrderForShipper extends AsyncTask<String,String,ArrayList<ShipperOrder>>{
        int startIndex,endIndex;
        SwipeRefreshLayout swipeRefreshLayout;

        public getOrderForShipper(int startIndex, int endIndex, SwipeRefreshLayout swipeRefreshLayout) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.swipeRefreshLayout = swipeRefreshLayout;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            if(!swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ShipperOrder> shipperOrders) {
            if (shipperOrders.size() > 0) {
                shipperOrderArrayList = new ArrayList<>();
                shipperOrderArrayList = shipperOrders;
                shipperOrderAdapter = new ShipperOrderAdapter(getActivity(), shipperOrderArrayList);
                recyc_shiper_order.setAdapter(shipperOrderAdapter);
                linear_order_new.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                linear_order_new.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        protected ArrayList<ShipperOrder> doInBackground(String... strings) {
            ArrayList<ShipperOrder> shipperOrders;
            shipperOrders=shipperData.getOrderForShipper(PreferenceUtilsShipper.getUserId(getActivity()),startIndex,endIndex);
            return shipperOrders;
        }
    }

}
