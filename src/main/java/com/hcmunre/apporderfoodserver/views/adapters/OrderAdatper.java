package com.hcmunre.apporderfoodserver.views.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.Interfaces.LoadMore;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.MenuData;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.views.activities.OrderDetailActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdatper extends RecyclerView.Adapter<OrderAdatper.ViewHolder> {
    private Context context;
    private ArrayList<Order> orders;
    SimpleDateFormat simpleDateFormat;
    LoadMore loadMore;
    OrderData orderData=new OrderData();
    public OrderAdatper(Context context, ArrayList<Order> listOrderHistory) {
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
    public OrderAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history,null);
        return new OrderAdatper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdatper.ViewHolder holder, int position) {
        Order order=orders.get(position);
        holder.txt_orderId.setText(order.getOrderId()+"");
        holder.txt_quantity.setText(new StringBuilder(order.getNumberOfItem()+"").append(" phần x "));
        holder.txt_total_price.setText(new StringBuilder(holder.numberFormat.format(order.getTotalPrice())).append("đ"));
        holder.txt_name_customer.setText(order.getOrderName());
        holder.btn_confirm.setOnClickListener(v -> {
            Common.currentOrder=orders.get(position);
            new updateOrder(1);
            itemRemoved(position);
            context.startActivity(new Intent(context, OrderDetailActivity.class));
        });
        holder.btn_cancel.setOnClickListener(view -> {
            Common.currentOrder=orders.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                    .setTitle("Hủy đơn")
                    .setMessage("Bạn có muốn hủy đơn không ?")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new updateOrder(5);
                            itemRemoved(position);
                            holder.btn_cancel.setBackgroundResource(R.drawable.round_button_light);
                            Common.showToast(context,"Đã hủy");
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.currentOrder=orders.get(position);
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
        NumberFormat numberFormat=NumberFormat.getInstance(locale);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
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
                Common.showToast(context,"Đã nhận");
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
