package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.hcmunre.apporderfoodserver.models.Entity.OrderDetail;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<OrderDetail> orderDetails;

    public OrderDetailAdapter(Context mContext, ArrayList<OrderDetail> orderDetails) {
        this.mContext = mContext;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail,null);
        return new OrderDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail orderDetail=orderDetails.get(position);
        holder.txt_name_food.setText(orderDetail.getName());
        holder.txt_quantity.setText(String.valueOf(orderDetail.getQuantity()));
        holder.txt_price.setText(new StringBuffer(holder.currency.format(orderDetail.getPrice())).append("đ"));
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_name_food)
        TextView txt_name_food;
        @BindView(R.id.txt_price)
        TextView txt_price;
        Locale locale=new Locale("vi","VN");
        NumberFormat currency=NumberFormat.getInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
