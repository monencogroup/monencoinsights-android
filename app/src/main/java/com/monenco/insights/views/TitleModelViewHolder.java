package com.monenco.insights.views;

import android.view.ViewGroup;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.models.Model;
import com.monenco.insights.models.TitleModel;

class TitleModelViewHolder extends AbstractModelViewHolder<TitleModel> {
    public TitleModelViewHolder(ViewGroup parent) {
        super(parent, R.layout.title_view_layout);
    }

    @Override
    public void onModelChange() {
        ((TextView)getRootView()).setText(getModel().title);
    }
}
