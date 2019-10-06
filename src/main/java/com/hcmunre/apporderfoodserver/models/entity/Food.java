package com.hcmunre.apporderfoodserver.models.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    public final String name;
    public final int imageFood;
    public final Float price;

    public Food(String name, int imageFood, Float price) {
        this.name = name;
        this.imageFood = imageFood;
        this.price = price;
    }

    protected Food(Parcel in) {
        name = in.readString();
        imageFood = in.readInt();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(imageFood);
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(price);
        }
    }
}
