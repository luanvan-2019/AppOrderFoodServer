package com.hcmunre.apporderfoodserver.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Food;
import com.hcmunre.apporderfoodserver.models.MenuFoodRestaurant;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ProductAdapter extends ExpandableRecyclerViewAdapter<MenuFoodViewHolder,ProductViewHolder> {
    List<? extends ExpandableGroup> grupo;
    public ProductAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
        grupo=groups;

    }
    public void expandAllGroups(){

        for(int i = 0;i<grupo.size();i++){
            if (!isGroupExpanded(grupo.get(i))) {
                onGroupClick(expandableList.getFlattenedGroupIndex(i));
            }
        }
    }
    @Override
    public MenuFoodViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menufood_restaurant,parent,false);
        return new MenuFoodViewHolder(view);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_restaurant,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Food product=(Food)group.getItems().get(childIndex);
        holder.bind(product);
    }

    @Override
    public void onBindGroupViewHolder(MenuFoodViewHolder holder, int flatPosition, ExpandableGroup group) {
        final MenuFoodRestaurant menuFoodRestaurant =(MenuFoodRestaurant)group;
        holder.bind(menuFoodRestaurant);
    }


}
