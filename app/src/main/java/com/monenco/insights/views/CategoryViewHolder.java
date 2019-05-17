package com.monenco.insights.views;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monenco.insights.Graphics;
import com.monenco.insights.ImageLoader;
import com.monenco.insights.R;
import com.monenco.insights.models.Category;

public class CategoryViewHolder extends AbstractModelViewHolder<Category> implements View.OnClickListener {
    private ImageView icon;
    private TextView title;
    private View checked;
    private CategorySelectFragment fragment;

    public CategoryViewHolder(ViewGroup parent, CategorySelectFragment fragment) {
        super(parent, R.layout.category_view_layout);
        icon = (ImageView) findViewById(R.id.category_view_icon);
        title = (TextView) findViewById(R.id.category_view_name);
        checked = findViewById(R.id.category_view_checked);
        setOnClickListener(this);
        this.fragment = fragment;
    }

    @Override
    public void onModelChange() {
        ImageLoader.loadImage(icon, getModel().icon);
        title.setText(getModel().name);
        setSelected();
    }

    private void setSelected() {
        boolean selected = getModel().isFavorite;
        if (selected) {
            Graphics.colorImage(icon, R.color.color_accent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title.setTextAppearance(R.style.text_bold);
            }
            title.setTextColor(getColor(R.color.color_accent));
            checked.setVisibility(View.VISIBLE);
        } else {
            Graphics.colorImage(icon, R.color.color_icon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title.setTextAppearance(R.style.text);
            }
            title.setTextColor(getColor(R.color.color_text));
            checked.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        getModel().isFavorite = !getModel().isFavorite;
        setSelected();
        fragment.notifyChange();
    }
}
