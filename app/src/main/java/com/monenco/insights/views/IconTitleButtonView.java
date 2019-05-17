package com.monenco.insights.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.monenco.insights.R;

public class IconTitleButtonView extends AbstractViewHolder {
    private ImageView icon;
    private TextView title;

    public IconTitleButtonView(View rootView, int iconResId, int titleResId) {
        super(rootView);
        icon = (ImageView) findViewById(R.id.icon_title_button_icon);
        title = (TextView) findViewById(R.id.icon_title_button_title);
        setIcon(iconResId);
        title.setText(getStringValue(titleResId));
    }

    public void setIcon(int iconResId) {
        icon.setImageResource(iconResId);
    }

    public void setTextColor(int colorResId) {
        title.setTextColor(getColor(colorResId));
    }

    public void setLoading(boolean bookmarkLoading) {
        if(bookmarkLoading){
            getRootView().setAlpha(0.2f);
        }else {
            getRootView().setAlpha(1);
        }
    }
}
