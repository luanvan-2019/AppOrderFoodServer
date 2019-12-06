package com.hcmunre.apporderfoodserver.models.Database;

import android.util.Log;

import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.hcmunre.apporderfoodserver.models.Entity.Menu;
import com.hcmunre.apporderfoodserver.models.Entity.MenuFood;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuData {
    Connection con;
    DataConnetion dataConnetion = new DataConnetion();
    CallableStatement callable;

    public int insertMenuRes(Menu menu) {
        int res = 0;
        try {
            String sql = "{call Sp_InsertMenuRes (?,?,?)}";
            con = dataConnetion.connectionData();
            callable = con.prepareCall(sql);
            callable.setString(1, menu.getmName().toString());
            callable.setInt(2, menu.getRestaurantId());
            callable.setString(3,menu.getImage());
            res = callable.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.w("Lỗi Kết nối", "" + e.getMessage());
        }
        return res;
    }
    public void deleteMenuRes(int indexDelete) {
        try {

            String sql = "Exec Sp_DeleteMenu '" + indexDelete + "'";
            con = dataConnetion.connectionData();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.executeQuery();
            con.close();
        } catch (Exception e) {
        }
    }
    public boolean updateMenu(Menu menu) {
        boolean success=false;
        try {
            String sql = "Exec Sp_UpdateMenu (?,?,?,?)";
            con = dataConnetion.connectionData();
            PreparedStatement pst = con.prepareCall(sql);
            pst.setInt(1,menu.getmId());
            pst.setString(2,menu.getmName().toString());
            pst.setInt(3,menu.getRestaurantId());
            pst.setString(4,menu.getImage());
            if(pst.executeUpdate()>0){
                success=true;
            }else{
                success=false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    public ArrayList<MenuFood> getMenuFood(int restaurantId){
        ArrayList<MenuFood> menuFoods=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectMenuFood '"+restaurantId+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                MenuFood menuFood=new MenuFood();
                menuFood.setNameMenu(rs.getString("NameMenu"));
                menuFood.setNameFood(rs.getString("NameFood"));
                menuFoods.add(menuFood);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuFoods;
    }
}
