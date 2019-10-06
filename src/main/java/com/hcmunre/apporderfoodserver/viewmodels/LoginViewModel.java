package com.hcmunre.apporderfoodserver.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hcmunre.apporderfoodserver.models.entity.RestaurantOwner;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<RestaurantOwner> mLogin=new MutableLiveData<>();
    private Context context;
    private RestaurantOwner restaurantOwner;
    public LoginViewModel(Context context, RestaurantOwner restaurantOwner) {
        this.context=context;
        this.restaurantOwner = restaurantOwner;
    }
}
