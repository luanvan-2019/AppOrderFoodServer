package com.hcmunre.apporderfoodserver.views.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Food;
import com.hcmunre.apporderfoodserver.models.MenuFoodRestaurant;
import com.hcmunre.apporderfoodserver.views.adapters.ProductAdapter;

import java.util.ArrayList;


public class MenuFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Food> products;
    Dialog dialog;
    FloatingActionButton mActionAddFood, mActionAddCate;
    Integer mImage[] = {R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat};
    String mName[] = {"Heo", "Boò", "Gà xối mở", "Gà ác hầm thuốc bắc", "Cá lóc kho tộ"};
    String mPrice[] = {"$ 50.00", "$ 45.00", "$ 29.00", "$ 35.00", "$15.00"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_food,
                container, false);
        recyclerView = view.findViewById(R.id.recyc_detailfood);
        mActionAddFood = view.findViewById(R.id.action_add_food);
        mActionAddCate = view.findViewById(R.id.action_add_category);
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
        //test

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<MenuFoodRestaurant> companies = new ArrayList<>();
        products = new ArrayList<>();
        for (int i = 0; i < mImage.length; i++) {
            Food item = new Food(mName[i], mPrice[i], mImage[i]);
            products.add(item);
        }
        MenuFoodRestaurant birds = new MenuFoodRestaurant("Món động vật", products);
        companies.add(birds);
        final ProductAdapter adapter = new ProductAdapter(companies);
        //luôn luôn show item trong menu
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        adapter.expandAllGroups();
                    }
                }
        );
        recyclerView.setAdapter(adapter);

        //test
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Đã lưu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        txtAdd=view.findViewById(R.id.txtadd);
//        txtCategory=view.findViewById(R.id.txtCategory);
//        tabLayout=view.findViewById(R.id.tab_order);
//        viewPager=view.findViewById(R.id.viewpager_order);
//        txtAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                txtAdd.setBackgroundResource(R.drawable.round_button);
//                txtCategory.setBackgroundResource(R.drawable.round_button_light);
//            }
//        });
//        txtCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                txtCategory.setBackgroundResource(R.drawable.round_button);
//                txtAdd.setBackgroundResource(R.drawable.round_button_light);
//            }
//        });
//        listOrder();
        return view;
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
}
