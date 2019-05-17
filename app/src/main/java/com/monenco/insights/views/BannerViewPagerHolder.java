package com.monenco.insights.views;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.ViewPagerAdapter;
import com.monenco.insights.models.Article;
import com.monenco.insights.models.ArticleSetModel;

import java.util.ArrayList;

public class BannerViewPagerHolder extends AbstractModelViewHolder<ArticleSetModel> implements ViewPager.OnPageChangeListener, ViewPagerAdapter.ViewHolderGenerator {
    private ViewPager viewPager;
    private LinearLayout dotsContainer;
    private ArrayList<DotView> dotViews;
    private ViewPagerAdapter<Article> adapter;

    public BannerViewPagerHolder(View rootView) {
        super(rootView);
        viewPager = (ViewPager) findViewById(R.id.banner_view_view_pager);
        dotsContainer = (LinearLayout) findViewById(R.id.banner_view_dots_container);
        dotViews = new ArrayList<>();
        adapter = new ViewPagerAdapter<>(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onModelChange() {
        adapter.clear();
        dotsContainer.removeAllViews();
        dotViews.clear();
        for (Article article : getModel().articleSet) {
            adapter.addData(article);
            DotView dotView = new DotView(dotsContainer);
            dotViews.add(dotView);
            dotsContainer.addView(dotView.getRootView());
        }
        if (getModel().articleSet.size() == 1) {
            dotsContainer.setVisibility(View.GONE);
        } else {
            dotsContainer.setVisibility(View.VISIBLE);
        }
        onPageSelected(0);
        adapter.notifyDataSetChanged();
    }

    private void hideDots() {
    }

    @Override
    public AbstractModelViewHolder generateViewHolder(ViewGroup parent) {
        return new BannerArticleViewHolder(parent);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        for (DotView dotView : dotViews) {
            dotView.setSelected(false);
        }
        dotViews.get(i).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
