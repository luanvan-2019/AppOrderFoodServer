package com.hcmunre.apporderfoodserver.commons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Entity.FavoriteOnlyId;
import com.hcmunre.apporderfoodserver.models.Entity.Menu;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.models.Entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.Entity.RestaurantOwner;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;
import com.hcmunre.apporderfoodserver.models.Entity.ShipperOrder;
import com.hcmunre.apporderfoodserver.notifications.MySingleton;
import com.hcmunre.apporderfoodserver.views.activities.OrderDetailActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
    public static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    public static String serverKey = "key=" + "AAAABxgzzk4:APA91bFOUq0T_vGnwemLQfJcU6akuV1gLQVJdL5mxyxV1m1bDeDbapGb8mWH0gKqSL2tSyuS_A7kTD3iWTfeFK0NhHNhcu8TY7Z7ClSu8LA2xJSJoDaYhbOge7MUF1J8V6FSRiUeDW8i";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public static String contentType = "application/json";
    public static ArrayList<FavoriteOnlyId> currentFavorite;
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
    public static String getTopicChannelUser(int userId) {
        return new StringBuilder("User").append(userId).toString();
    }
    public static void sendNotification(JSONObject notification,Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Yêu cầu lỗi", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Lỗi");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    public static boolean checkFavorite(int id) {
        boolean result=false;
        for (FavoriteOnlyId item:currentFavorite){
            if(item.getFoodId()==id){
                result=true;
            }
        }
        return result;
    }
    public static Bitmap getBitmap(String image){
        byte[] decodeString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodebitmap;
    }
}
