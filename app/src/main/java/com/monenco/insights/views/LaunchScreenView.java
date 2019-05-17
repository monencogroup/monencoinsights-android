package com.monenco.insights.views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monenco.insights.MainView;
import com.monenco.insights.R;


public class LaunchScreenView extends AbstractViewHolder {
    private View loadingView;
    private View failedView;

    private TextView title;
    private TextView subTitle;
    private ButtonView button;

    public LaunchScreenView(View rootView) {
        super(rootView);
        TextView appName = (TextView) findViewById(R.id.launch_screen_loading_view_app_name);
        appName.setText(getStringValue(R.string.app_name));
        loadingView = findViewById(R.id.launch_screen_loading_view);
        failedView = findViewById(R.id.launch_screen_failed_view);
        title = (TextView) findViewById(R.id.launch_screen_failed_view_title);
        subTitle = (TextView) findViewById(R.id.launch_screen_failed_view_sub_title);
        button = new ButtonView(findViewById(R.id.launch_screen_failed_view_button),R.string.try_again);
    }

    public void setState(int state) {
        switch (state) {
            case MainView.STATE_DONE:
                setVisible(false);
                break;
            case MainView.STATE_LOADING:
                setVisible(true);
                loadingView.setVisibility(View.VISIBLE);
                failedView.setVisibility(View.GONE);
                break;
            case MainView.STATE_FAILED:
                setVisible(true);
                loadingView.setVisibility(View.GONE);
                failedView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setFailedView(int titleResId, int subTitleResId, int buttonResId, View.OnClickListener onClickListener) {
        title.setText(getStringValue(titleResId));
        subTitle.setText(getStringValue(subTitleResId));
        button.setText(buttonResId);
        button.setOnClickListener(onClickListener);

    }

    @Override
    public void destroy() {
        super.destroy();
        loadingView = null;
        failedView = null;
        title = null;
        subTitle = null;
        button = null;
    }
}
