package com.hcmunre.apporderfoodserver.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.Database.MenuData;
import com.hcmunre.apporderfoodserver.models.entity.Food;
import com.hcmunre.apporderfoodserver.models.entity.Menu;
import com.hcmunre.apporderfoodserver.models.entity.Restaurant;
import com.hcmunre.apporderfoodserver.views.adapters.MenuAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MenuFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.recyc_detailfood)
    RecyclerView recyc_detailfood;
    ArrayList<Food> products;
    ArrayList menuFoods;
    Dialog dialog;
    @BindView(R.id.action_add_food)
    FloatingActionButton mActionAddFood;
    @BindView(R.id.action_add_category)
    FloatingActionButton mActionAddCate;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.txtRestaurant)
    TextView txtRestaurant;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtCountMenu)
    TextView txtCountMenu;
    MenuAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_food,
                container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        getInforRestaurant();

        return view;
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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyc_detailfood.setLayoutManager(layoutManager);
        recyc_detailfood.setItemAnimator(new DefaultItemAnimator());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Đã lưu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getMenuFood();
    }

    private void openDialogAddFood() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialogAnimation);
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.dialog_add_food, null);
        alertDialog.setView(add_menu);
        final AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        ImageView mClose = dialog.findViewById(R.id.txt_close);
        EditText txtname_food = dialog.findViewById(R.id.txtname_food);
        EditText txtprice = dialog.findViewById(R.id.txtprice);
        EditText txtcategory_food = dialog.findViewById(R.id.txtcategory_food);
        EditText txtdescription = dialog.findViewById(R.id.txtdescription);
        ImageView imageFood = dialog.findViewById(R.id.imageFood);
        Button btnSelectImage = dialog.findViewById(R.id.btnSelectImage);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openDialogAddMenu() {
        Intent intent = getActivity().getIntent();
        final Restaurant restaurant = (Restaurant) intent.getSerializableExtra(Common.KEY_RESTAURANT);
        //
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
        final EditText editCategory = dialog.findViewById(R.id.editCategory);
        Button btnAđdMenu=dialog.findViewById(R.id.btnAđdMenu);
        btnAđdMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuData menuData=new MenuData();
                Menu menu=new Menu();
                menu.setmName(editCategory.getText().toString());
                menu.setRestaurantId(restaurant.getmId());
                if(editCategory.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    int res=menuData.insertMenuRes(menu);
                    if(res>0){
                        Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                        menuAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getActivity(), "Thêm Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void getInforRestaurant() {
        Intent intent = getActivity().getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra(Common.KEY_RESTAURANT);
        txtRestaurant.setText(restaurant.getmName());
        txtAddress.setText(restaurant.getAddress());
        collapsingToolbarLayout.setTitle(restaurant.getmName());
    }

    private void getMenuFood() {
        Intent intent = getActivity().getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra(Common.KEY_RESTAURANT);
        ArrayList<Menu> listMenu;
        FoodData foodData = new FoodData();
        listMenu = foodData.getMenuResFood(restaurant.getmId());
        menuAdapter = new MenuAdapter(getActivity(), listMenu);
        txtCountMenu.setText(menuAdapter.getItemCount() + "");
        recyc_detailfood.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();
    }

    //update và delete menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog();
        } else if (item.getTitle().equals(Common.DELETE)) {
            confirmDeleteDialog();
        }
        return super.onContextItemSelected(item);
    }

    private void confirmDeleteDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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

    private void showUpdateDialog() {
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
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 700);
        ImageView mClose = dialog.findViewById(R.id.txt_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}
