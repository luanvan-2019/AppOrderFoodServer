package com.hcmunre.apporderfoodserver.commons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Entity.Menu;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;
import com.hcmunre.apporderfoodserver.models.Entity.ShipperOrder;

import java.io.File;

public class Common {
    public static Restaurant currentRestaurant;
    public static Order currentOrder;
    public static Shipper currentShiper;
    public static ShipperOrder currrentShipperOrder;
    public static RestaurantOwner currentRestaurantOwner;
    public static String ID_USER = "";
    public static String KEY_RESTAURANT = "data_restaurant";
    public static String KEY_MENU = "data_menu";
    public static final String UPDATE = "Cập nhật";
    public static final String DELETE = "Xóa";
    public static final String TAG="ERROR";
    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {

                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
    public static int convertStatusToIndex(int orderStatus){
        if(orderStatus==-1){
            return 3;
        }else {
            return orderStatus;
        }
    }
    public static int convertStatusFoodToIndex(int statusFood){
        if(statusFood==1)
            return 1;
        return statusFood;
    }
    public static String convertStatusToString(int orderStatus) {
        switch (orderStatus) {
            case 0:
                return "Đã đặt";
            case 1:
                return "Đang giao";
            case 2:
                return "Đã giao";
            case -1:
                return "Đã hủy";
            default:
                return "Đã hủy";
        }
    }
    public static String convertStatusFoodToString(int statusFood){
        switch (statusFood){
            case 0:
                return "Hết món";
            case 1:
                return "Đang bán";
            default:
                return "Đang bán";
        }
    }
    public static int convertStringToStatus(String status){
        if(status.equals("Đã đặt")){
            return 0;
        }else if(status.equals("Đang giao")){
            return 1;
        }else if(status.equals("Đã giao")){
            return 2;
        }else if(status.equals("Đã hủy")){
            return -1;
        }
        return -1;
    }
    public static int convertStringToStatusFood(String status){
        if(status.equals("Hết món")){
            return 0;
        }else if(status.equals("Đang bán")){
            return 1;
        }
        return 1;
    }
    public static String getAppPath(Context context) {
        File file=new File(android.os.Environment.getExternalStorageDirectory()
        +File.separator
        +context.getResources().getString(R.string.app_name)
        +File.separator);
        if(!file.exists()){
            file.mkdir();
        }
        return file.getPath()+File.separator;
    }
    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getTopicChannel(int userId) {
        return new StringBuilder("Restaurant").append(userId).toString();
    }
    public static String createTopicSender(String topicChannel) {
        return new StringBuilder("/topics/").append(topicChannel).toString();
    }

    public static String getTopicChannelShippper(int userId) {
        return new StringBuilder("Shipper").append(userId).toString();
    }
}
