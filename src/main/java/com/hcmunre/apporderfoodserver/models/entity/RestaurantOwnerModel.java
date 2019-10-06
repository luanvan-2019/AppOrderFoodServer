package com.hcmunre.apporderfoodserver.models.entity;

import java.util.ArrayList;

public class RestaurantOwnerModel {
    private boolean success;
    private String message;
    private ArrayList<RestaurantOwner> results;

    public RestaurantOwnerModel(boolean success, String message, ArrayList<RestaurantOwner> results) {
        this.success = success;
        this.message = message;
        this.results = results;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<RestaurantOwner> getResults() {
        return results;
    }

    public void setResults(ArrayList<RestaurantOwner> results) {
        this.results = results;
    }
}
