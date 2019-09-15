package com.hcmunre.apporderfoodserver.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class MenuFoodRestaurant extends ExpandableGroup<Food> {
    public MenuFoodRestaurant(String title, List<Food> items) {
        super(title, items);
    }
}
