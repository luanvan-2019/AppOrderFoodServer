package com.hcmunre.apporderfoodserver.models.Database;

import com.hcmunre.apporderfoodserver.models.entity.Food;
import com.hcmunre.apporderfoodserver.models.entity.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodData {
    Connection con;
    DataConnetion dataConnetion =new DataConnetion();
    PreparedStatement pst;
    ResultSet rs;
//    public ArrayList<Food> getAllFood(){
//        ArrayList<Food> foods = new ArrayList<>();//tạo mảng đề lưu food
//        try {
//            String sql="EXEC Sp_SelectFood";
//            con =dataConnetion.connectionData();
//            pst= con.prepareStatement(sql);
//            rs=pst.executeQuery();
//            Food food;
//            while(rs.next()){
//                food=new Food(rs.getString("Name"),rs.getInt("Image"),rs.getFloat("Price"));
//                foods.add(food);
//            }
//            con.close();
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return foods;
//    }
    public  ArrayList getMenuFood(){
        ArrayList arrayList=new ArrayList();
        try {
            String sql="EXEC Sp_SelectMenu";
            con =dataConnetion.connectionData();
            pst= con.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
               arrayList.add(rs.getString("Name"));
            }
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    //server localhost
    public ArrayList<Menu> getMenuResFood(int restaurantId){
        ArrayList<Menu> listMenuFood=new ArrayList();
        try {
            String sql="Exec Sp_SelectMenuRes '"+restaurantId+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            Menu menu;
            while (rs.next()){
                menu=new Menu();
                menu.setmId(rs.getInt("Id"));
                menu.setmName(rs.getString("Name"));
                listMenuFood.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMenuFood;
    }
    public ArrayList<Food> getFoodOfMenu(int menuId) throws SQLException {
        ArrayList<Food> listFoodOfMenu=new ArrayList<>();
        String sql="Exec Sp_SelectFood '"+menuId+"'";
        con=dataConnetion.connectionData();
        pst=con.prepareStatement(sql);
        rs=pst.executeQuery();
        Food food;
        while(rs.next()){
            food=new Food();
            food.setName(rs.getString("Name"));
            food.setPrice(rs.getFloat("Price"));
            listFoodOfMenu.add(food);
        }
        return listFoodOfMenu;
    }

}
