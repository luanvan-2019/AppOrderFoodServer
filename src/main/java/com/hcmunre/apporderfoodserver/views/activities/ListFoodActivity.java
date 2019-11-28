package com.hcmunre.apporderfoodserver.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aldoapps.autoformatedittext.AutoFormatEditText;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
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

public class ListFoodActivity extends AppCompatActivity {
    @BindView(R.id.recyc_listFood)
    RecyclerView recyc_listFood;
    @BindView(R.id.action_add_food)
    FloatingActionButton mActionAddFood;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    EditText txtname_food,txtprice,txtdescription;
    AutoFormatEditText txt_price;
    Button btnSelectImage,btnAddFood;
    ImageView imageFood;
    FoodData foodData=new FoodData();
    Food food;
    byte[] byteArray;
    String encodedImage;
    FoodAdapter adapter;
    private static final int RESULT_LOAD_IMAGE = 1;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
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
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null){
            Bitmap bitmap=null;
            Uri uri=data.getData();
            InputStream imageStream;
            try {
                imageStream=getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(bitmap!=null){
                this.imageFood.setImageBitmap(bitmap);
                try {
                    Bitmap image=((BitmapDrawable)imageFood.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                    byteArray=byteArrayOutputStream.toByteArray();
                    encodedImage= Base64.encodeToString(byteArray,Base64.DEFAULT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
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
        txtname_food=dialog.findViewById(R.id.txtname_food);
        txtprice=dialog.findViewById(R.id.txtprice);
        txtdescription=dialog.findViewById(R.id.txtdescription);
        imageFood=dialog.findViewById(R.id.imageFood);
        btnSelectImage=dialog.findViewById(R.id.btnSelectImage);
        btnAddFood=dialog.findViewById(R.id.btnAddFood);
        btnAddFood.setOnClickListener(view -> {
        //
        food=new Food();
        Intent intent = getIntent();
        Menu menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
        food.setName(txtname_food.getText().toString());
        food.setImageFood(encodedImage);
        food.setDescription(txtdescription.getText().toString());
        food.setPrice(Float.parseFloat(txtprice.getText().toString()));
        food.setMenuId(menu.getmId());
        Observable<Integer> addFood=Observable.just(foodData.insertFood(food));
        compositeDisposable.add(
                addFood
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            if(integer>0){
                                Toast.makeText(ListFoodActivity.this, "Đã thêm ", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(ListFoodActivity.this, "Không thêm được", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> Toast.makeText(ListFoodActivity.this, "Đã Hủy"+throwable.getMessage(), Toast.LENGTH_SHORT).show())
        );
        });
        chooseImage();
        mClose.setOnClickListener(view -> dialog.dismiss());
    }

    private void listFoodOfMenu() {
        Intent intent = getIntent();
        Menu menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
        toolbar.setTitle(menu.getmName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            Observable<ArrayList<Food>> listFoods=Observable.just(foodData.getFoodOfMenu(menu.getmId()));
            compositeDisposable.add(
                    listFoods
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(foods -> {
                            adapter = new FoodAdapter(foods, ListFoodActivity.this);
                            recyc_listFood.setAdapter(adapter);
                    }, throwable -> {
                        Toast.makeText(this, "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    })
            );
        } catch (SQLException e) {
            Toast.makeText(this, "Lỗi "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(item);
        } else if (item.getTitle().equals(Common.DELETE)) {
            confirmDeleteDialog(item);
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        alertDialog.setTitle("Cập nhật món ăn");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_food = layoutInflater.inflate(R.layout.dialog_add_food, null);
        alertDialog.setView(add_food);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        ImageView txt_close = dialog.findViewById(R.id.txt_close);
        txtname_food=dialog.findViewById(R.id.txtname_food);
        txtprice=dialog.findViewById(R.id.txtprice);
        txtdescription=dialog.findViewById(R.id.txtdescription);
        imageFood=dialog.findViewById(R.id.imageFood);
        btnSelectImage=dialog.findViewById(R.id.btnSelectImage);
        btnAddFood=dialog.findViewById(R.id.btnAddFood);
        btnAddFood.setText("Cập nhật");
        btnAddFood.setOnClickListener(view -> {
            Food food=new Food();
            Intent intent = getIntent();
            Menu menu = (Menu) intent.getSerializableExtra(Common.KEY_MENU);
            food.setName(txtname_food.getText().toString());
            food.setImageFood(encodedImage);
            food.setDescription(txtdescription.getText().toString());
            food.setPrice(Float.parseFloat(txtprice.getText().toString()));
            food.setMenuId(menu.getmId());
            adapter.updateFood(item.getOrder(),food);
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
        if(adapter!=null){
            adapter.onStop();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    private void chooseImage(){
        btnSelectImage.setOnClickListener(view -> {
            //Mở gallery và chọn hình trong media
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }else{
                Toast.makeText(ListFoodActivity.this, "Không tìm thấy media", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
