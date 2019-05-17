package com.monenco.insights.views;

import android.view.View;
import android.widget.TextView;

import com.monenco.insights.R;

public class EmptyViewHolder extends AbstractViewHolder {
    public EmptyViewHolder(View rootView) {
        super(rootView);
        ((TextView)findViewById(R.id.empty_view_title)).setText(getStringValue(R.string.empty_list));
    }
}
