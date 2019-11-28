package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.Interfaces.LoadMore;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.views.activities.OrderDetailShipperActivity;
import com.hcmunre.apporderfoodserver.views.activities.ShipperMapActivity;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Entity.ShipperOrder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipperOrderAdapter extends RecyclerView.Adapter<ShipperOrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ShipperOrder> shipperOrders;
    LoadMore loadMore;
    ShipperData shipperData=new ShipperData();
    OrderData orderData=new OrderData();
    public ShipperOrderAdapter(Context context, ArrayList<ShipperOrder> shipperOrders) {
        this.context = context;
        this.shipperOrders = shipperOrders;
    }

    @NonNull
    @Override
    public ShipperOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shipper_order,null);
        return new ShipperOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipperOrderAdapter.ViewHolder holder, int position) {
        ShipperOrder shipperOrder=shipperOrders.get(position);
        holder.txt_orderId.setText(shipperOrder.getOrderID()+"");
        holder.txt_quantity.setText(shipperOrder.getNumberOfItem()+"");
        holder.txt_total_price.setText(String.valueOf(holder.numberFormat.format(shipperOrder.getTotalPrice())));
        holder.itemView.setOnClickListener(v -> {
            Common.currrentShipperOrder=shipperOrders.get(position);
            context.startActivity(new Intent(context, OrderDetailShipperActivity.class));
        });
        holder.btn_receiv.setOnClickListener(v -> {
            Common.currrentShipperOrder=shipperOrders.get(position);
            boolean success=shipperData.updateStatusShipping(shipperOrder.getOrderID(),2);
            Order order=new Order();
            order.setOrderId(shipperOrder.getOrderID());
            order.setOrderStatus(2);
            boolean success_update_order=orderData.updateOrder(order);
            if(success==true){
                if(success_update_order==true){
                    Common.showToast(context,"Đã nhận");
                }else{
                    Log.d(Common.TAG,"Lỗi");
                }
            }else {
                Log.d(Common.TAG,"Lỗi");
            }
            context.startActivity(new Intent(context, ShipperMapActivity.class));
        });
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order=new Order();
                order.setOrderId(shipperOrder.getOrderID());
                order.setOrderStatus(3);
                boolean success_update_order=orderData.updateOrder(order);
                if(success_update_order==true){
                    Common.showToast(context,"Đã hủy");
                }else{
                    Log.d(Common.TAG,"Lỗi");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shipperOrders.size();
    }
    public void addItem(ArrayList<ShipperOrder> shipperOrder) {
        shipperOrders.addAll(shipperOrder);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_orderId)
        TextView txt_orderId;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_total_price)
        TextView txt_total_price;
        @BindView(R.id.btn_receive)
        TextView btn_receiv;
        @BindView(R.id.btn_cancel)
        TextView btn_cancel;
        Locale locale=new Locale("vi","VN");
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }
}
