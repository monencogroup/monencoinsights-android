package com.monenco.insights.views;

import android.view.ViewGroup;

import com.monenco.insights.R;

public class DotView extends AbstractViewHolder {
    public DotView(ViewGroup parent) {
        super(parent, R.layout.dot_view_layout);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            getRootView().setAlpha(1);
        } else {
            getRootView().setAlpha(0.3f);
        }
    }
}
