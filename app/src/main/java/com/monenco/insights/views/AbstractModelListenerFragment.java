package com.monenco.insights.views;

import android.view.View;
import android.widget.FrameLayout;

import com.monenco.insights.R;
import com.monenco.insights.models.Model;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnModelReceivedListener;
import com.monenco.insights.requester.utils.StatusCode;


public abstract class AbstractModelListenerFragment<E extends Model> extends AbstractFragment implements OnModelReceivedListener<E> {
    private LoadingViewHolder loadingViewHolder;
    private FrameLayout contentViewContainer;
    private E model;

    @Override
    public int getLayoutResId() {
        return R.layout.abstract_model_listener_fragment_layout;
    }

    public abstract int getContentLayoutResId();

    @Override
    public void initialViews() {
        loadingViewHolder = new LoadingViewHolder(findViewById(R.id.abstractModelListenerFragment_loadingView));
        contentViewContainer = (FrameLayout) findViewById(R.id.abstractModelListenerFragment_content);
        View contentView = inflate(getContentLayoutResId(), contentViewContainer);
        contentViewContainer.addView(contentView);
        initialContentViews();
    }

    public abstract void initialContentViews();

    public void reload() {
        setState(LoadingViewHolder.STATE_LOADING);
        getRootView().postDelayed(new Runnable() {
            @Override
            public void run() {
                request(getRequest());
            }
        }, 250);
    }

    private void loaded() {
        setState(LoadingViewHolder.STATE_LOADED);
        onLoaded();
        onAppear();
    }

    public abstract void onAppear();

    @Override
    public final void onShowActions() {
        if (model == null) {
            reload();
        } else {
            onAppear();
        }
    }

    public abstract void onLoaded();

    private Request<E> getRequest() {
        Request<E> request = new Request<>(Request.METHOD_GET, getRelativeUrl());
        request.setParams(getRequestParams());
        request.setModelReceivedListener(this, getModelClass());
        return request;
    }

    public abstract String getRelativeUrl();

    public abstract RequestParams getRequestParams();

    public abstract Class<E> getModelClass();

    private void setState(int state) {
        loadingViewHolder.setState(state);
        if (state == LoadingViewHolder.STATE_LOADED) {
            contentViewContainer.setVisibility(View.VISIBLE);
        } else {
            contentViewContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(int requestId, int statusCode, E receivedModel) {
        model = receivedModel;
        loaded();
    }

    @Override
    public void onFailure(int requestId, int statusCode) {
        loadingViewHolder.setOnFailedButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        if (statusCode == StatusCode.NO_INTERNET_CONNECTION) {
            setState(LoadingViewHolder.STATE_NO_INTERNET);
        } else {
            setState(LoadingViewHolder.STATE_FAILED);
        }
    }

    public E getModel() {
        return model;
    }
}
