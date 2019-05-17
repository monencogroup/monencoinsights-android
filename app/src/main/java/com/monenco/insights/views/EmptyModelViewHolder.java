package com.monenco.insights.views;

import android.view.ViewGroup;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.models.EmptyModel;
import com.monenco.insights.models.Model;

class EmptyModelViewHolder extends AbstractModelViewHolder<EmptyModel> {
    public EmptyModelViewHolder(ViewGroup parent) {
        super(parent, R.layout.empty_model_view_layout);
        ((TextView)findViewById(R.id.empty_model_view_title)).setText(getStringValue(R.string.empty_list));
    }

    @Override
    public void onModelChange() {

    }
}
