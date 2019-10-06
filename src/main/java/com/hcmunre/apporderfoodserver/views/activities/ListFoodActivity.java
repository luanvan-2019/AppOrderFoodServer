package com.hcmunre.apporderfoodserver.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.entity.Food;
import com.hcmunre.apporderfoodserver.models.entity.Menu;
import com.hcmunre.apporderfoodserver.views.adapters.FoodAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFoodActivity extends AppCompatActivity {
    @BindView(R.id.recyc_listFood)
    RecyclerView recyclerView;
    @BindView(R.id.txtNameMenuRes)
    TextView txtNameMenuRes;
    @BindView(R.id.action_add_food)
    FloatingActionButton mActionAddFood;
    @BindView(R.id.action_add_category)
    FloatingActionButton mActionAddCate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfood);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        mActionAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddFood();
            }
        });
        mActionAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddMenu();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listFoodOfMenu();
        getIntentMenu();
    }

    private void openDialogAddFood() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.dialog_add_food, null);
        alertDialog.setView(add_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        ImageView mClose = dialog.findViewById(R.id.txt_close);
        EditText txtname_food=dialog.findViewById(R.id.txtname_food);
        EditText txtprice=dialog.findViewById(R.id.txtprice);
        EditText txtcategory_food=dialog.findViewById(R.id.txtcategory_food);
        EditText txtdescription=dialog.findViewById(R.id.txtdescription);
        ImageView imageFood=dialog.findViewById(R.id.imageFood);
        Button btnSelectImage=dialog.findViewById(R.id.btnSelectImage);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openDialogAddMenu() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.dialog_add_menu, null);
        alertDialog.setView(add_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 700);
        ImageView mClose = dialog.findViewById(R.id.txt_close);
        EditText editCategory=dialog.findViewById(R.id.editCategory);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void listFoodOfMenu() {
        Intent intent = getIntent();
        Menu menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
        ArrayList<Food> foods = new ArrayList<>();
        FoodData foodData = new FoodData();
        try {
            foods = foodData.getFoodOfMenu(menu.getmId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FoodAdapter adapter = new FoodAdapter(foods, ListFoodActivity.this);
        recyclerView.setAdapter(adapter);

    }

    private void getIntentMenu() {
        int id = 0;
        Intent intent = getIntent();
        Menu menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
        txtNameMenuRes.setText(menu.getmName());
        id = menu.getmId();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog();
        } else if (item.getTitle().equals(Common.DELETE)) {
            confirmDeleteDialog();
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        alertDialog.setTitle("Cập nhật món ăn");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_food = layoutInflater.inflate(R.layout.dialog_add_food, null);
        alertDialog.setView(add_food);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        ImageView txt_close = dialog.findViewById(R.id.txt_close);
        txt_close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void confirmDeleteDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Bạn có chắc chắn xóa không?");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View confirm_delete = layoutInflater.inflate(R.layout.confirm_delete, null);
        alertDialog.setView(confirm_delete);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
