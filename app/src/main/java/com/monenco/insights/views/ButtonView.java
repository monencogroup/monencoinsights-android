package com.monenco.insights.views;

import android.view.View;
import android.widget.TextView;

import com.monenco.insights.R;

public class ButtonView extends AbstractViewHolder implements View.OnClickListener {
    private String text;
    private View.OnClickListener onClickListener;
    private boolean enable;
    private boolean loading;

    public ButtonView(View rootView, int textResId) {
        super(rootView);
        enable = true;
        loading = false;
        this.setText(textResId);
        getRootView().setOnClickListener(this);

    }

    public void setText(int textResId) {
        this.text = getStringValue(textResId);
        setText();
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        setText();
    }

    private void setText() {
        if (this.loading) {
            ((TextView) getRootView()).setText(getStringValue(R.string.loading));
        } else {
            ((TextView) getRootView()).setText(text);
        }
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        getRootView().setClickable(enable);

        if (enable) {
            getRootView().setAlpha(1);
        } else {
            getRootView().setAlpha(0.4f);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View view) {
        if (enable && !loading && onClickListener != null) {
            onClickListener.onClick(view);
        }

    }
}
