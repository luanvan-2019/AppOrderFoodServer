package com.hcmunre.apporderfoodserver.views.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.Entity.HotFood;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarChartActivity extends AppCompatActivity {
    private ArrayList<HotFood> hotFoodsList;
    private ArrayList<PieEntry> pieEntries;
    @BindView(R.id.piechart)
    PieChart pieChart;
    FoodData foodData = new FoodData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        ButterKnife.bind(this);
        new loadChart().execute();
    }

    public class loadChart extends AsyncTask<String, String, ArrayList<HotFood>> {
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
            hotFoodsList = foodData.reportHotFood(Common.currentRestaurant.getmId());
            return hotFoodsList;
        }
    }
}
