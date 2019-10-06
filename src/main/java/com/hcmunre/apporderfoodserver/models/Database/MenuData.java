package com.hcmunre.apporderfoodserver.models.Database;

import android.util.Log;

import com.hcmunre.apporderfoodserver.models.entity.Menu;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuData {
    Connection con;
    DataConnetion dataConnetion=new DataConnetion();
    PreparedStatement pst;
    ResultSet rs;
    CallableStatement callable;
    public int insertMenuRes(Menu menu){
        int res=0;
        try {
            String sql = "{call Sp_InsertMenuRes (?,?)}";
            con = dataConnetion.connectionData();
            callable = con.prepareCall(sql);
            callable.setString(1, menu.getmName());
            callable.setInt(2, menu.getRestaurantId());
            res = callable.executeUpdate();
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
            Log.w("Lỗi Kết nối","" + e.getMessage());
        }
        return res;
    }
    public String deleteMenuRes(String indexDelete) {
        try {
            String sql = "DELETE FROM Menu WHERE Id='"+indexDelete+"'";
            con = dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            pst.executeQuery();
            con.close();
        } catch (Exception e) {
        }
        return null;
    }
}
