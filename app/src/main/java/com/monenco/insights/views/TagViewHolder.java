package com.monenco.insights.views;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.models.Tag;

public class TagViewHolder extends AbstractModelViewHolder<Tag> {
    public TagViewHolder(ViewGroup parent) {
        super(parent, R.layout.tag_view_layout);
    }

    @Override
    public void onModelChange() {
        ((TextView) getRootView()).setText(getModel().name);
    }

    public void setIsLarge(boolean isLarge) {
        if (isLarge) {
            ((TextView) getRootView()).setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_m));
        } else {
            ((TextView) getRootView()).setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_s));
        }
    }
}
