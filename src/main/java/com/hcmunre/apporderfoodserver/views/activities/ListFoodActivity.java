package com.hcmunre.apporderfoodserver.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.commons.Progress;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.hcmunre.apporderfoodserver.models.Entity.Menu;
import com.hcmunre.apporderfoodserver.views.adapters.FoodAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;

public class ListFoodActivity extends AppCompatActivity {
    @BindView(R.id.recyc_listFood)
    RecyclerView recyc_listFood;
    @BindView(R.id.action_add_food)
    FloatingActionButton mActionAddFood;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    EditText txtname_food, txtdescription;
    CurrencyEditText txtprice;
    Button btnSelectImage, btnAddFood;
    ImageView imageFood;
    FoodData foodData = new FoodData();
    Food food;
    byte[] byteArray;
    String encodedImage;
    FoodAdapter adapter;
    SwitchCompat switch_status;
    private static final int RESULT_LOAD_IMAGE = 1;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Menu menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfood);
        ButterKnife.bind(this);
        init();

    }

    private void init() {

        mActionAddFood.setOnClickListener(view -> openDialogAddFood());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyc_listFood.setLayoutManager(layoutManager);
        recyc_listFood.setHasFixedSize(true);
        recyc_listFood.setItemAnimator(new DefaultItemAnimator());
        listFoodOfMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = null;
            Uri uri = data.getData();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                this.imageFood.setImageBitmap(bitmap);
                try {
                    Bitmap image = ((BitmapDrawable) imageFood.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "Đã hủy", Toast.LENGTH_SHORT).show();
        }
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
        txtname_food = dialog.findViewById(R.id.txtname_food);
        txtprice = dialog.findViewById(R.id.txtprice);
        txtdescription = dialog.findViewById(R.id.txtdescription);
        imageFood = dialog.findViewById(R.id.imageFood);
        switch_status = dialog.findViewById(R.id.switch_status);
        btnSelectImage = dialog.findViewById(R.id.btnSelectImage);
        btnAddFood = dialog.findViewById(R.id.btnAddFood);
        txtprice.setCurrency(CurrencySymbols.NONE);
        txtprice.setDelimiter(false);
        txtprice.setSpacing(false);
        txtprice.setDecimals(false);
        txtprice.setSeparator(",");
        btnAddFood.setOnClickListener(view -> {
            //
            food = new Food();
            Intent intent = getIntent();
            Menu menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
            food.setName(txtname_food.getText().toString());
            food.setImageFood(encodedImage);
            food.setDescription(txtdescription.getText().toString());
            food.setPrice(Integer.parseInt(txtprice.getText().toString().replaceAll("[^0-9]", "")));
            food.setMenuId(menu.getmId());
            if (switch_status.isChecked()) {
                food.setStatusFood(1);
            } else {
                food.setStatusFood(0);
            }
            int success = foodData.insertFood(food);
            if (success > 0) {
                Toast.makeText(ListFoodActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(ListFoodActivity.this, "Không thêm được", Toast.LENGTH_SHORT).show();
            }

        });
        chooseImage();
        mClose.setOnClickListener(view -> dialog.dismiss());
    }

    private void listFoodOfMenu() {
        Intent intent = getIntent();
        menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
        toolbar.setTitle(menu.getmName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new getListFood().execute();
    }

    public class getListFood extends AsyncTask<String, String, ArrayList<Food>> {
        Progress progress = new Progress();

        @Override
        protected void onPreExecute() {
            progress.showProgress(ListFoodActivity.this);
        }

        @Override
        protected void onPostExecute(ArrayList<Food> foods) {
            if (foods != null) {
                adapter = new FoodAdapter(foods, ListFoodActivity.this);
                recyc_listFood.setAdapter(adapter);
                progress.hideProgress();
            }

        }

        @Override
        protected ArrayList<Food> doInBackground(String... strings) {
            ArrayList<Food> foods = null;
            try {
                foods = foodData.getFoodOfMenu(menu.getmId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return foods;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            Log.d("BBB", item.getMenuInfo() + "");
            showUpdateDialog(item);
        } else if (item.getTitle().equals(Common.DELETE)) {
            confirmDeleteDialog(item);
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_food = layoutInflater.inflate(R.layout.dialog_add_food, null);
        alertDialog.setView(add_food);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        //findView
        ImageView txt_close = dialog.findViewById(R.id.txt_close);
        txtname_food = dialog.findViewById(R.id.txtname_food);
        txtprice = dialog.findViewById(R.id.txtprice);
        switch_status = dialog.findViewById(R.id.switch_status);
        txtdescription = dialog.findViewById(R.id.txtdescription);
        imageFood = dialog.findViewById(R.id.imageFood);
        txtprice.setCurrency(CurrencySymbols.NONE);
        txtprice.setDelimiter(false);
        txtprice.setSpacing(false);
        txtprice.setDecimals(false);
        txtprice.setSeparator(",");
        //getItem from recyclerview food
        Food get_food = adapter.getItem(item.getOrder());
        txtname_food.setText(get_food.getName());
        txtdescription.setText(get_food.getDescription());
        txtprice.setText(get_food.getPrice()+"");
        if (get_food.getStatusFood() == 1) {
            switch_status.setChecked(true);
        } else if (get_food.getStatusFood() == 0) {
            switch_status.setChecked(false);
        }
        if (get_food.getImageFood()!=null){
            imageFood.setImageBitmap(Common.getBitmap(get_food.getImageFood()));

        }
        //
        btnSelectImage = dialog.findViewById(R.id.btnSelectImage);
        btnAddFood = dialog.findViewById(R.id.btnAddFood);
        btnAddFood.setText("Cập nhật");
        btnAddFood.setOnClickListener(view -> {
            Intent intent = getIntent();
            Menu menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
            Food food = new Food();
            food.setName(txtname_food.getText().toString());
            if(encodedImage!=null){
                food.setImageFood(encodedImage);
            }else {
                food.setImageFood(get_food.getImageFood());
            }
            food.setDescription(txtdescription.getText().toString());
            food.setPrice(Integer.parseInt(txtprice.getText().toString().replaceAll("[^0-9]", "")));
            food.setMenuId(menu.getmId());
            if(switch_status.isChecked()){
                food.setStatusFood(1);
            }else {
                food.setStatusFood(0);
            }
            adapter.updateFood(item.getOrder(), food);
            adapter.notifyItemChanged(item.getOrder());
            dialog.dismiss();
        });
        txt_close.setOnClickListener(view -> dialog.dismiss());
        chooseImage();
    }

    private void confirmDeleteDialog(MenuItem item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Bạn có chắc chắn xóa không?");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View confirm_delete = layoutInflater.inflate(R.layout.confirm_delete, null);
        alertDialog.setView(confirm_delete);
        alertDialog.setPositiveButton("Đồng ý", (dialogInterface, i) -> {
            adapter.removeFood(item.getOrder());
            dialogInterface.dismiss();
        });
        alertDialog.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        if (adapter != null) {
            adapter.onStop();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void chooseImage() {
        btnSelectImage.setOnClickListener(view -> {
            //Mở gallery và chọn hình trong media
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            } else {
                Toast.makeText(ListFoodActivity.this, "Không tìm thấy media", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
