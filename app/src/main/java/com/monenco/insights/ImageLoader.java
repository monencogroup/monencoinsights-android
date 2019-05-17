package com.monenco.insights;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageLoader {
    public static void loadImage(ImageView imageView, String url) {
        if (StringHelper.notNone(url)) {
            Picasso.with(imageView.getContext()).load(url).into(imageView);
        }

    }
}
