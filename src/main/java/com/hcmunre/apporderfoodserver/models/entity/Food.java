package com.hcmunre.apporderfoodserver.models.entity;

public class Food {
    private int id;
    private String name;
    private  String imageFood;
    private String Description;
    private Float price;
    private int menuId;

    public Food(){

    }

    public Food(int id, String name, String imageFood, String description, Float price, int menuId) {
        this.id = id;
        this.name = name;
        this.imageFood = imageFood;
        Description = description;
        this.price = price;
        this.menuId = menuId;
    }

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
