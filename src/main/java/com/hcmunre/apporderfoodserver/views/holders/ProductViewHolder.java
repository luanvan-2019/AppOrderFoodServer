package com.hcmunre.apporderfoodserver.views.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductViewHolder extends ChildViewHolder {
    public TextView txtitem_name,txtitem_price,quantityTxt,llMinus;
    public ImageView img_food;
    public LinearLayout layoutPlus;
    public int quantity;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    public ProductViewHolder(View itemView) {
        super(itemView);
        txtitem_name=itemView.findViewById(R.id.txtitem_name);
        txtitem_price=itemView.findViewById(R.id.txtitem_price);
        img_food=itemView.findViewById(R.id.img_food);
    }
    public void bind(Food food){
        txtitem_name.setText(food.getName());
//        txtitem_price.setText(String.valueOf(currencyVN.format(food.getPrice())));
    }
}
