package com.hcmunre.apporderfoodserver.views.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.RestaurantData;
import com.hcmunre.apporderfoodserver.models.Entity.ManagementRestaurant;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;
import com.hcmunre.apporderfoodserver.views.activities.OrderDetailActivity;

import java.sql.Time;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ManagementRestaurant> restaurantArrayList;
    RestaurantData restaurantData=new RestaurantData();
    public RestaurantAdapter(Context mContext, ArrayList<ManagementRestaurant> restaurantArrayList) {
        this.mContext = mContext;
        this.restaurantArrayList = restaurantArrayList;
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {
        ManagementRestaurant managementRestaurant = restaurantArrayList.get(position);
        holder.txt_restaurnantId.setText(managementRestaurant.getId()+"");
        holder.txt_name_restaurant.setText(managementRestaurant.getRestaurantName());
        holder.txt_username.setText(new StringBuilder(managementRestaurant.getUsername())
                .append(" - ")
                .append(managementRestaurant.getPhone()));
        holder.txt_address.setText(managementRestaurant.getAddress());
        if(managementRestaurant.getStatus()==1){
            holder.btn_comfirm.setText("Đã xác nhận");
            holder.btn_comfirm.setEnabled(false);
        }
        holder.btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean update_permission=restaurantData.updatePermissionNewRestaurant(managementRestaurant.getId(),1);
                if(update_permission==true){
                    Restaurant restaurant = new Restaurant();
                    restaurant.setmName(managementRestaurant.getRestaurantName());
                    restaurant.setPhone(managementRestaurant.getPhone());
                    restaurant.setOpening(managementRestaurant.getOpening());
                    restaurant.setClosing(managementRestaurant.getClosing());
                    restaurant.setAddress(managementRestaurant.getAddress());
                    restaurant.setLat(managementRestaurant.getLat());
                    restaurant.setLng(managementRestaurant.getLng());
                    RestaurantOwner restaurantOwner = new RestaurantOwner();
                    restaurantOwner.setName(managementRestaurant.getUsername());
                    restaurantOwner.setEmail(managementRestaurant.getEmail());
                    restaurantOwner.setPassword(managementRestaurant.getPassword());
                    Log.d("BBB",managementRestaurant.getId()+"");
                    boolean success=restaurantData.createNewRestaurant(restaurant, restaurantOwner);
                    if (success == true) {
                        holder.btn_comfirm.setEnabled(false);
                        holder.btn_comfirm.setText("Đã xác nhận");
                    } else {
                        Common.showToast(mContext,"chưa xác nhận");
                    }
                }else {
                    Common.showToast(mContext,"Chưa xác nhận");
                }


            }
        });
        holder.image_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+ managementRestaurant.getPhone()));
                if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }else {
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.txt_restaurnantId)
       TextView txt_restaurnantId;
       @BindView(R.id.txt_username)
       TextView txt_username;
       @BindView(R.id.txt_address)
       TextView txt_address;
       @BindView(R.id.txt_name_restaurant)
       TextView txt_name_restaurant;
       @BindView(R.id.btn_comfirm)
        Button btn_comfirm;
       @BindView(R.id.image_call)
       ImageView image_call;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}



