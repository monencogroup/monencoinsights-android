package com.monenco.insights.views;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.monenco.insights.MultipleViewAdapter;
import com.monenco.insights.R;
import com.monenco.insights.models.Article;
import com.monenco.insights.models.ArticleSetModel;
import com.monenco.insights.models.EmptyModel;
import com.monenco.insights.models.Model;
import com.monenco.insights.models.TitleModel;
import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnModelListReceivedListener;
import com.monenco.insights.requester.utils.URLs;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends AbstractFragment implements View.OnClickListener, TextView.OnEditorActionListener, MultipleViewAdapter.ViewGenerator, OnModelListReceivedListener<Article> {
    private static final String TYPE_ARTICLE = "article";
    private static final String TYPE_TITLE = "title";
    private static final String TYPE_EMPTY = "empty";

    private String query;
    private ArrayList<Article> articles;

    private BannerViewPagerHolder banner;
    private View toolbar;
    private EditText searchView;
    private View setting;
    private MultipleViewAdapter adapter;
    @Override
    public int getLayoutResId() {
        return R.layout.search_fragment_layout;
    }

    @Override
    public void initialArguments() {


    }

    @Override
    public void initialViews() {
        banner = new BannerViewPagerHolder(findViewById(R.id.search_fragment_banner));
        if (!getMainView().getConfigs().bannerArticles.isEmpty()) {
            ArticleSetModel articleSetModel = new ArticleSetModel();
            articleSetModel.articleSet = new ArrayList<>();
            articleSetModel.articleSet.addAll(getMainView().getConfigs().bannerArticles);
            banner.setModel(articleSetModel);
        }
        toolbar = findViewById(R.id.search_fragment_toolbar);
        toolbar.setPadding(0, getMainActivity().getStatusBarHeight(), 0, 0);
        searchView = (EditText) findViewById(R.id.search_text);
        searchView.setOnEditorActionListener(this);
        searchView.setHint(getStringValue(R.string.search_hint));
        setting = findViewById(R.id.search_setting);
        setting.setOnClickListener(this);

        articles = new ArrayList<>();
        ListView list = (ListView) findViewById(R.id.search_list);
        adapter = new MultipleViewAdapter(this);
        list.setAdapter(adapter);
    }

    @Override
    public void onShowActions() {
        dataSetChanged();
        showPaymentDone();
    }
    private void showPaymentDone(){
        Bundle extras = getMainActivity().getIntent().getExtras();
        if (extras != null && extras.containsKey("article") && extras.containsKey("status")) {
            if (!getMainView().showedPaymentDonePopup) {
                getMainView().showedPaymentDonePopup = true;
                int status = extras.getInt("status", 0);
                int articleId = extras.getInt("article", 0);
                if (status == 1) {
                    Toast.makeText(getContext(),getStringValue(R.string.payment_done),Toast.LENGTH_SHORT).show();
                    pushFragment(ArticleFragment.createInstance(articleId));
                } else {
                   Toast.makeText(getContext(),getStringValue(R.string.payment_failed),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == setting) {
            pushFragment(new SettingFragment());
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String query = searchView.getText().toString();
            searchForQuery(query);
            return true;
        }
        return false;
    }

    private void searchForQuery(String query) {
        setLoading(true);
        this.query = query;
        RequestParams params = new RequestParams();
        params.put("lang", Translator.getLang());
        params.put("query", query);
        Request<Article> articlesRequest = new Request<>(Request.METHOD_GET, URLs.ARTICLE + URLs.SEARCH);
        articlesRequest.setParams(params);
        articlesRequest.setModelListReceivedListener(this, Article.class);
        request(articlesRequest);
    }

    private void setLoading(boolean loading) {
        searchView.setEnabled(!loading);
    }


    @Override
    public void onSuccess(int requestId, int statusCode, List<Article> receivedList) {
        setLoading(false);
        articles.clear();
        articles.addAll(receivedList);
        dataSetChanged();
    }

    private void dataSetChanged() {
        adapter.clear();
        if (query != null) {
            adapter.addData(new TitleModel(getStringValue(R.string.search_result) + " " + query), TYPE_TITLE);
        }
        if (articles.isEmpty()) {
            if (query != null) {
                adapter.addData(new EmptyModel(), TYPE_EMPTY);
            }
            adapter.addData(new TitleModel(getStringValue(R.string.new_articles)), TYPE_TITLE);
            if (!getMainView().getConfigs().newArticles.isEmpty()) {
                for (Article article : getMainView().getConfigs().newArticles) {
                    adapter.addData(article, TYPE_ARTICLE);
                }
            }
        } else {
            for (Article article : articles) {
                adapter.addData(article, TYPE_ARTICLE);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(int requestId, int statusCode) {
        setLoading(false);
        Toast.makeText(getContext(), getStringValue(R.string.connection_failed_please_try_again), Toast.LENGTH_LONG).show();
    }

    @Override
    public AbstractModelViewHolder<? extends Model> generateViewHolder(ViewGroup parent, String typeKey) {
        switch (typeKey) {
            case TYPE_TITLE:
                return new TitleModelViewHolder(parent);
            case TYPE_ARTICLE:
                return new ArticleViewHolder(parent, adapter);
            case TYPE_EMPTY:
                return new EmptyModelViewHolder(parent);
            default:
                return null;
        }
    }

}
