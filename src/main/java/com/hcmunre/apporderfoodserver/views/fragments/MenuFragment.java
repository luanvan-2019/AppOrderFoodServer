package com.hcmunre.apporderfoodserver.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.commons.Progress;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.Database.MenuData;
import com.hcmunre.apporderfoodserver.models.Database.RestaurantData;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.hcmunre.apporderfoodserver.models.Entity.Menu;
import com.hcmunre.apporderfoodserver.models.Entity.MenuFood;
import com.hcmunre.apporderfoodserver.models.Entity.MenuFoodRestaurant;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.views.activities.PreferenceUtilsServer;
import com.hcmunre.apporderfoodserver.views.adapters.MenuAdapter;
import com.hcmunre.apporderfoodserver.views.adapters.ProductAdapter;

import net.sourceforge.jtds.jdbc.UniqueIdentifier;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class MenuFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.recyc_detailfood)
    RecyclerView recyc_detailfood;
    @BindView(R.id.action_add_food)
    FloatingActionButton mActionAddFood;
    @BindView(R.id.action_add_category)
    FloatingActionButton mActionAddCate;
    @BindView(R.id.txtRestaurant)
    TextView txtRestaurant;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtCountMenu)
    TextView txtCountMenu;
    @BindView(R.id.img_restaurant)
    ImageView img_restaurant;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    byte[] byteArray;
    String encodedImage;
    MenuAdapter menuAdapter;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    RestaurantData restaurantData=new RestaurantData();
    Restaurant restaurant;
    FoodData foodData = new FoodData();
    ImageView image_menu;
    private static final int RESULT_LOAD_IMAGE = 2;
    Progress progress=new Progress();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_food,
                container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        eventClick();
        new getMenuFood().execute();
        getInforRestaurant();
        return view;
    }

    private void init() {
        mActionAddCate.setOnClickListener(view -> openDialogAddMenu());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyc_detailfood.setLayoutManager(gridLayoutManager);
        recyc_detailfood.setItemAnimator(new DefaultItemAnimator());

    }
    private void eventClick(){
        img_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });
    }
    private void openDialogAddMenu() {
        Intent intent = getActivity().getIntent();
        final Restaurant restaurant = (Restaurant) intent.getSerializableExtra(Common.KEY_RESTAURANT);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialogAnimation);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.dialog_add_menu, null);
        alertDialog.setView(add_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 700);
        ImageView mClose = dialog.findViewById(R.id.txt_close);
        EditText editCategory = dialog.findViewById(R.id.editCategory);
        Button btnAddMenu=dialog.findViewById(R.id.btnAđdMenu);
        btnAddMenu.setOnClickListener(view -> {
            MenuData menuData=new MenuData();
            Menu menu=new Menu();
            menu.setmName(editCategory.getText().toString());
            menu.setRestaurantId(restaurant.getmId());
            if(editCategory.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
            }else {
                Observable<Integer> addMenu=Observable.just(menuData.insertMenuRes(menu));
                compositeDisposable.add(addMenu
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            if (integer > 0) {
                                Toast.makeText(getActivity(), "Đã thêm", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Không thêm được", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(getActivity(), "Lỗi "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));
            }

        });



        mClose.setOnClickListener(view -> dialog.dismiss());
    }

    private void getInforRestaurant() {
        txtRestaurant.setText(PreferenceUtilsServer.getRestaurantName(getActivity()));
        txtAddress.setText(PreferenceUtilsServer.getRestaurantAddress(getActivity()));
    }

    public class getMenuFood extends AsyncTask<String,String,ArrayList<Menu>>{

    @Override
        protected void onPreExecute() {
            progress.showProgress(getActivity());
        }
        @Override
        protected void onPostExecute(ArrayList<Menu> menus) {
            menuAdapter = new MenuAdapter(getActivity(), menus);
            txtCountMenu.setText(menuAdapter.getItemCount() + "");
            recyc_detailfood.setAdapter(menuAdapter);
            menuAdapter.notifyDataSetChanged();
            progress.hideProgress();
        }

        @Override
        protected ArrayList<Menu> doInBackground(String... strings) {
            ArrayList<Menu> menus;
            menus = foodData.getMenuResFood(PreferenceUtilsServer.getUserId(getActivity()));
            return menus;
        }
    }

    //update và delete menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(item);
        } else if (item.getTitle().equals(Common.DELETE)) {
            confirmDeleteDialog(item);
        }
        return super.onContextItemSelected(item);
    }

    private void confirmDeleteDialog(final MenuItem item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Bạn có chắc chắn xóa không?");
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View confirm_delete = layoutInflater.inflate(R.layout.confirm_delete, null);
        alertDialog.setView(confirm_delete);
        alertDialog.setPositiveButton("OK", (dialogInterface, i) -> {
            menuAdapter.itemRemoved(item.getOrder());
            dialogInterface.dismiss();
        });
        alertDialog.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            getActivity().finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==getActivity().RESULT_OK && data!=null){
            Bitmap bitmap=null;
            Uri uri=data.getData();
            InputStream imageStream;
            try {
                imageStream=getActivity().getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(bitmap!=null){
                image_menu.setImageBitmap(bitmap);
                try {
                    Bitmap image=((BitmapDrawable)image_menu.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                    byteArray=byteArrayOutputStream.toByteArray();
                    encodedImage= Base64.encodeToString(byteArray,Base64.DEFAULT);
                    updateRestauratant(encodedImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }
    private void showUpdateDialog(final MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialogAnimation);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View update_menu = layoutInflater.inflate(R.layout.dialog_add_menu, null);
        alertDialog.setView(update_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Cập nhật thực đơn");
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 900);
        ImageView mClose = dialog.findViewById(R.id.txt_close);
        EditText editCategory = dialog.findViewById(R.id.editCategory);
        Button btn_select_image=dialog.findViewById(R.id.btnSelectImage);
        Button btnAddMenu=dialog.findViewById(R.id.btnAđdMenu);
        image_menu=dialog.findViewById(R.id.image_menu);
        btnAddMenu.setText("Cập nhật");
        btnAddMenu.setOnClickListener(view -> {
            Menu menu=new Menu();
            menu.setmName(editCategory.getText().toString());
            menu.setImage(encodedImage);
            menu.setRestaurantId(PreferenceUtilsServer.getUserId(getContext()));
            menuAdapter.itemUpdate(item.getOrder(),menu);
            dialog.dismiss();
        });
        btn_select_image.setOnClickListener(v -> chooseImage());
        mClose.setOnClickListener(view -> dialog.dismiss());

    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void updateRestauratant(String image) {
        restaurant=new Restaurant();
        restaurant.setmId(Common.currentRestaurant.getmId());
        restaurant.setImage(image);
        progressBar.setVisibility(View.VISIBLE);
        Observable<Boolean> updateImage=Observable.just(restaurantData.updateImage(restaurant));
        compositeDisposable.add(
                updateImage
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if(aBoolean==true){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Đã thay đổi ảnh " +Common.currentRestaurant.getmId(), Toast.LENGTH_SHORT).show();
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Không thể thay đổi ảnh "+Common.currentRestaurant.getmId(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    progressBar.setVisibility(View.GONE);
                    Log.d(Common.TAG,"Lỗi hệ thống"+throwable.getMessage());
                })
        );
    }
    private void chooseImage(){
            //Mở gallery và chọn hình trong media
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }else{
                Toast.makeText(getActivity(), "Không tìm thấy media", Toast.LENGTH_SHORT).show();
            }
    }

}
