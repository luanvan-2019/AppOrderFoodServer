package com.hcmunre.apporderfoodserver.models.entity;

import java.io.Serializable;

public class Menu implements Serializable {
    private int mId;
    private String mName;
    private int mImage;

    public Menu() {
    }

    public Menu(int mId, String mName, int mImage) {
        this.mId = mId;
        this.mName = mName;
        this.mImage = mImage;
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

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }
}
