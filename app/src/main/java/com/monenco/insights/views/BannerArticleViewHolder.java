package com.monenco.insights.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.models.Article;

class BannerArticleViewHolder extends AbstractModelViewHolder<Article> implements View.OnClickListener {
    private ImageViewHolder image;
    private TextView title;
    private TextView lead;

    public BannerArticleViewHolder(ViewGroup parent) {
        super(parent, R.layout.banner_article_view_holder);
        image = new ImageViewHolder(findViewById(R.id.article_view_image), R.color.color_primary);
        title = (TextView) findViewById(R.id.article_view_title);
        lead = (TextView) findViewById(R.id.article_view_lead);
        setOnClickListener(this);
    }

    @Override
    public void onModelChange() {
        image.setImage(getModel().image);
        title.setText(getModel().title);
        lead.setText(getModel().leadText);
    }

    @Override
    public void onClick(View view) {
        pushFragment(ArticleFragment.createInstance(getModel().id));
    }
}
