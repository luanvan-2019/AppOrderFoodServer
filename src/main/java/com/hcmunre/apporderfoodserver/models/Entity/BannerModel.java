package com.hcmunre.apporderfoodserver.models.Entity;

public class BannerModel {
    private int id;
    private int image;

    public BannerModel(int id, int image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
