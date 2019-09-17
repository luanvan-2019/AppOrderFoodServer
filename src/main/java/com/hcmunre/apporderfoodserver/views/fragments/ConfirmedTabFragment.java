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
import com.hcmunre.apporderfoodserver.models.Order;
import com.hcmunre.apporderfoodserver.models.RestaurantModel;
import com.hcmunre.apporderfoodserver.views.adapters.RestaurantAdapter;

import java.util.ArrayList;

public class ConfirmedTabFragment extends Fragment {


    Integer mImage[] = {R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat};
    String mName[] = {"Chân gà ngâm xả ớt", "Bún thịt nướng", "Bún chả cua", "Bloody Mary Meatballs", "Halloumi & Pepper Skewers"};
    String mPrice[] = {"$ 50.00", "$ 45.00", "$ 29.00", "$ 35.00", "$15.00"};

    private ArrayList<RestaurantModel> restaurantModelArrayList;
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

        restaurantModelArrayList = new ArrayList<>();
        for (int i = 0; i < mImage.length; i++) {
            RestaurantModel item = new RestaurantModel(mImage[i], mName[i], mPrice[i]);
            restaurantModelArrayList.add(item);
        }
        restaurantAdapter = new RestaurantAdapter(getActivity(), restaurantModelArrayList);
        recyclerView.setAdapter(restaurantAdapter);
        return view;
    }

}
