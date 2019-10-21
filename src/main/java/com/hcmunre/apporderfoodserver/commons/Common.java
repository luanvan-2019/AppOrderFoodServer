package com.hcmunre.apporderfoodserver.commons;

import com.hcmunre.apporderfoodserver.models.entity.Menu;
import com.hcmunre.apporderfoodserver.models.entity.Restaurant;
import com.hcmunre.apporderfoodserver.models.entity.RestaurantOwner;

public class Common {
    public static Restaurant currentRestaurant;
    public static Menu menu;
    public static RestaurantOwner currentRestaurantOwner;
    public static String ID_USER = "";
    public static String KEY_RESTAURANT="data_restaurant";
    public static String KEY_MENU="data_menu";
    public static final String UPDATE = "Cập nhật";
    public static final String DELETE = "Xóa";
}
