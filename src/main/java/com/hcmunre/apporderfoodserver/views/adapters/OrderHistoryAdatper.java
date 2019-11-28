package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.Interfaces.LoadMore;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.views.activities.OrderDetailActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryAdatper extends RecyclerView.Adapter<OrderHistoryAdatper.ViewHolder> {
    private Context context;
    private ArrayList<Order> orders;
    SimpleDateFormat simpleDateFormat;
    LoadMore loadMore;
    OrderData orderData=new OrderData();
    public OrderHistoryAdatper(Context context, ArrayList<Order> listOrderHistory) {
        this.context = context;
        this.orders = listOrderHistory;
        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
    }
    public void setData(ArrayList<Order> listOrderHistory) {
        this.orders.clear();
        listOrderHistory.addAll(listOrderHistory);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderHistoryAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history,null);
        return new OrderHistoryAdatper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdatper.ViewHolder holder, int position) {
        Order order=orders.get(position);
        holder.txt_orderId.setText(order.getOrderId()+"");
        holder.txt_quantity.setText(order.getNumberOfItem()+"");
        holder.txt_total_price.setText(String.valueOf(holder.numberFormat.format(order.getTotalPrice())));
        holder.txt_name_customer.setText(order.getOrderName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OrderDetailActivity.class));
            }
        });
    }
    public void itemRemoved(int position) {
        orders.remove(position);
        notifyItemRemoved(position);

    }
    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void addItem(ArrayList<Order> orderArrayList) {
        orders.addAll(orderArrayList);
        notifyDataSetChanged();
    }
    public void refresh(ArrayList<Order> itemsw) {
        this.orders = itemsw;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_orderId)
        TextView txt_orderId;
        @BindView(R.id.txt_name_customer)
        TextView txt_name_customer;
        @BindView(R.id.txt_quantity)
        TextView txt_quantity;
        @BindView(R.id.txt_total_price)
        TextView txt_total_price;
        @BindView(R.id.btn_confirm)
        TextView btn_confirm;
        @BindView(R.id.btn_cancel)
        TextView btn_cancel;
        Locale locale=new Locale("vi","VN");
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            btn_confirm.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        }
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }
    public class updateOrder extends AsyncTask<String,String,Boolean>{
        private int orderStatus;

        public updateOrder(int orderStatus) {
            this.orderStatus = orderStatus;
            this.execute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean==true){
                Log.d(Common.TAG,"Đã thực hiện");
            }
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Order order=new Order();
            order.setOrderId(Common.currentOrder.getOrderId());
            order.setOrderStatus(orderStatus);
            boolean success=orderData.updateOrder(order);
            return success;
        }
    }
}
