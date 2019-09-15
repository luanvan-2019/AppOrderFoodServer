package com.hcmunre.apporderfoodserver.models;
public class RestaurantModel {

    private int mId;
    String mName,mPrice;

    public RestaurantModel(int mId, String mName, String mPrice) {
        this.mId = mId;
        this.mName = mName;
        this.mPrice = mPrice;
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

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }
}
