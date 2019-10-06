package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.entity.Restaurant;
import com.hcmunre.apporderfoodserver.views.OrderDetailActivity;
import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Restaurant> restaurantArrayList;


    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {
        Restaurant arraylists = restaurantArrayList.get(position);

        holder.mName.setText(arraylists.getmName());
        holder.mPrice.setText(arraylists.getmPrice());
        holder.mId.setText(String.valueOf(arraylists.getmId()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, OrderDetailActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mId, mPrice,mName;
        LinearLayout parentLayout;
        public ViewHolder(View view) {
            super(view);

            mId = view.findViewById(R.id.txtid_order);
            mPrice = view.findViewById(R.id.txt_price);
            mName = view.findViewById(R.id.txt_name);
            parentLayout=view.findViewById(R.id.parent_layout);
        }
    }

    public RestaurantAdapter(Context mainActivityContacts, ArrayList<Restaurant> restaurantArrayList) {
        this.restaurantArrayList = restaurantArrayList;
        this.mContext = mainActivityContacts;
    }

}



