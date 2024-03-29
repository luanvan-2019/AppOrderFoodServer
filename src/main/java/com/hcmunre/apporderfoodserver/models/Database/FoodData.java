package com.hcmunre.apporderfoodserver.models.Database;

import android.util.Log;

import com.hcmunre.apporderfoodserver.models.Entity.FavoriteOnlyId;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.hcmunre.apporderfoodserver.models.Entity.HotFood;
import com.hcmunre.apporderfoodserver.models.Entity.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodData {
    Connection con;
    DataConnetion dataConnetion = new DataConnetion();
    PreparedStatement pst;
    ResultSet rs;
    public ArrayList<Menu> getMenuResFood(int restaurantId) {
        ArrayList<Menu> listMenuFood = new ArrayList();
        try {
            String sql = "Exec Sp_SelectMenuRes '" + restaurantId + "'";
            con = dataConnetion.connectionData();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            Menu menu;
            while (rs.next()) {
                menu = new Menu();
                menu.setmId(rs.getInt("Id"));
                menu.setmName(rs.getString("Name"));
                menu.setImage(rs.getString("Image"));
                listMenuFood.add(menu);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMenuFood;
    }

    public ArrayList<Food> getFoodOfMenu(int menuId) throws SQLException {
        ArrayList<Food> listFoodOfMenu = new ArrayList<>();
        String sql = "Exec Sp_SelectFood '"+menuId+"'";
        con = dataConnetion.connectionData();
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();
        Food food;
        while (rs.next()) {
            food = new Food();
            food.setId(rs.getInt("Id"));
            food.setName(rs.getString("Name"));
            food.setImageFood(rs.getString("Image"));
            food.setDescription(rs.getString("Description"));
            food.setPrice(rs.getInt("Price"));
            food.setStatusFood(rs.getInt("Status"));
            listFoodOfMenu.add(food);
        }
        con.close();
        return listFoodOfMenu;
    }
    public int insertFood(Food food) {
        int res=0;
        try {
            String sql = "Exec Sp_InsertFood (?,?,?,?,?)";
            con = dataConnetion.connectionData();
            PreparedStatement pst = con.prepareCall(sql);
            pst.setString(1, food.getName());
            pst.setString(2, food.getImageFood());
            pst.setString(3, food.getDescription());
            pst.setInt(4, food.getPrice());
            pst.setInt(5, food.getMenuId());
            res = pst.executeUpdate();
            if(res>0){
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.w("Lỗi Kết nối", "" + e.getMessage());
        }
        return res;
    }
    public boolean deleteFood(Food food){
        try {
            String sql="Exec Sp_DeleteFood '"+food.getId()+"'";
            con=dataConnetion.connectionData();
            pst = con.prepareStatement(sql);
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
    public boolean updateFood(Food food){
        try {
            String sql="Exec Sp_UpdateFood (?,?,?,?,?,?)";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareCall(sql);
            pst.setInt(1,food.getId());
            pst.setString(2,food.getName());
            pst.setString(3,food.getImageFood());
            pst.setString(4,food.getDescription());
            pst.setInt(5,food.getPrice());
            pst.setInt(6,food.getMenuId());
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
    public boolean updateStatus(Food food){
        boolean success=false;
        try {
            String sql="Exec Sp_UpdateStatus (?,?)";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareCall(sql);
            pst.setInt(1,food.getId());
            pst.setInt(2,food.getStatusFood());
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
    public ArrayList<HotFood> reportHotFood(int restaurantId){
        ArrayList<HotFood> hotFoods=new ArrayList<>();
        try {
            String sql="Exec Sp_ReportHotFood '"+restaurantId+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                HotFood hotFood=new HotFood();
                hotFood.setFoodId(rs.getInt("FoodId"));
                hotFood.setName(rs.getString("Name"));
                hotFood.setPercent(rs.getDouble("Percentage"));
                hotFoods.add(hotFood);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotFoods;
    }
    public ArrayList<Food> reportAllFood(int restaurantId,String from,String to){
        ArrayList<Food> foods=new ArrayList<>();
        try {
            String sql="Exec Sp_ReportAllFood '"+restaurantId+"','"+from+"','"+to+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                Food food=new Food();
                food.setId(rs.getInt("FoodId"));
                food.setName(rs.getString("Name"));
                food.setPrice(rs.getInt("Price"));
                food.setQuantity(rs.getInt("Quantity"));
                foods.add(food);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }
}
