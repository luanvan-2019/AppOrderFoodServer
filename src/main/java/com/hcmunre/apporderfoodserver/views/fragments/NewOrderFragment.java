package com.hcmunre.apporderfoodserver.views.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmunre.apporderfoodserver.Interfaces.LoadMore;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Entity.MaxOrder;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.views.activities.PreferenceUtilsServer;
import com.hcmunre.apporderfoodserver.views.adapters.OrderAdatper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewOrderFragment extends Fragment implements LoadMore {

    Unbinder unbinder;
    @BindView(R.id.recyc_order)
    RecyclerView recyc_order;
    @BindView(R.id.linear_order_new)
    LinearLayout linear_order_new;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    private ArrayList<Order> orderArrayList;
    private OrderAdatper orderAdatper;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    OrderData orderData=new OrderData();
    int maxData=0;
    public NewOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_new_order, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        getMaxOrder();
        return view;
    }
    private void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyc_order.setLayoutManager(layoutManager);
        recyc_order.setItemAnimator(new DefaultItemAnimator());
        //swipe
        swipe_layout.setColorSchemeResources(R.color.colorPrimary);
        //swipe

    }
    private void getMaxOrder(){
        Observable<MaxOrder> maxNumOrder=Observable.just(orderData.getMaxOrder(PreferenceUtilsServer.getUserId(getActivity()),0));
        compositeDisposable.add(
                maxNumOrder
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(maxOrder -> {
                            maxData=maxOrder.getMaxRowNum();
                            swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    new getAllOrder(0,10,swipe_layout);
                                }
                            });

                            new getAllOrder(0,10,swipe_layout);
                        }, throwable -> {
                            Toast.makeText(getActivity(), "Lỗi MaxOrder "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
    }
    public class getAllOrder extends AsyncTask<String,String,ArrayList<Order>> {
        int startIndex,endIndex;
        SwipeRefreshLayout swipeRefreshLayout;
        public getAllOrder(int startIndex, int endIndex,SwipeRefreshLayout swipeRefreshLayout) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.swipeRefreshLayout=swipeRefreshLayout;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            if(!swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Order> orders) {
            if(orders.size()>0){
                orderArrayList=new ArrayList<>();
                orderArrayList=orders;
                orderAdatper = new OrderAdatper(getActivity(), orderArrayList);
                recyc_order.setAdapter(orderAdatper);

                orderAdatper.refresh(orderArrayList);
                orderAdatper.setLoadMore(this::onPreExecute);
                linear_order_new.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }else{
                swipeRefreshLayout.setRefreshing(false);
                linear_order_new.setVisibility(View.VISIBLE);
//                orderArrayList.remove(orderArrayList.size()-1);
//                orderArrayList=orders;
//                orderAdatper.addItem(orderArrayList);
            }
        }

        @Override
        protected ArrayList<Order> doInBackground(String... strings) {
            ArrayList<Order> orders;
            orders=orderData.getAllOrder(PreferenceUtilsServer.getUserId(getActivity()),
                    0,startIndex,endIndex);
            return orders;
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onLoadMore() {
        if(orderAdatper.getItemCount()<maxData){
            orderArrayList.add(null);//add null để show loading progress
            orderAdatper.notifyItemInserted(orderArrayList.size()-1);
            new getAllOrder(orderArrayList.size()+1,orderArrayList.size()+10,swipe_layout);//get next 10 item
        }else {
            Toast.makeText(getActivity(), "Max to data", Toast.LENGTH_SHORT).show();
        }
    }
    private void refresh(int m){
        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.postDelayed(runnable,2000);
    }

}
