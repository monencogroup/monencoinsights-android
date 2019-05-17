package com.monenco.insights.views;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.monenco.insights.R;

public class IconTitleViewHolder extends AbstractViewHolder {
    private TextView title;
    private ImageViewHolder image;

    public IconTitleViewHolder(View rootView, int iconResId) {
        super(rootView);
        ImageView icon = (ImageView) findViewById(R.id.icon_title_icon);
        icon.setImageResource(iconResId);
        image = new ImageViewHolder(icon, iconResId);
        title = (TextView) findViewById(R.id.icon_title_title);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setImage(String url) {
        image.setImage(url);
    }

    public void setIsLarge(boolean isLarge) {
        if (isLarge) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_l));
        } else {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_m));
        }
    }
}
