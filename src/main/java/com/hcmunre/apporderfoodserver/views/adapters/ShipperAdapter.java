package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipperAdapter extends RecyclerView.Adapter<ShipperAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Shipper> shippers;

    public ShipperAdapter(Context context, ArrayList<Shipper> shippers) {
        this.context = context;
        this.shippers = shippers;
    }

    @NonNull
    @Override
    public ShipperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shipper,null);
        return new ShipperAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipperAdapter.ViewHolder holder, int position) {
        Shipper shipper=shippers.get(position);
        if(shipper.getImage()!=null){
            byte[] decodeString = Base64.decode(shipper.getImage(), Base64.DEFAULT);
            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
            holder.img_shipper.setImageBitmap(decodebitmap);
        }
        holder.txt_name.setText(shipper.getName());
        holder.txt_address.setText(shipper.getAddress());
        holder.txt_phone.setText(shipper.getPhone());
    }

    @Override
    public int getItemCount() {
        return shippers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_shipper)
        ImageView img_shipper;
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_address)
        TextView txt_address;
        @BindView(R.id.txt_phone)
        TextView txt_phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
