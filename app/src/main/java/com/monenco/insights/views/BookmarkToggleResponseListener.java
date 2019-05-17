package com.monenco.insights.views;

import android.widget.Toast;

import com.monenco.insights.R;
import com.monenco.insights.models.Article;
import com.monenco.insights.requester.listeners.OnResponseReceivedListener;

public class BookmarkToggleResponseListener implements OnResponseReceivedListener {
    private Article article;
    private ArticleViewHolder viewHolder;
    private ArticleFragment fragment;

    public BookmarkToggleResponseListener(Article article, ArticleViewHolder viewHolder) {
        this.article = article;
        this.viewHolder = viewHolder;
    }

    public BookmarkToggleResponseListener(Article article, ArticleFragment fragment) {
        this.article = article;
        this.fragment = fragment;
    }

    @Override
    public void onSuccess(int requestId, int statusCode) {
        article.bookmarkLoading = false;
        article.isBookmarked = !article.isBookmarked;
        if (viewHolder != null) {
            viewHolder.notifyDataSetChanged();
        }
        if (fragment != null) {
            fragment.setBookmark();
        }
    }

    @Override
    public void onFailure(int requestId, int statusCode) {
        article.bookmarkLoading = false;
        if (viewHolder != null) {
            Toast.makeText(viewHolder.getContext(), viewHolder.getStringValue(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            viewHolder.notifyDataSetChanged();
        }
        if (fragment != null) {
            Toast.makeText(fragment.getContext(), fragment.getStringValue(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            fragment.setBookmark();
        }
    }

    @Override
    public boolean isAlive() {
        if (viewHolder != null) {
            return viewHolder.isAlive();
        } else if (fragment != null) {
            return fragment.isAlive();
        }
        return false;
    }
}
