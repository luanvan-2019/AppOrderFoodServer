package com.hcmunre.apporderfoodserver.models.Database;

import com.hcmunre.apporderfoodserver.models.Entity.MaxOrder;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;
import com.hcmunre.apporderfoodserver.models.Entity.ShipperOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShipperData {
    Connection con;
    DataConnetion dataConnetion = new DataConnetion();

    public ShipperData() {
        con = dataConnetion.connectionData();
    }
    public boolean createNewShipper(Shipper shipper) {
        boolean success = false;
        try {
            String sql="Exec Sp_CreatNewShipper (?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst=con.prepareCall(sql);
            pst.setString(1,shipper.getName());
            pst.setString(2,shipper.getAddress());
            pst.setString(3,shipper.getPhone());
            pst.setDouble(4,shipper.getLat());
            pst.setDouble(5,shipper.getLng());
            pst.setString(6,shipper.getImage());
            pst.setString(7,shipper.getEmail());
            pst.setString(8,HashPass.md5(shipper.getPassword()));
            pst.setInt(9,shipper.getRestaurantId());
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
    public ArrayList<Shipper> getAllShipper(int restaurantId){
        ArrayList<Shipper> shippers=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectShippers '"+restaurantId+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                Shipper shipper=new Shipper();
                shipper.setId(rs.getInt("Id"));
                shipper.setName(rs.getString("Name"));
                shipper.setAddress(rs.getString("Address"));
                shipper.setPhone(rs.getString("Phone"));
                shipper.setImage(rs.getString("Image"));
                shipper.setEmail(rs.getString("Email"));
                shipper.setPassword(rs.getString("Password"));
                shippers.add(shipper);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippers;
    }
    public ArrayList<ShipperOrder> getOrderForShipper(int shipperId, int startIndex, int endIndex){
        ArrayList<ShipperOrder> shipperOrders=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectOrderForShipperBK '"+shipperId+"','"+startIndex+"','"+endIndex+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                ShipperOrder shipperOrder=new ShipperOrder();
                shipperOrder.setUserId(rs.getInt("UserId"));
                shipperOrder.setOrderID(rs.getInt("OrderId"));
                shipperOrder.setShippingStatus(rs.getInt("ShippingStatus"));
                shipperOrder.setOrderName(rs.getString("OrderName"));
                shipperOrder.setOrderAddress(rs.getString("OrderAddress"));
                shipperOrder.setOrderPhone(rs.getString("OrderPhone"));
                shipperOrder.setDate(rs.getString("OrderDate"));
                shipperOrder.setOrderStatus(rs.getInt("OrderStatus"));
                shipperOrder.setTotalPrice(rs.getDouble("TotalPrice"));
                shipperOrder.setNumberOfItem(rs.getInt("NumberOfItem"));
                shipperOrders.add(shipperOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shipperOrders;
    }
    public MaxOrder getMaxOrderForShipper(int restaurantId){
        String sql="Exec Sp_SelectMaxOrderForShipper '"+restaurantId+"'";
        MaxOrder maxOrder = null;
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                maxOrder=new MaxOrder(rs.getInt("MaxRowNum"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxOrder;
    }
    public Shipper getInforShipper(Shipper shipper){
        Shipper shipper1= null;
        try {
            String sql="EXEC Sp_SelectShipperLogin '"+shipper.getEmail().toString()+"'," +
                    "'"+ HashPass.md5(shipper.getPassword())+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                shipper1=new Shipper();
                shipper1.setId(rs.getInt("Id"));
                shipper1.setName(rs.getString("Name"));
                shipper1.setAddress(rs.getString("Address"));
                shipper1.setPhone(rs.getString("Phone"));
                shipper1.setImage(rs.getString("Image"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shipper1;
    }
    public boolean updateStatusShipping(int orderId,int shippingStatus){
        boolean success=false;
        try {
            String sql="Exec Sp_UpdateShippingStatus '"+orderId+"','"+shippingStatus+"'";
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
