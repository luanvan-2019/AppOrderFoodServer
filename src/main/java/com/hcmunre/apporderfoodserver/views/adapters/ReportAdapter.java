package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.entity.Report;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private Context mContext;
    ArrayList<Report> reports;

    public ReportAdapter(Context mContext, ArrayList<Report> reports) {
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
        Report report = reports.get(position);
        holder.txt_resname.setText(report.getmResName());
        holder.txt_totalfood.setText(report.getmTotalFood());
        holder.txt_totalmoney.setText(report.getmTotalPrice());
    }

    @Override
    public int getItemCount() {
        return reports.size() > 0 ? reports.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_resname, txt_totalfood, txt_totalmoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_resname = itemView.findViewById(R.id.txt_resname);
            txt_totalfood = itemView.findViewById(R.id.txt_totalfood);
            txt_totalmoney = itemView.findViewById(R.id.txt_totalmoney);

        }
    }
}
