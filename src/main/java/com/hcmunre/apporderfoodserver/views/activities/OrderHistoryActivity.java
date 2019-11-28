package com.hcmunre.apporderfoodserver.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.views.adapters.OrderHistoryAdatper;
import com.hcmunre.apporderfoodserver.views.fragments.NewOrderFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @BindView(R.id.recyc_order_history)
    RecyclerView recyc_order_history;
    @BindView(R.id.linear_order_new)
    LinearLayout linear_order_new;
    OrderData orderData = new OrderData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        toolbar.setTitle("Lịch sử đơn hàng");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyc_order_history.setLayoutManager(linearLayoutManager);
        recyc_order_history.setItemAnimator(new DefaultItemAnimator());
        swipe_layout.setColorSchemeResources(R.color.colorPrimary);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getAllOrder(swipe_layout);
            }
        });

        new getAllOrder(swipe_layout);
    }

    public class getAllOrder extends AsyncTask<String, String, ArrayList<Order>> {
        SwipeRefreshLayout swipeRefreshLayout;

        public getAllOrder(SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Order> orders) {
            if (orders.size() > 0) {
                ArrayList<Order> orderArrayList = new ArrayList<>();
                orderArrayList = orders;
                OrderHistoryAdatper orderAdatper = new OrderHistoryAdatper(OrderHistoryActivity.this, orders);
                recyc_order_history.setAdapter(orderAdatper);
                orderAdatper.refresh(orderArrayList);
                orderAdatper.setLoadMore(this::onPreExecute);
                linear_order_new.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                linear_order_new.setVisibility(View.VISIBLE);
            }
        }
        @Override
        protected ArrayList<Order> doInBackground(String... strings) {
            ArrayList<Order> orders;
            orders=orderData.getAllOrder(PreferenceUtilsServer.getUserId(OrderHistoryActivity.this),
                    4,0,10);
            return orders;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
