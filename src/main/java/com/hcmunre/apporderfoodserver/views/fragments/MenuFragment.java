package com.hcmunre.apporderfoodserver.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
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

        dialog = new Dialog(getActivity(), R.style.CustomDialogAnimation);
        dialog.setContentView(R.layout.dialog_add_food);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        //
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        //
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        ImageView mClose = dialog.findViewById(R.id.txt_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openDialogAddMenu() {

        dialog = new Dialog(getActivity(), R.style.CustomDialogAnimation);
        dialog.setContentView(R.layout.dialog_add_menu);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.65);
        //
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;
        layoutParams.gravity = Gravity.BOTTOM;
        //
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        ImageView mClose = dialog.findViewById(R.id.txt_close);
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
}
