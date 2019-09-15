package com.hcmunre.apporderfoodserver.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    public final String name;
    public final String price;
    public final int imageFood;

    public Food(String name, String price, int imageFood) {
        this.name = name;
        this.price = price;
        this.imageFood = imageFood;
    }


    protected Food(Parcel in) {
        name = in.readString();
        price = in.readString();
        imageFood = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(imageFood);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
