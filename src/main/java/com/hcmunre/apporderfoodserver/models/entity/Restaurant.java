package com.hcmunre.apporderfoodserver.models.entity;

import java.io.Serializable;
import java.sql.Time;

public class Restaurant implements Serializable {

    private int mId;
    private String mName,phone,address;
    private Float lat,lng;
    private String image;
    private Time openClosingTime;
    public Restaurant() {
    }

    public Restaurant(int mId, String mName, String mPrice) {
        this.mId = mId;
        this.mName = mName;
        this.mPrice = mPrice;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Time getOpenClosingTime() {
        return openClosingTime;
    }

    public void setOpenClosingTime(Time openClosingTime) {
        this.openClosingTime = openClosingTime;
    }

    public String getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(String userOwner) {
        this.userOwner = userOwner;
    }

    private String userOwner;
    String mPrice;



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
