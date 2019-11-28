package com.hcmunre.apporderfoodserver.views.holders;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.models.Entity.MenuFoodRestaurant;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class MenuFoodViewHolder extends GroupViewHolder {
    private TextView mTextView,mTextCount;
    private ImageView imageView;
    public MenuFoodViewHolder(View itemView) {
        super(itemView);
        mTextView=itemView.findViewById(R.id.textview1);
        imageView=itemView.findViewById(R.id.img_row);
    }
    public void bind(MenuFoodRestaurant menuFoodRestaurant){
        mTextView.setText(menuFoodRestaurant.getTitle());
    }
    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageView.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        imageView.setAnimation(rotate);
    }
}
