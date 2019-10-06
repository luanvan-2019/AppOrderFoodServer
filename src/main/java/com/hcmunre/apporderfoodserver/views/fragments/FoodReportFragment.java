package com.hcmunre.apporderfoodserver.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.entity.Report;
import com.hcmunre.apporderfoodserver.views.adapters.ReportAdapter;

import java.util.ArrayList;

public class FoodReportFragment extends Fragment {
    String mResName[] = {"Tý", "Sửu", "Dần", "Mẹo", "Thìn"};
    String mTotalFood[] = {"10", "20", "30", "40", "50"};
    String mTotalPrice[] = {"100.000đ", "200.000đ", "300.000đ", "400.000đ", "500.000đ"};

    private ArrayList<Report> reports;
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allfood, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        reports = new ArrayList<>();
        for (int i = 0; i < mResName.length; i++) {
            Report item = new Report(mResName[i], mTotalFood[i], mTotalPrice[i]);
            reports.add(item);
        }
        reportAdapter = new ReportAdapter(getActivity(), reports);
        recyclerView.setAdapter(reportAdapter);

        return view;
    }
}
