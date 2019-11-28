package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.RestaurantData;
import com.hcmunre.apporderfoodserver.models.Entity.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> categories;
    RestaurantData restaurantData=new RestaurantData();
    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, final int position) {
        final Category category = categories.get(position);
        holder.itemView.setTag(position);
        holder.txt_name_cate.setText(category.getName());
        if(category.getImage()!=null){
            byte[] decodeString = Base64.decode(category.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            holder.image_cate.setImageBitmap(bitmap);
        }


    }
    public void deleteCategory(int position) {
        boolean success=restaurantData.deleteCategory(categories.get(position).getId());
        if (success==true){
            Common.showToast(context,"Đã xóa");
        }else {
            Common.showToast(context,"Không thể xóa");
        }
        categories.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, categories.size());

    }
    public void updateCate(int position, Category category) {
        new updateCate(position,category);
        notifyItemChanged(position);
    }
    public class updateCate extends AsyncTask<String,String,Boolean>{
        private int position;
        private Category category;

        public updateCate(int position, Category category) {
            this.position = position;
            this.category = category;
            this.execute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean==true){
                Common.showToast(context,"Đã cập nhật");
            }else {
                Common.showToast(context,"không thể cập nhật");
            }
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            category.setId(categories.get(position).getId());
            boolean success=restaurantData.updateCategory(category);
            return success;
        }
    }
    @Override
    public int getItemCount() {
        return categories.size() > 0 ? categories.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        @BindView(R.id.image_cate)
        ImageView image_cate;
        @BindView(R.id.txt_name_cate)
        TextView txt_name_cate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Chọn");
            menu.add(0,0,getAdapterPosition(), Common.UPDATE);
            menu.add(0,1,getAdapterPosition(),Common.DELETE);
        }
    }
}
