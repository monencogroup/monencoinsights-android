package com.monenco.insights.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.monenco.insights.MainActivity;
import com.monenco.insights.MainView;
import com.monenco.insights.requester.Request;


public interface ViewController {
    View getRootView();

    int getColor(int colorResId);

    Resources getRes();

    Drawable getDrawable(int drawableResId);

    String getStringValue(int stringResId);

    float getDimen(int dimenResId);

    View findViewById(int viewResId);

    MainActivity getMainActivity();

    Context getContext();

    MainView getMainView();

    void pushFragment(AbstractFragment fragment);

    void pushFragment(AbstractFragment fragment, int animationType);

    View inflate(int layoutResId, ViewGroup parent);

    void destroy();

    void popFragment();

    void popFragment(boolean shouldReloadBehind);

    void request(Request request);

}
