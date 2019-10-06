package com.hcmunre.apporderfoodserver.views.adapters;


import android.app.Activity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.entity.Food;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private ArrayList<Food> arrayList;
    private Activity activity;
    int recentPos = -1;

    public FoodAdapter(ArrayList<Food> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_restaurant, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food food = arrayList.get(position);
        holder.itemView.setTag(position);
        holder.txtitem_name.setText(food.getName());
        holder.txtitem_price.setText(holder.currencyVN.format(food.getPrice()));
        holder.img_food.setImageResource(food.getImageFood());

    }

    @Override
    public int getItemCount() {
        return arrayList.size() > 0 ? arrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        @BindView(R.id.txtitem_name)
        TextView txtitem_name;
        @BindView(R.id.txtitem_price)
        TextView txtitem_price;
        @BindView(R.id.img_food)
        ImageView img_food;
        int quantity;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Chọn");
            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
        }
    }

    public void addFood(Food food) {
        arrayList.add(food);
        notifyDataSetChanged();
    }
}
