package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Entity.Food;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<Food> reports;

    public ReportAdapter(Context mContext, ArrayList<Food> reports) {
        this.mContext = mContext;
        this.reports = reports;
    }

    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reports, parent, false);
        return new ReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        Food report = reports.get(position);
        holder.txt_name_food.setText(report.getName());
        holder.txt_total_quantity.setText(String.valueOf(report.getQuantity()));
        Integer totalPrice=report.getQuantity()*report.getPrice();
        Locale locale=new Locale("vi","VN");
        NumberFormat numberFormat=NumberFormat.getInstance(locale);
        holder.txt_total_price.setText(new StringBuilder(numberFormat.format(totalPrice)).append(" đ"));
    }

    @Override
    public int getItemCount() {
        return reports.size() > 0 ? reports.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name_food)
        TextView txt_name_food;
        @BindView(R.id.txt_total_quantity)
        TextView txt_total_quantity;
        @BindView(R.id.txt_total_price)
        TextView txt_total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
