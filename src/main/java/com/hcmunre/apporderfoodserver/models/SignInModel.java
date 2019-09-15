package com.hcmunre.apporderfoodserver.models;

import com.hcmunre.apporderfoodserver.models.Database.DataConnetion;
import com.hcmunre.apporderfoodserver.models.Database.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignInModel {
    Connection con;
    DataConnetion dataCon = new DataConnetion();
    String z = "";
//    boolean isSuccess=false;
    public String DangNhap(User user) {
        try {
            con = dataCon.connectionData();
            if (con == null) {
                z = "Vui lòng kiểm tra kết nối";
            } else {
                String query = "Select Email,Pasword from tb_NguoiDung where Email='" + user.getEmail().toString() + "'and Pasword='" + user.getPassword().toString() + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
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
//        try
//        {
//            con = dataCon.connectionData();
//            if (con == null)
//            {
//                z = "Vui lòng kiểm tra kết nối!";
//            }
//            else
//            {
//                String query = "Select Email,Pasword from tb_NguoiDung where Email='" + user.getEmail().toString()
//                        + "'and Pasword='" + user.getPassword().toString() + "'";
//                Statement stmt = con.createStatement();
//                ResultSet rs = stmt.executeQuery(query);
//                if(rs.next())
//                {
//                    z = "Login successful";
//                    isSuccess=true;
//                    con.close();
//                }
//                else
//                {
//                    z = "Invalid Credentials!";
//                    isSuccess = false;
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            isSuccess = false;
//            z = ex.getMessage();
//        }
//        return z;
    }

}
