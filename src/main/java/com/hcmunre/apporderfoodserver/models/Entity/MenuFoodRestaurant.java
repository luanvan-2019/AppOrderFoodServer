package com.hcmunre.apporderfoodserver.models.Entity;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class MenuFoodRestaurant extends ExpandableGroup<Food> {
    public MenuFoodRestaurant(String title, ArrayList<Food> items) {
        super(title, items);
    }
}
