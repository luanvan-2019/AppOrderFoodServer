package com.hcmunre.apporderfoodserver.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Database.RestaurantData;
import com.hcmunre.apporderfoodserver.models.Entity.ManagementRestaurant;
import com.hcmunre.apporderfoodserver.views.adapters.RestaurantAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManagementRestaurantActivity extends AppCompatActivity {
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @BindView(R.id.recyc_management_restaurant)
    RecyclerView recyc_management_restaurant;
    RestaurantData restaurantData=new RestaurantData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_restaurant);
        ButterKnife.bind(this);
        init();
        new getNewRestaurant().execute();
    }
    private void init(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyc_management_restaurant.setLayoutManager(linearLayoutManager);
        recyc_management_restaurant.setItemAnimator(new DefaultItemAnimator());
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getNewRestaurant().execute();
            }
        });

    }
    public class getNewRestaurant extends AsyncTask<String,String, ArrayList<ManagementRestaurant>>{
        @Override
        protected void onPreExecute() {
            if(!swipe_layout.isRefreshing()){
                swipe_layout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ManagementRestaurant> managementRestaurants) {
            if(managementRestaurants.size()>0){
                RestaurantAdapter restaurantAdapter=new RestaurantAdapter(ManagementRestaurantActivity.this,managementRestaurants);
                recyc_management_restaurant.setAdapter(restaurantAdapter);
                swipe_layout.setRefreshing(false);
            }else {
                Log.d("BBB","Hiện không có cửa hàng mới");
            }

        }

        @Override
        protected ArrayList<ManagementRestaurant> doInBackground(String... strings) {
            ArrayList<ManagementRestaurant> managementRestaurants;
            managementRestaurants=restaurantData.getNewRegisterRestaurant();
            return managementRestaurants;
        }
    }
}
