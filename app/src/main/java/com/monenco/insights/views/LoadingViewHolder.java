package com.monenco.insights.views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monenco.insights.R;


public class LoadingViewHolder extends AbstractViewHolder implements View.OnClickListener {
    public static final int STATE_LOADED = 0;
    public static final int STATE_NO_INTERNET = 1;
    public static final int STATE_FAILED = 2;
    public static final int STATE_LOADING = 3;

    private View loadingView;
    private View failedView;
    private TextView failedSubTitle;
    private View.OnClickListener onFailedButtonClickListener;

    public LoadingViewHolder(View rootView) {
        super(rootView);
        loadingView = findViewById(R.id.loading_view_loading);
        TextView loadingTitle = (TextView) findViewById(R.id.loading_view_loading_title);
        loadingTitle.setText(getStringValue(R.string.loading));
        failedView = findViewById(R.id.loading_view_failed);
        TextView failedTitle = (TextView) findViewById(R.id.loading_view_failed_title);
        failedTitle.setText(getStringValue(R.string.connection_failed));
        failedSubTitle = (TextView) findViewById(R.id.loading_view_failed_sub_title);
        ButtonView failedButton = new ButtonView(findViewById(R.id.loading_view_failed_button),R.string.try_again);
        failedButton.setOnClickListener(this);
    }

    public void setOnFailedButtonClickListener(View.OnClickListener listener) {
        this.onFailedButtonClickListener = listener;
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADED:
                setVisible(false);
                break;
            case STATE_LOADING:
                setVisible(true);
                loadingView.setVisibility(View.VISIBLE);
                failedView.setVisibility(View.GONE);
                break;
            case STATE_FAILED:
                setVisible(true);
                loadingView.setVisibility(View.GONE);
                failedView.setVisibility(View.VISIBLE);
                failedSubTitle.setText(getStringValue(R.string.connection_failed_please_try_again));
                break;
            case STATE_NO_INTERNET:
                setVisible(true);
                loadingView.setVisibility(View.GONE);
                failedView.setVisibility(View.VISIBLE);
                failedSubTitle.setText(getStringValue(R.string.no_internet_please_try_again));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (onFailedButtonClickListener != null) {
            onFailedButtonClickListener.onClick(this.getRootView());
        }
    }
}
