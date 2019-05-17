package com.monenco.insights.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.models.Article;
import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnModelListReceivedListener;
import com.monenco.insights.requester.utils.URLs;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends AbstractFragment implements OnModelListReceivedListener<Article>, View.OnClickListener {
    private ArrayList<Article> articles;
    private ListView list;
    private ArticleListAdapter adapter;
    private EmptyViewHolder empty;
    private LoadingViewHolder loadingViewHolder;

    @Override
    public int getLayoutResId() {
        return R.layout.bookmarks_fragment_layout;
    }

    @Override
    public void initialArguments() {

    }

    @Override
    public void initialViews() {
        articles = new ArrayList<>();
        View back = findViewById(R.id.bookmarks_fragment_back);
        if(Translator.isPersian()){
            back.setScaleX(-1);
        }
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.bookmarks_fragment_title);
        title.setText(getStringValue(R.string.bookmarks));
        list = (ListView) findViewById(R.id.bookmarks_fragment_list);
        adapter = new ArticleListAdapter(getContext(), R.layout.article_view_layout);
        list.setAdapter(adapter);
        empty = new EmptyViewHolder(findViewById(R.id.bookmarks_fragment_empty));
        loadingViewHolder = new LoadingViewHolder(findViewById(R.id.bookmarks_fragment_loading));
        loadingViewHolder.setOnFailedButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetch();
            }
        });
        fetch();
    }

    private void fetch() {
        setState(LoadingViewHolder.STATE_LOADING);
        Request<Article> request = new Request<>(Request.METHOD_GET, URLs.ARTICLE + URLs.BOOKMARK + URLs.LIST);
        RequestParams params = new RequestParams();
        params.put("lang", Translator.getLang());
        request.setParams(params);
        request.setModelListReceivedListener(this, Article.class);
        request(request);
    }

    private void setState(int state) {
        loadingViewHolder.setState(state);
        switch (state) {
            case LoadingViewHolder.STATE_FAILED:
            case LoadingViewHolder.STATE_LOADING:
            case LoadingViewHolder.STATE_NO_INTERNET:
                list.setVisibility(View.GONE);
                empty.setVisible(false);
                break;
            case LoadingViewHolder.STATE_LOADED:
                empty.setVisible(articles.isEmpty());
                if (articles.isEmpty()) {
                    list.setVisibility(View.GONE);
                } else {
                    list.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
        }
    }

    @Override
    public void onSuccess(int requestId, int statusCode, List<Article> receivedList) {
        articles.clear();
        articles.addAll(receivedList);
        setState(LoadingViewHolder.STATE_LOADED);
    }

    @Override
    public void onFailure(int requestId, int statusCode) {
        setState(LoadingViewHolder.STATE_FAILED);
    }

    @Override
    public void onClick(View view) {
        popFragment();
    }

    private class ArticleListAdapter extends ArrayAdapter<Article> {

        ArticleListAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Article getItem(int position) {
            return articles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ArticleViewHolder holder;
            if (convertView == null || convertView.getTag(R.id.view_0) == null) {
                holder = new ArticleViewHolder(parent);
                convertView = holder.getRootView();
                convertView.setTag(R.id.view_0, holder);
            } else {
                holder = (ArticleViewHolder) convertView.getTag(R.id.view_0);
            }
            holder.setModel(getItem(position));
            return convertView;
        }
    }
}
