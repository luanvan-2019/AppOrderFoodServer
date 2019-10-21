package com.hcmunre.apporderfoodserver.models.Database;

import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.entity.RestaurantOwner;

import java.sql.Connection;
import java.sql.ResultSet;
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
                    Common.currentRestaurant=new Restaurant();
                    Common.currentRestaurant.setmId(rs.getInt("Id"));
                    Common.currentRestaurant.setmName(rs.getString("Name"));
                    Common.currentRestaurant.setAddress(rs.getString("Address"));
                    Common.currentRestaurantOwner=new RestaurantOwner();
                    Common.currentRestaurantOwner.setName(rs.getString("NameOwner"));
                    Common.currentRestaurantOwner.setAddress(rs.getString("AddressOwner"));
                    Common.currentRestaurantOwner.setPhone(rs.getString("PhoneOwner"));
                    Common.currentRestaurant.setImage(rs.getString("ImageOwner"));
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
}
