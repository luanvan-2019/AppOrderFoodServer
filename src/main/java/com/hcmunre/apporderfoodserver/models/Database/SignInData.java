package com.hcmunre.apporderfoodserver.models.Database;

import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignInData {
    Connection con;
    DataConnetion dataCon = new DataConnetion();
    String z = "";

    public String login(RestaurantOwner restaurantOwner) {
        try {
            con = dataCon.connectionData();
            if (con == null) {
                z = "Vui lòng kiểm tra kết nối";
            } else {
                String query = "Exec Sp_SelectRestaurantLogin '" + restaurantOwner.getEmail().toString() + "','" + restaurantOwner.getPassword().toString() + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Common.currentRestaurant = new Restaurant();
                    Common.currentRestaurant.setmId(rs.getInt("Id"));
                    Common.currentRestaurant.setmName(rs.getString("Name"));
                    Common.currentRestaurant.setAddress(rs.getString("Address"));
                    Common.currentRestaurantOwner = new RestaurantOwner();
                    Common.currentRestaurantOwner.setName(rs.getString("NameOwner"));
                    Common.currentRestaurantOwner.setAddress(rs.getString("AddressOwner"));
                    Common.currentRestaurantOwner.setPhone(rs.getString("PhoneOwner"));
                    Common.currentRestaurantOwner.setImage(rs.getString("ImageOwner"));
                    Common.currentRestaurantOwner.setEmail(rs.getString("Email"));
                    z = "success";
                    con.close();
                } else {
                    z = "Không đúng tên đăng nhập hoặc mật khẩu";
                }
            }
        } catch (Exception e) {
            z = "Error";
        }
        return z;
    }

    public RestaurantOwner getInforRestaurant(RestaurantOwner restaurantOwner) {
        RestaurantOwner restaurantOwner1=null;
        try {

            String sql = "Exec Sp_SelectRestaurantLogin '" + restaurantOwner.getEmail().toString() + "','" + restaurantOwner.getPassword().toString() + "'";
            con=dataCon.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if (rs.next()) {
                restaurantOwner1 = new RestaurantOwner();
                restaurantOwner1.setRestaurantId(rs.getInt("Id"));
                restaurantOwner1.setRestaurantName(rs.getString("Name"));
                restaurantOwner1.setRestaurantAddress(rs.getString("Address"));
                restaurantOwner1.setName(rs.getString("NameOwner"));
                restaurantOwner1.setAddress(rs.getString("AddressOwner"));
                restaurantOwner1.setPhone(rs.getString("PhoneOwner"));
                restaurantOwner1.setImage(rs.getString("ImageOwner"));
                restaurantOwner1.setEmail(rs.getString("Email"));
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantOwner1;
    }

}
