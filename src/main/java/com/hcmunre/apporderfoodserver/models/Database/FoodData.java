package com.hcmunre.apporderfoodserver.models.Database;

import android.util.Log;

import com.hcmunre.apporderfoodserver.models.entity.Food;
import com.hcmunre.apporderfoodserver.models.entity.Menu;

import java.sql.CallableStatement;
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
            food.setPrice(rs.getFloat("Price"));
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
            pst.setFloat(4, food.getPrice());
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
            String sql="Exec Sp_UpdateFood '"+food.getId()+"','"+food.getName().toString()+"','"+food.getImageFood()+"'," +
                    "'"+food.getDescription().toString()+"','"+food.getPrice()+"','"+food.getMenuId()+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
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


}
