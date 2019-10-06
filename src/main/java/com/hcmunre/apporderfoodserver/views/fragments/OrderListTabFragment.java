package com.hcmunre.apporderfoodserver.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.entity.Restaurant;
import com.hcmunre.apporderfoodserver.views.adapters.RestaurantAdapter;

import java.util.ArrayList;

public class OrderListTabFragment extends Fragment {


    Integer mId[] = {123456, 123456, 123456, 123456, 123456};
    String mName[] = {"Nguyễn Huu Trọng", "Nguyễn văn mạnh", "Hồ việt Trung", "Châu khải Phong", "Jack"};
    String mPrice[] = {"50.000đ", "45.000đ", "29.000đ", "35.000đ", "15.000đ"};

    private ArrayList<Restaurant> restaurantArrayList;
    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allfood, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        restaurantArrayList = new ArrayList<>();
        for (int i = 0; i < mId.length; i++) {
            Restaurant item = new Restaurant(mId[i], mName[i], mPrice[i]);
            restaurantArrayList.add(item);
        }
        restaurantAdapter = new RestaurantAdapter(getActivity(), restaurantArrayList);
        recyclerView.setAdapter(restaurantAdapter);

        return view;


    }
}

