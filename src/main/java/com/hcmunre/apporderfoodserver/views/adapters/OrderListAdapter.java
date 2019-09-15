package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Order;

import java.util.ArrayList;
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Order> orderArrayList;


    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        Order arraylists = orderArrayList.get(position);

        holder.itemname.setText(arraylists.getItemname());
        holder.itemprice.setText(arraylists.getItemprice());
        holder.foodimg1.setImageResource(arraylists.getFoodimg1());


    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodimg1;
        TextView itemname, itemprice,txtcount;
        LinearLayout parentLayout;
        public ViewHolder(View view) {
            super(view);

            itemname = view.findViewById(R.id.itemname);
            itemprice = view.findViewById(R.id.itemprice);
            foodimg1 = view.findViewById(R.id.foodimg1);
            parentLayout=view.findViewById(R.id.parent_layout);
        }
    }

    public OrderListAdapter(Context mainActivityContacts, ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.mContext = mainActivityContacts;
    }

}



