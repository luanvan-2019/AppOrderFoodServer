package com.hcmunre.apporderfoodserver.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telecom.StatusHints;
import android.util.Base64;
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
import androidx.annotation.Nullable;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<Shipper> {
    private Context context;
    private ArrayList<Shipper> shippers;
    private Spinner spinner;
    public SpinnerAdapter(Context context, ArrayList<Shipper> shippers, Spinner spinner) {
        super(context,0,shippers);
        this.context=context;
        this.shippers=shippers;
        this.spinner=spinner;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    public View initView(int position,View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_spinner,parent,false);
        }
        TextView txt_name_shipper=convertView.findViewById(R.id.txt_name_shipper);
        TextView txt_phone=convertView.findViewById(R.id.txt_phone);
        ImageView image_call=convertView.findViewById(R.id.image_call);
        Shipper currentShipper=getItem(position);
        if(currentShipper!=null){
            txt_name_shipper.setText(currentShipper.getName());
            txt_phone.setText(currentShipper.getPhone());
        }
        Common.currentShiper=shippers.get(position);
        return convertView;
    }
}
