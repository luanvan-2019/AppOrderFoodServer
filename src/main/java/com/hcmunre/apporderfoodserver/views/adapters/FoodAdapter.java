package com.hcmunre.apporderfoodserver.views.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.Entity.FavoriteOnlyId;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
import com.hcmunre.apporderfoodserver.models.Entity.Menu;
import com.hcmunre.apporderfoodserver.models.Entity.Status;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private ArrayList<Food> arrayList;
    private Context mContext;
    CompositeDisposable compositeDisposable;
    FoodData foodData = new FoodData();

    public FoodAdapter() {
    }

    public FoodAdapter(ArrayList<Food> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        compositeDisposable = new CompositeDisposable();
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food food = arrayList.get(position);
        holder.itemView.setTag(position);
        holder.txtitem_name.setText(food.getName());
        holder.txtitem_price.setText(new StringBuilder(holder.currencyVN.format(food.getPrice())).append("đ"));
        if(food.getImageFood()!=null){
            holder.img_food.setImageBitmap(Common.getBitmap(food.getImageFood()));
        }
        holder.txtdescription.setText(food.getDescription());
        Log.d("BBB", "" + food.getStatusFood());
        if (food.getStatusFood() == 1) {
            holder.switch_status.setChecked(true);
        } else if (food.getStatusFood() == 0) {
            holder.switch_status.setChecked(false);
        }
        //
        holder.switch_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.switch_status.isChecked()) {
                    Food food1 = new Food();
                    food1.setId(food.getId());
                    food1.setStatusFood(1);
                    boolean success = foodData.updateStatus(food1);
                    if (success == true) {
                        Toast.makeText(mContext, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(mContext, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                } else {
                    Food food1 = new Food();
                    food1.setId(food.getId());
                    food1.setStatusFood(0);
                    boolean success = foodData.updateStatus(food1);
                    if (success == true) {
                        Toast.makeText(mContext, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(mContext, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public Food getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size() > 0 ? arrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        @BindView(R.id.txtitem_name)
        TextView txtitem_name;
        @BindView(R.id.txtitem_price)
        TextView txtitem_price;
        @BindView(R.id.txtdescription)
        TextView txtdescription;
        @BindView(R.id.img_food)
        ImageView img_food;
        @BindView(R.id.switch_status)
        SwitchCompat switch_status;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getInstance(localeVN);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Chọn");
            contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);
        }
    }

    public void removeFood(int position) {
        FoodData foodData = new FoodData();
        Food food = new Food();
        food.setId(arrayList.get(position).getId());
        foodData.deleteFood(food);
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());

    }

    public void updateFood(int position, Food food) {
        FoodData foodData = new FoodData();
        food.setId(arrayList.get(position).getId());
        Observable<Boolean> updateFoodOfenu = Observable.just(foodData.updateFood(food));
        compositeDisposable.add(
                updateFoodOfenu
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> {
                            if (aBoolean == true) {
                                Toast.makeText(mContext, "Đã Cập nhật", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "chưa Cập nhật", Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(mContext, "Lỗi " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
        notifyItemChanged(position);
    }
}
