package com.hcmunre.apporderfoodserver.views.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.hcmunre.apporderfoodserver.models.Entity.HotFood;
import com.hcmunre.apporderfoodserver.models.Entity.Report;
import com.hcmunre.apporderfoodserver.views.activities.PreferenceUtilsServer;
import com.hcmunre.apporderfoodserver.views.adapters.ReportAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ResReportTabFragment extends Fragment {
    Unbinder unbinder;
    private ArrayList<Report> reports;
    private ReportAdapter reportAdapter;
    private ArrayList<HotFood> hotFoodsList;
    private ArrayList<Food> foods;
    private ArrayList<PieEntry> pieEntries;
    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.recyc_order)
    RecyclerView recyc_order;
    @BindView(R.id.txt_total_quantity)
    TextView txt_total_quantity;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.txt_date_from)
    TextView txt_date_from;
    @BindView(R.id.txt_date_to)
    TextView txt_date_to;
    @BindView(R.id.txt_report)
    TextView txt_report;
    FoodData foodData = new FoodData();
    String date_from,date_to;
    SimpleDateFormat simpleDateFormat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allfood, container, false);
        unbinder= ButterKnife.bind(this,view);
        init();
        eventClick();
        new loadChartReport().execute();
        return view;
    }
    private void init(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyc_order.setLayoutManager(layoutManager);
        recyc_order.setItemAnimator(new DefaultItemAnimator());
    }
    private void eventClick(){
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String date_from_format=simpleDateFormat.format(calendar.getTime());
        txt_date_from.setText(date_from_format);
        txt_date_from.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, (view, year, month, dayOfMonth) -> {
                txt_date_from.setText(new StringBuilder("")
                        .append(dayOfMonth)
                        .append("/")
                        .append(month + 1)
                        .append("/")
                        .append(year));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        String date_to_format=simpleDateFormat.format(calendar.getTime());
        txt_date_to.setText(date_to_format);
        txt_date_to.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, (view, year, month, dayOfMonth) -> {
                txt_date_to.setText(new StringBuilder("")
                        .append(dayOfMonth)
                        .append("/")
                        .append(month+1)
                        .append("/")
                        .append(year));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        txt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date_from=txt_date_from.getText().toString();
                date_to=txt_date_to.getText().toString();
                int day_from=Integer.parseInt(date_from.substring(0,2));
                int day_to=Integer.parseInt(date_to.substring(0,2));
                int month_from=Integer.parseInt(date_from.substring(3,5));
                int month_to=Integer.parseInt(date_to.substring(3,5));
                int year_from=Integer.parseInt(date_from.substring(6,10));
                int year_to=Integer.parseInt(date_to.substring(6,10));
                if(day_to>=day_from && month_to>=month_from&&year_to>=year_from){
                    new loadReportFood(date_from,date_to);
                }else {
                    Common.showToast(getActivity(),"Vui lòng chọn ngày bắt đầu nhỏ hơn");
                }
            }
        });

    }
    public class loadChartReport extends AsyncTask<String, String, ArrayList<HotFood>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<HotFood> hotFoods) {
            int i = 0;
            pieEntries = new ArrayList<>();
            for (HotFood hotFood : hotFoods) {
                pieEntries.add(new PieEntry(Float.parseFloat(String.valueOf(hotFood.getPercent())), hotFood.getName()));
                i++;
            }

            PieDataSet dataSet = new PieDataSet(pieEntries, "Món ăn được đặt nhiều");
            PieData pieData = new PieData();
            pieData.setDataSet(dataSet);
            pieData.setValueTextSize(14f);
            pieData.setValueFormatter(new PercentFormatter(pieChart));
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieChart.setData(pieData);
            pieChart.animateXY(2000, 2000);
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);

        }

        @Override
        protected ArrayList<HotFood> doInBackground(String... strings) {
            hotFoodsList = new ArrayList<>();
            hotFoodsList = foodData.reportHotFood(PreferenceUtilsServer.getUserId(getActivity()));
            return hotFoodsList;
        }
    }
    public class loadReportFood extends AsyncTask<String, String, ArrayList<Food>> {
        String from,to;

        public loadReportFood(String from, String to) {
            this.from = from;
            this.to = to;
            this.execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Food> foods) {
            int total_quantity=0;
            double totalPrice=0.0;
            int i=0;
            Locale locale=new Locale("vi","VN");
            NumberFormat numberFormat=NumberFormat.getInstance(locale);
            for (Food food:foods) {
                total_quantity=total_quantity+food.getQuantity();
                totalPrice=totalPrice+(food.getQuantity()*food.getPrice());
                i++;

            }
            txt_total_quantity.setText(String.valueOf(total_quantity));
            txt_total_price.setText(new StringBuilder(numberFormat.format(totalPrice)).append(" đ"));
            reportAdapter = new ReportAdapter(getActivity(), foods);
            recyc_order.setAdapter(reportAdapter);

        }

        @Override
        protected ArrayList<Food> doInBackground(String... strings) {
            foods = new ArrayList<>();
            foods = foodData.reportAllFood(PreferenceUtilsServer.getUserId(getActivity()),from,to);
            return foods;
        }
    }

}
