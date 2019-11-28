package com.hcmunre.apporderfoodserver.models.Entity;

public class Report {
    private String mResName;
    private String mTotalFood;
    private String mTotalPrice;

    public Report(String mResName, String mTotalFood, String mTotalPrice) {
        this.mResName = mResName;
        this.mTotalFood = mTotalFood;
        this.mTotalPrice = mTotalPrice;
    }

    public String getmResName() {
        return mResName;
    }

    public void setmResName(String mResName) {
        this.mResName = mResName;
    }

    public String getmTotalFood() {
        return mTotalFood;
    }

    public void setmTotalFood(String mTotalFood) {
        this.mTotalFood = mTotalFood;
    }

    public String getmTotalPrice() {
        return mTotalPrice;
    }

    public void setmTotalPrice(String mTotalPrice) {
        this.mTotalPrice = mTotalPrice;
    }
}
