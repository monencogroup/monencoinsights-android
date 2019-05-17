package com.monenco.insights.views;

import android.view.View;
import android.view.ViewGroup;

import com.monenco.insights.models.Model;


public abstract class AbstractModelViewHolder<E extends Model> extends AbstractViewHolder {
    private E model;

    public AbstractModelViewHolder(ViewGroup parent, int layoutResId) {
        super(parent, layoutResId);
    }

    public AbstractModelViewHolder(View view) {
        super(view);
    }


    public abstract void onModelChange();

    public void setModel(Model model) {
        this.model = (E)model;
        onModelChange();
    }

    public E getModel() {
        return model;
    }
}
