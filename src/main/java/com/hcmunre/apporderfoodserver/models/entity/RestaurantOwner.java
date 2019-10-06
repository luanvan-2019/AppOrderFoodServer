package com.hcmunre.apporderfoodserver.models.entity;

public class RestaurantOwner {
    private int id;
    private int restaurantId;
    private String name,phone,email,password;
    private boolean status;
    public RestaurantOwner(){
    }

    public RestaurantOwner(int id, int restaurantId, String name, String phone, String email, String password, boolean status) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
