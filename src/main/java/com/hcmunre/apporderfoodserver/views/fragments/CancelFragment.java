package com.hcmunre.apporderfoodserver.views.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
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
import com.hcmunre.apporderfoodserver.views.adapters.OrderCancelAdatper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CancelFragment extends Fragment implements LoadMore {

    Unbinder unbinder;
    @BindView(R.id.recyc_cancel_order)
    RecyclerView recyc_cancel_order;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @BindView(R.id.linear_order_new)
    LinearLayout linear_order_new;
    private ArrayList<Order> orderArrayList;
    private OrderCancelAdatper orderAdatper;
    OrderData orderData=new OrderData();
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    int maxData=0;
    public CancelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cancel, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        getMaxOrder();
        return view;
    }
    private void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyc_cancel_order.setLayoutManager(layoutManager);
        recyc_cancel_order.setItemAnimator(new DefaultItemAnimator());
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
                                    new getAllOrderCanceled(0,10,swipe_layout);
                                }
                            });
                            new getAllOrderCanceled(0,10,swipe_layout);
                        }, throwable -> {
                            Toast.makeText(getActivity(), "Lỗi MaxOrder "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
    }
    public class getAllOrderCanceled extends AsyncTask<String,String,ArrayList<Order>> {
        int startIndex,endIndex;
        SwipeRefreshLayout swipeRefreshLayout;
        public getAllOrderCanceled(int startIndex, int endIndex,SwipeRefreshLayout swipeRefreshLayout) {
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
                orderAdatper = new OrderCancelAdatper(getActivity(), orderArrayList);
                recyc_cancel_order.setAdapter(orderAdatper);
                orderAdatper.refresh(orderArrayList);
                orderAdatper.setLoadMore(this::onPreExecute);
                linear_order_new.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }else{
                swipeRefreshLayout.setRefreshing(false);
                linear_order_new.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected ArrayList<Order> doInBackground(String... strings) {
            ArrayList<Order> orders;
            orders=orderData.getAllOrder(PreferenceUtilsServer.getUserId(getActivity()),
                    5,startIndex,endIndex);
            return orders;
        }
    }
    @Override
    public void onLoadMore() {
        if(orderAdatper.getItemCount()<maxData){
            orderArrayList.add(null);//add null để show loading progress
            orderAdatper.notifyItemInserted(orderArrayList.size()-1);
            new getAllOrderCanceled(orderArrayList.size()+1,orderArrayList.size()+10,swipe_layout);//get next 10 item
        }else {
            Toast.makeText(getActivity(), "Max to data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
