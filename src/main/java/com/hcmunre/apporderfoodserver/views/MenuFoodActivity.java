package com.hcmunre.apporderfoodserver.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Food;
import com.hcmunre.apporderfoodserver.models.MenuFoodRestaurant;
import com.hcmunre.apporderfoodserver.views.adapters.ProductAdapter;

import java.util.ArrayList;

public class MenuFoodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Food> products;
    Integer mImage[] = {R.drawable.ic_eat,R.drawable.ic_eat,R.drawable.ic_eat,R.drawable.ic_eat,R.drawable.ic_eat};
    String mName[] = {"Heo", "Boò", "Gà xối mở", "Gà ác hầm thuốc bắc", "Cá lóc kho tộ"};
    String mPrice[] = {"$ 50.00", "$ 45.00", "$ 29.00", "$ 35.00", "$15.00"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_food);
        recyclerView = findViewById(R.id.recyc_detailfood);

        //test
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<MenuFoodRestaurant> companies = new ArrayList<>();
        products = new ArrayList<>();
        for (int i = 0; i < mImage.length; i++) {
            Food item = new Food(mName[i], mPrice[i], mImage[i]);
            products.add(item);
        }
        MenuFoodRestaurant birds = new MenuFoodRestaurant("Món động vật", products);
        companies.add(birds);
        ProductAdapter adapter = new ProductAdapter(companies);
        recyclerView.setAdapter(adapter);

        //test
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Đã lưu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
