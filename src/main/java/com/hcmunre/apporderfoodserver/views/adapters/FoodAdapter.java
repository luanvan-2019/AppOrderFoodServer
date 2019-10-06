package com.hcmunre.apporderfoodserver.views.adapters;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.models.entity.Food;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{
    ArrayList<Food> arrayList;
    public FoodAdapter(ArrayList<Food> arrayList) {
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_restaurant,null);
//        return new ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food=arrayList.get(position);
        holder.itemView.setTag(position);
//        holder.txtitem_name.setText(food.getName());
//        holder.txtitem_price.setText(food.getPrice()+"");
//        holder.img_food.setImageResource(food.getImageFood());
    }
    @Override
    public int getItemCount() {
        return arrayList.size()>0? arrayList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtitem_name,txtitem_price;
        ImageView img_food;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            txtitem_name=itemView.findViewById(R.id.txtitem_name);
//            txtitem_price=itemView.findViewById(R.id.txtitem_price);
//            img_food=itemView.findViewById(R.id.img_food);
        }
    }
    public void addFood(Food food){
        arrayList.add(food);
        notifyDataSetChanged();
    }
}
