package com.hcmunre.apporderfoodserver.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.RestaurantData;
import com.hcmunre.apporderfoodserver.models.Entity.Category;
import com.hcmunre.apporderfoodserver.views.adapters.CategoryAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;
    @BindView(R.id.recyc_category)
    RecyclerView recyc_category;
    @BindView(R.id.action_add_category)
    FloatingActionButton action_add_category;
    Button btn_select_image,btn_add;
    ImageView image_cate;
    EditText edit_name;
    byte[] byteArray;
    String encodedImage;
    CategoryAdapter categoryAdapter;
    private static final int RESULT_LOAD_IMAGE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        init();
        eventClick();
    }
    private void init(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyc_category.setLayoutManager(linearLayoutManager);
        recyc_category.setHasFixedSize(true);
        recyc_category.setItemAnimator(new DefaultItemAnimator());
        //
        toolbar.setTitle("Nhóm loại món ăn");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        swipe_layout.setColorSchemeResources(R.color.colorPrimary);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getCategory().execute();
            }
        });
        new getCategory().execute();
    }
    private void eventClick(){
        action_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddCate();
            }
        });
    }
    public class getCategory extends AsyncTask<String, String, ArrayList<Category>> {

        @Override
        protected void onPreExecute() {
            if(!swipe_layout.isRefreshing()){
                swipe_layout.setRefreshing(true);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            if(categories.size()>0){
                categoryAdapter = new CategoryAdapter(CategoryActivity.this, categories);
                recyc_category.setAdapter(categoryAdapter);
                swipe_layout.setRefreshing(false);
            }else {
                Toast.makeText(CategoryActivity.this, "Loại món ăn đang trống", Toast.LENGTH_SHORT).show();
                swipe_layout.setRefreshing(false);
            }

        }
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected ArrayList<Category> doInBackground(String... strings) {
            ArrayList<Category> restaurants ;
            RestaurantData restaurantData = new RestaurantData();
            restaurants = restaurantData.getCategory();
            return restaurants;
        }
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
                image_cate.setImageBitmap(bitmap);
                try {
                    Bitmap image = ((BitmapDrawable) image_cate.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            Toast.makeText(this, "Đã hủy", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            openDialogUpdateCate(item);
        }else if(item.getTitle().equals(Common.DELETE)){
            openDialogDeleteCate(item);
        }
        return super.onContextItemSelected(item);
    }
    private void openDialogDeleteCate(MenuItem item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Bạn có chắc chắn xóa không?");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View confirm_delete = layoutInflater.inflate(R.layout.confirm_delete, null);
        alertDialog.setView(confirm_delete);
        alertDialog.setPositiveButton("Đồng ý", (dialogInterface, i) -> {
            categoryAdapter.deleteCategory(item.getOrder());
            dialogInterface.dismiss();
        });
        alertDialog.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();
    }
    private void openDialogUpdateCate(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.dialog_add_category, null);
        alertDialog.setView(add_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        //
        edit_name = dialog.findViewById(R.id.edit_name_cate);
        image_cate = dialog.findViewById(R.id.image_cate);
        btn_select_image = dialog.findViewById(R.id.btn_select_image);
        btn_add = dialog.findViewById(R.id.btn_add);
        btn_add.setText("Cập nhật");
        ImageView close = dialog.findViewById(R.id.txt_close);
        RelativeLayout view = dialog.findViewById(R.id.main_layout);
        chooseImage();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category=new Category();
                category.setName(edit_name.getText().toString());
                category.setImage(encodedImage);
                categoryAdapter.updateCate(item.getOrder(),category);
                dialog.dismiss();
            }
        });
        close.setOnClickListener(v -> dialog.dismiss());
    }

    private void openDialogAddCate() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomDialogAnimation);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.dialog_add_category, null);
        alertDialog.setView(add_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        //
        EditText edit_name = dialog.findViewById(R.id.edit_name_cate);
        image_cate = dialog.findViewById(R.id.image_cate);
        btn_select_image = dialog.findViewById(R.id.btn_select_image);
        btn_add = dialog.findViewById(R.id.btn_add);
        ImageView close = dialog.findViewById(R.id.txt_close);
        RelativeLayout view = dialog.findViewById(R.id.main_layout);
        chooseImage();
        close.setOnClickListener(v -> dialog.dismiss());
    }
    private void chooseImage() {
        btn_select_image.setOnClickListener(view -> {
            //Mở gallery và chọn hình trong media
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            } else {
                Toast.makeText(this, "Không tìm thấy media", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    private void refresh(){

    }
}
