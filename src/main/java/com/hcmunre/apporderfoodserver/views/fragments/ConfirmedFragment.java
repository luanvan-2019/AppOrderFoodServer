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
import com.hcmunre.apporderfoodserver.views.adapters.OrderListAdapter;

import java.util.ArrayList;

public class ConfirmedFragment extends Fragment {



            Integer foodimg1[] = {R.drawable.ic_eat,R.drawable.ic_eat,R.drawable.ic_eat,R.drawable.ic_eat,R.drawable.ic_eat};
            String itemname[] = {"Chân gà ngâm xả ớt", "Bún thịt nướng", "Bún chả cua", "Bloody Mary Meatballs", "Halloumi & Pepper Skewers"};
            String itemprice[] = {"$ 50.00", "$ 45.00", "$ 29.00", "$ 35.00", "$15.00"};

            private ArrayList<Order> orderArrayList;
            private RecyclerView recyclerView;
            private OrderListAdapter orderListAdapter;


            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_allfood, container, false);

                recyclerView=view.findViewById(R.id.recyclerview);
//                sliderLayout=view.findViewById(R.id.imageSlider);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                orderArrayList =new ArrayList<>();
                for (int i = 0; i < foodimg1.length; i++) {
                    Order item = new Order(foodimg1[i],itemname[i],itemprice[i]);
                    orderArrayList.add(item);
                }
                orderListAdapter =new OrderListAdapter(getActivity(), orderArrayList);
                    recyclerView.setAdapter(orderListAdapter);
                    return view;
        }

}
