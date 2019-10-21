package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.hcmunre.apporderfoodserver.models.Database.MenuData;
import com.hcmunre.apporderfoodserver.models.entity.Menu;
import com.hcmunre.apporderfoodserver.views.activities.ListFoodActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Menu> listMenus;

    public MenuAdapter(Context mContext, ArrayList<Menu> menus) {
        this.mContext = mContext;
        this.listMenus = menus;
    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu,parent,false);
        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position) {
        final Menu menu=listMenus.get(position);
        holder.imageMenu.setImageResource(menu.getRestaurantId());
        holder.txtNameMenu.setText(menu.getmName());
        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(mContext, ListFoodActivity.class);
            intent.putExtra(Common.KEY_MENU,menu);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listMenus.size();
    }

    public void addItem(int position, Menu menu)
    {
        listMenus.add(position, menu);
        notifyItemInserted(position);
    }
    public void itemRemoved(int position) {
        listMenus.remove(position);
        notifyItemRemoved(position);
        MenuData menuData=new MenuData();
        Toast.makeText(mContext, listMenus.get(position).getmId()+"", Toast.LENGTH_SHORT).show();
        menuData.deleteMenuRes(listMenus.get(position).getmId());

    }
    public void itemUpdate(int position,String name,int restaurantId) {
        notifyItemChanged(position);
        MenuData menuData=new MenuData();
        Menu menu=new Menu();
        menu.setmId(listMenus.get(position).getmId());
        menu.setmName(name);
        menu.setRestaurantId(restaurantId);
        menuData.updateMenu(menu);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        @BindView(R.id.imageMenu)
        ImageView imageMenu;
        @BindView(R.id.txtNameMenu)
        TextView txtNameMenu;
        public ViewHolder(@NonNull View view) {
            super(view);
           ButterKnife.bind(this,view);
           view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Chọn");
            contextMenu.add(0,0,getAdapterPosition(),Common.UPDATE);
            contextMenu.add(0,1,getAdapterPosition(),Common.DELETE);
        }
    }

}
