package com.monenco.insights;

import android.widget.ImageView;

public class Graphics {
    public static void colorImage(ImageView imageView,int colorResId){
        imageView.setColorFilter(imageView.getContext().getResources().getColor(colorResId));

    }
}
