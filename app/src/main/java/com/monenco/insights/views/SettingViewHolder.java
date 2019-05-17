package com.monenco.insights.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.monenco.insights.R;

public class SettingViewHolder extends AbstractViewHolder {
    public SettingViewHolder(View rootView, int titleResId, int iconResId, boolean isLast) {
        super(rootView);
        ImageView icon = (ImageView) findViewById(R.id.setting_view_icon);
        TextView title = (TextView) findViewById(R.id.setting_view_title);
        View border = findViewById(R.id.setting_view_border);
        icon.setImageResource(iconResId);
        title.setText(getStringValue(titleResId));
        if (isLast) {
            border.setVisibility(View.GONE);
        }
    }
    public SettingViewHolder(View rootView, String titleText, int iconResId, boolean isLast) {
        super(rootView);
        ImageView icon = (ImageView) findViewById(R.id.setting_view_icon);
        TextView title = (TextView) findViewById(R.id.setting_view_title);
        View border = findViewById(R.id.setting_view_border);
        icon.setImageResource(iconResId);
        title.setText(titleText);
        if (isLast) {
            border.setVisibility(View.GONE);
        }
    }
}
