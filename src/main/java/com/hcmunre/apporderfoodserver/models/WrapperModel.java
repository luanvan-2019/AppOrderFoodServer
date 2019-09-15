package com.hcmunre.apporderfoodserver.models;

import java.util.ArrayList;

public class WrapperModel {
    private ArrayList<BannerModel> mBanner;
    private ArrayList<BannerModel> mrArticle;

    public WrapperModel(ArrayList<BannerModel> mBanner, ArrayList<BannerModel> mrArticle) {
        this.mBanner = mBanner;
        this.mrArticle = mrArticle;
    }

    public ArrayList<BannerModel> getmBanner() {
        return mBanner;
    }

    public void setmBanner(ArrayList<BannerModel> mBanner) {
        this.mBanner = mBanner;
    }

    public ArrayList<BannerModel> getMrArticle() {
        return mrArticle;
    }

    public void setMrArticle(ArrayList<BannerModel> mrArticle) {
        this.mrArticle = mrArticle;
    }
}
