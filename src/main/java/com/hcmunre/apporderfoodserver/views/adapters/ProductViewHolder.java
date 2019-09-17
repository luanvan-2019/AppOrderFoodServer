package com.hcmunre.apporderfoodserver.views.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Food;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ProductViewHolder extends ChildViewHolder {
    private TextView txtitem_name, txtitem_price;
    private ImageView img_food;

    public ProductViewHolder(View itemView) {
        super(itemView);
        txtitem_name = itemView.findViewById(R.id.txtitem_name);
        txtitem_price = itemView.findViewById(R.id.txtitem_price);
        img_food = itemView.findViewById(R.id.img_food);
    }

    public void bind(Food food) {
        txtitem_name.setText(food.name);
        txtitem_price.setText(food.price);
        img_food.setImageResource(food.imageFood);
    }
}
