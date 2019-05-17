package com.monenco.insights.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monenco.insights.Calander;
import com.monenco.insights.MainActivity;
import com.monenco.insights.R;
import com.monenco.insights.Settings;
import com.monenco.insights.StringHelper;
import com.monenco.insights.models.Article;
import com.monenco.insights.models.ArticlePart;
import com.monenco.insights.models.BankLink;
import com.monenco.insights.models.Tag;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnModelReceivedListener;
import com.monenco.insights.requester.listeners.OnResponseReceivedListener;
import com.monenco.insights.requester.utils.URLs;

import java.util.ArrayList;
import java.util.Objects;

public class ArticleFragment extends AbstractModelListenerFragment<Article> implements View.OnClickListener, MainActivity.PurchaseListener {
    private static final String ARTICLE_ID = "ID";
    private int id;
    private Toolbar toolbar;
    private ImageViewHolder image;
    private IconTitleButtonView font;
    private IconTitleButtonView bookmark;
    private IconTitleButtonView share;
    private ButtonView purchase;

    private TextView lead;
    private TextView titleInner;
    private IconTitleViewHolder category;
    private IconTitleViewHolder author;
    private IconTitleViewHolder date;
    private IconTitleValueViewHolder price;
    private View actions;
    private LinearLayout tags;
    private ArrayList<TagViewHolder> tagsViews;
    private LinearLayout partsContainer;
    private ArrayList<ArticlePartViewHolder> articleParts;

    public static ArticleFragment createInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARTICLE_ID, id);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.article_fragment_layout;
    }

    @Override
    public void initialContentViews() {
        articleParts = new ArrayList<>();
        tagsViews = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.article_fragment_title);
        getMainActivity().setSupportActionBar(toolbar);
        Objects.requireNonNull(getMainActivity().getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getMainActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        image = new ImageViewHolder(findViewById(R.id.article_fragment_image), R.color.color_primary_light);
        font = new IconTitleButtonView(findViewById(R.id.article_fragment_font), R.drawable.ic_font, R.string.font);
        bookmark = new IconTitleButtonView(findViewById(R.id.article_fragment_bookmark), R.drawable.ic_bookmark, R.string.bookmark);
        share = new IconTitleButtonView(findViewById(R.id.article_fragment_share), R.drawable.ic_share, R.string.share);
        font.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        share.setOnClickListener(this);
        purchase = new ButtonView(findViewById(R.id.article_fragment_purchase), R.string.purchase);
        purchase.setOnClickListener(this);
        actions = findViewById(R.id.article_fragment_actions);
        lead = (TextView) findViewById(R.id.article_fragment_lead);
        titleInner = (TextView) findViewById(R.id.article_fragment_title_inner);
        category = new IconTitleViewHolder(findViewById(R.id.article_fragment_category), R.drawable.ic_category);
        author = new IconTitleViewHolder(findViewById(R.id.article_fragment_author), R.drawable.ic_author);
        date = new IconTitleViewHolder(findViewById(R.id.article_fragment_date), R.drawable.ic_date);
        price = new IconTitleValueViewHolder(findViewById(R.id.article_fragment_price), R.drawable.ic_price, R.string.price);

        tags = (LinearLayout) findViewById(R.id.article_fragment_tags);
        partsContainer = (LinearLayout) findViewById(R.id.article_fragment_parts_container);
    }

    @Override
    public void onAppear() {
        toolbar.setTitle("");
        image.setImage(getModel().image);
        lead.setText(getModel().leadText);
        titleInner.setText(getModel().title);
        category.setImage(getModel().category.icon);
        category.setTitle(getModel().category.name);
        author.setTitle(getModel().author.name);
        date.setTitle(Calander.getDate(getModel().creationDate, false));
        if (getModel().purchasable) {
            String value = StringHelper.getPrice(getModel().price);
            value += " " + getStringValue(R.string.tooman);
            if (getModel().purchased) {
                price.setValueColor(R.color.color_accent);
                value += " (" + getStringValue(R.string.purchased) + ")";
            } else {
                price.setValueColor(R.color.color_primary);
            }
            price.setValue(value);
        } else {
            price.setValue(getStringValue(R.string.free_price));
            price.setValueColor(R.color.color_accent);
        }
        if (getModel().purchasable && !getModel().purchased) {
            actions.setVisibility(View.GONE);
            purchase.setVisible(true);
        } else {
            actions.setVisibility(View.VISIBLE);
            purchase.setVisible(false);

        }
        for (Tag tag : getModel().tags) {
            TagViewHolder tagViewHolder = new TagViewHolder(tags);
            tagViewHolder.setModel(tag);
            tags.addView(tagViewHolder.getRootView());
            tagsViews.add(tagViewHolder);
        }
        if (getModel().parts != null) {
            for (ArticlePart articlePart : getModel().parts) {
                ArticlePartViewHolder articlePartViewHolder = new ArticlePartViewHolder(partsContainer);
                articlePartViewHolder.setModel(articlePart);
                partsContainer.addView(articlePartViewHolder.getRootView());
                articleParts.add(articlePartViewHolder);
            }
        }
        setBookmark();
    }

    @Override
    public void onLoaded() {

    }

    @Override
    public String getRelativeUrl() {
        return URLs.ARTICLE;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        params.put("id", id);
        return params;
    }

    @Override
    public Class getModelClass() {
        return Article.class;
    }

    @Override
    public void initialArguments() {
        Bundle args = getArguments();
        assert args != null;
        this.id = args.getInt(ARTICLE_ID);
    }

    @Override
    public void onClick(View view) {
        if (view == bookmark.getRootView()) {
            toggleBookmark();
        } else if (view == share.getRootView()) {
            share();
        } else if (view == font.getRootView()) {
            isLarge = !isLarge;
            if (isLarge) {
                titleInner.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_xxl));
                lead.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_xl));
            } else {
                titleInner.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_l));
                lead.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_m));
            }
            for (ArticlePartViewHolder articlePartViewHolder : articleParts) {
                articlePartViewHolder.setIsLarge(isLarge);
            }
            for (TagViewHolder tagViewHolder : tagsViews) {
                tagViewHolder.setIsLarge(isLarge);
            }
            category.setIsLarge(isLarge);
            author.setIsLarge(isLarge);
            date.setIsLarge(isLarge);

        } else if (view == purchase.getRootView()) {
            purchase.setLoading(true);
            if (Settings.CAFE_BAZAR) {
                boolean success = getMainActivity().purchace(getModel().cafeBazarPaymentId, this);
                if (!success) {
                    purchase.setLoading(false);
                    Toast.makeText(getContext(),getStringValue(R.string.payment_failed),Toast.LENGTH_SHORT).show();
                }
            } else {
                Request purchaseRequest = new Request(Request.METHOD_POST, URLs.ARTICLE + URLs.PURCHASE);
                RequestParams params = new RequestParams();
                params.put("platform", "Android");
                params.put("articleID", getModel().id);
                purchaseRequest.setParams(params);
                purchaseRequest.setModelReceivedListener(new PurchaseArticleListener(), BankLink.class);
                request(purchaseRequest);
            }
        }
    }

    private boolean isLarge;

    private void toggleBookmark() {
        Article article = getModel();
        if (!article.bookmarkLoading) {
            article.bookmarkLoading = true;
            setBookmark();
            Request request = new Request(Request.METHOD_POST, URLs.ARTICLE + URLs.BOOKMARK + URLs.TOGGLE);
            RequestParams params = new RequestParams();
            params.put("id", article.id);
            params.put("newState", !article.isBookmarked);
            request.setParams(params);
            OnResponseReceivedListener listener = new BookmarkToggleResponseListener(article, this);
            request.setResponseReceivedListener(listener);
            request(request);
        }
    }

    public void setBookmark() {
        if (getModel().isBookmarked) {
            bookmark.setIcon(R.drawable.ic_bookmark_checked);
            bookmark.setTextColor(R.color.color_primary);
        } else {
            bookmark.setIcon(R.drawable.ic_bookmark);
            bookmark.setTextColor(R.color.color_text);
        }
        bookmark.setLoading(getModel().bookmarkLoading);

    }

    private void share() {
        getMainActivity().share(getModel().title, getModel().leadText);
    }

    @Override
    public void onFinish(String paymentId, String paymentToken, boolean success) {
        if (success) {
            Request request = new Request(Request.METHOD_POST, URLs.ARTICLE + URLs.PAYMENT + URLs.CHECK);
            RequestParams params = new RequestParams();
            params.put("paymentId", paymentId);
            params.put("paymentToken", paymentToken);
            request.setParams(params);
            OnResponseReceivedListener listener = new PaymentCheckListener();
            request.setResponseReceivedListener(listener);
            request(request);
        } else {
            purchase.setLoading(false);
            Toast.makeText(getContext(),getStringValue(R.string.payment_failed),Toast.LENGTH_SHORT).show();
        }
    }

    private class PurchaseArticleListener implements OnModelReceivedListener<BankLink> {

        @Override
        public void onSuccess(int requestId, int statusCode, BankLink received) {
            purchase.setLoading(false);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(received.url));
            startActivity(i);
            getMainActivity().finish();
        }

        @Override
        public void onFailure(int requestId, int statusCode) {
            purchase.setLoading(false);
            Toast.makeText(getContext(), getStringValue(R.string.connection_failed_please_try_again), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean isAlive() {
            return ArticleFragment.this.isAlive();
        }
    }

    private class PaymentCheckListener implements OnResponseReceivedListener {

        @Override
        public void onSuccess(int requestId, int statusCode) {
            purchase.setLoading(false);
            Toast.makeText(getContext(),getStringValue(R.string.payment_done),Toast.LENGTH_SHORT).show();
            reload();
        }

        @Override
        public void onFailure(int requestId, int statusCode) {
            purchase.setLoading(false);
            Toast.makeText(getContext(),getStringValue(R.string.payment_failed),Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean isAlive() {
            return ArticleFragment.this.isAlive();
        }
    }
}
