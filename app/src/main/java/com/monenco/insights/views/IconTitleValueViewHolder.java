package com.monenco.insights.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.monenco.insights.R;

class IconTitleValueViewHolder extends AbstractViewHolder {
    private TextView value;

    public IconTitleValueViewHolder(View rootView, int iconResId, int titleResId) {
        super(rootView);
        ImageView icon = (ImageView) findViewById(R.id.icon_title_value_icon);
        icon.setImageResource(iconResId);
        TextView title = (TextView) findViewById(R.id.icon_title_value_title);
        title.setText(getStringValue(titleResId));
        value = (TextView) findViewById(R.id.icon_title_value_value);
    }

    public void setValue(String value) {
        this.value.setText(value);
    }

    public void setValueColor(int colorResId) {
        value.setTextColor(getColor(colorResId));
    }
}
