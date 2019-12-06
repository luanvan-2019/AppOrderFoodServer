package com.hcmunre.apporderfoodserver.models.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private int id;
    private String name;
    private String imageFood;
    private String Description;
    private int price;
    private int menuId;
    private int statusFood;
    private int quantity;
    public Food(){

    }

    public Food(int id, String name, String imageFood, String description, int price, int menuId) {
        this.id = id;
        this.name = name;
        this.imageFood = imageFood;
        Description = description;
        this.price = price;
        this.menuId = menuId;
    }


    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageFood = in.readString();
        Description = in.readString();
        price = in.readInt();
        menuId = in.readInt();
        statusFood = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFood() {
        return imageFood;
    }

    public void setImageFood(String imageFood) {
        this.imageFood = imageFood;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getStatusFood() {
        return statusFood;
    }

    public void setStatusFood(int statusFood) {
        this.statusFood = statusFood;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageFood);
        dest.writeString(Description);
        dest.writeInt(price);
        dest.writeInt(menuId);
        dest.writeInt(statusFood);
        dest.writeInt(quantity);
    }
}
