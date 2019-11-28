package com.hcmunre.apporderfoodserver.views.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.FoodData;
import com.hcmunre.apporderfoodserver.models.Entity.Food;
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
    private ArrayAdapter adapter;
    CompositeDisposable compositeDisposable;
    FoodData foodData = new FoodData();

    public FoodAdapter() {
    }

    public FoodAdapter(ArrayList<Food> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        compositeDisposable = new CompositeDisposable();
    }
    public void onStop(){
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
        byte[] decodeString = Base64.decode(food.getImageFood(), Base64.DEFAULT);
        Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        holder.itemView.setTag(position);
        holder.txtitem_name.setText(food.getName());
        holder.txtitem_price.setText(holder.currencyVN.format(food.getPrice()));
        holder.img_food.setImageBitmap(decodebitmap);
        holder.txtdescription.setText(food.getDescription());
        //spinner
        List<Status> spinnerList = new ArrayList<>();
        spinnerList.add(new Status(0, "Hết món"));
        spinnerList.add(new Status(1, "Đang bán"));
        adapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner_status_food.setAdapter(adapter);
        holder.spinner_status_food.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Food food1 = new Food();
                food1.setId(food.getId());
                food1.setStatusFood(Common.convertStringToStatusFood(holder.spinner_status_food.getSelectedItem().toString()));
                Observable<Boolean> updateOrder = Observable.just(foodData.updateStatus(food1));
                compositeDisposable.add(
                        updateOrder
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aBoolean -> {
                                    if (aBoolean==true){
//                                        holder.spinner_status_food.setSelection(Common.convertStatusFoodToIndex(food1.getStatusFood()));
                                        Toast.makeText(mContext, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(mContext, "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                                }, throwable -> {
                                    Toast.makeText(mContext, "Lỗi " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                })
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        @BindView(R.id.spinner_status_food)
        AppCompatSpinner spinner_status_food;
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
            contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);
        }
    }

    //    public void addFood(int position) {
//        arrayList.add(position);
//        notifyDataSetChanged();
//    }
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
