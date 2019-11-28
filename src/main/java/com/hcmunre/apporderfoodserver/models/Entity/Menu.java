package com.hcmunre.apporderfoodserver.models.Entity;

import java.io.Serializable;

public class Menu implements Serializable {
    private int mId;
    private String mName,image;
    private int restaurantId;

    public Menu() {
    }

    public Menu(int mId, String mName, int restaurantId) {
        this.mId = mId;
        this.mName = mName;
        this.restaurantId = restaurantId;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
