package com.hcmunre.apporderfoodserver.models.Database;

import com.bumptech.glide.util.CachedHashCodeArrayMap;
import com.hcmunre.apporderfoodserver.models.Entity.Category;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import io.reactivex.Observable;

public class RestaurantData {
    Connection con;
    DataConnetion dataConnetion=new DataConnetion();

    public RestaurantData() {
        con=dataConnetion.connectionData();
    }

    public boolean createNewRestaurant(Restaurant restaurant, RestaurantOwner restaurantOwner){
        try {
            String sql="Exec Sp_CreateNewRestaurant (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst=con.prepareCall(sql);
            pst.setString(1,restaurant.getmName());
            pst.setString(2,restaurant.getPhone());
            pst.setString(3,restaurantOwner.getName());
            pst.setString(4,restaurantOwner.getEmail());
            pst.setString(5,restaurantOwner.getPassword());
            pst.setTime(6, restaurant.getOpening());
            pst.setTime(7, restaurant.getClosing());
            pst.setString(8,restaurant.getAddress());
            pst.setDouble(9,restaurant.getLat());
            pst.setDouble(10,restaurant.getLng());
            if(pst.executeUpdate()>0){
                con.close();
                return true;
            }else {
                con.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public Observable<Boolean> checkExistRestaurantOwner(String email){
        boolean success=false;
        try {
            String sql="Exec Sp_CheckExistRestaurantOwner '"+email+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            if(pst.executeUpdate()>0){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Observable.just(success);
    }
    public boolean updateRestaurant(Restaurant restaurant){
        boolean success=false;
        try {
            String sql="EXEC Sp_UpdateRestaurant (?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst=con.prepareCall(sql);
            pst.setInt(1,restaurant.getmId());
            pst.setString(2,restaurant.getmName());
            pst.setString(3,restaurant.getAddress());
            pst.setString(4,restaurant.getPhone());
            pst.setDouble(5,restaurant.getLat());
            pst.setDouble(6,restaurant.getLng());
            pst.setString(7,restaurant.getImage());
            pst.setTime(8, restaurant.getOpening());
            pst.setTime(9, restaurant.getClosing());
            if(pst.executeUpdate()>0){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    public boolean updateImage(Restaurant restaurant){
        boolean success=false;
        try {
            String sql="Exec Sp_UpdateImage '"+restaurant.getmId()+"','"+restaurant.getPhone()+"','"+restaurant.getImage()+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            if(pst.executeUpdate()>0){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    //admin management
    public ArrayList<Category> getCategory(){
        ArrayList<Category> categories=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectCategory";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                Category category=new Category();
                category.setId(rs.getInt("Id"));
                category.setName(rs.getString("Name"));
                category.setImage(rs.getString("Image"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public boolean updateCategory(Category category){
        boolean success=false;
        try {
            String sql="Exec Sp_UpdateCategory '"+category.getId()+"','"+category.getName().toString()+"','"+category.getImage()+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            if(pst.executeUpdate()>0){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    public boolean insertCategory(Category category){
        boolean success=false;
        try {
            String sql="Exec Sp_InsertCategory (?,?)";
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setString(1,category.getName());
            pst.setString(2, category.getImage());
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    public boolean deleteCategory(int cateId){
        boolean success=false;
        try {
            String sql="Exec Sp_DeleteCategory '"+cateId+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            if(pst.executeUpdate()>0){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}
