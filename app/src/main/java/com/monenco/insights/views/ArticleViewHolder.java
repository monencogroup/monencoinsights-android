package com.monenco.insights.views;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monenco.insights.Calander;
import com.monenco.insights.ImageLoader;
import com.monenco.insights.MultipleViewAdapter;
import com.monenco.insights.R;
import com.monenco.insights.models.Article;
import com.monenco.insights.models.Tag;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnResponseReceivedListener;
import com.monenco.insights.requester.utils.URLs;

public class ArticleViewHolder extends AbstractModelViewHolder<Article> implements View.OnClickListener {
    private ImageView image;
    private TextView title;
    private TextView date;
    private TextView lead;
    private IconTitleButtonView read;
    private IconTitleButtonView bookmark;
    private IconTitleButtonView share;
    private LinearLayout tagsContainer;
    private MultipleViewAdapter adapter;

    public ArticleViewHolder(ViewGroup parent, MultipleViewAdapter adapter) {
        super(parent, R.layout.article_view_layout);
        this.adapter = adapter;
        init();
    }

    public ArticleViewHolder(ViewGroup parent) {
        super(parent, R.layout.article_view_layout);
        init();
        bookmark.setVisible(false);
        findViewById(R.id.article_view_bookmarked_line).setVisibility(View.GONE);
    }

    private void init() {
        image = (ImageView) findViewById(R.id.article_view_image);
        title = (TextView) findViewById(R.id.article_view_title);
        date = (TextView) findViewById(R.id.article_view_date);
        lead = (TextView) findViewById(R.id.article_view_lead);
        read = new IconTitleButtonView(findViewById(R.id.article_view_read_more), R.drawable.ic_read, R.string.read);
        bookmark = new IconTitleButtonView(findViewById(R.id.article_view_bookmarked), R.drawable.ic_bookmark, R.string.bookmark);
        share = new IconTitleButtonView(findViewById(R.id.article_view_share), R.drawable.ic_share, R.string.share);
        tagsContainer = (LinearLayout) findViewById(R.id.article_view_tags_container);
        getRootView().setOnClickListener(this);
        read.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onModelChange() {
        ImageLoader.loadImage(image, getModel().image);
        title.setText(getModel().title);
        Log.e("DATE IS ",getModel().creationDate);
        date.setText(Calander.getDate(getModel().creationDate, true));
        lead.setText(getModel().leadText);
        if (getModel().isBookmarked) {
            bookmark.setIcon(R.drawable.ic_bookmark_checked);
            bookmark.setTextColor(R.color.color_primary);
        } else {
            bookmark.setIcon(R.drawable.ic_bookmark);
            bookmark.setTextColor(R.color.color_text);
        }
        tagsContainer.removeAllViews();
        for (Tag tag : getModel().tags) {
            TagViewHolder tagViewHolder = new TagViewHolder(tagsContainer);
            tagViewHolder.setModel(tag);
            tagsContainer.addView(tagViewHolder.getRootView());
        }
        setBookmarkLoadingState();
    }

    @Override
    public void onClick(View view) {
        if (view == getRootView() || view == read.getRootView()) {
            pushFragment(ArticleFragment.createInstance(getModel().id));
        } else if (view == bookmark.getRootView()) {
            toggleBookmark();
        } else if (view == share.getRootView()) {
            share();
        }
    }

    private void toggleBookmark() {
        Article article = getModel();
        if (!article.bookmarkLoading) {
            article.bookmarkLoading = true;
            Request request = new Request(Request.METHOD_POST, URLs.ARTICLE + URLs.BOOKMARK + URLs.TOGGLE);
            RequestParams params = new RequestParams();
            params.put("id", article.id);
            params.put("newState", !article.isBookmarked);
            request.setParams(params);
            OnResponseReceivedListener listener = new BookmarkToggleResponseListener(article, this);
            request.setResponseReceivedListener(listener);
            request(request);
            notifyDataSetChanged();
        }
    }

    private void setBookmarkLoadingState() {
        bookmark.setLoading(getModel().bookmarkLoading);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    private void share() {
        getMainActivity().share(getModel().title, getModel().leadText);
    }
}
