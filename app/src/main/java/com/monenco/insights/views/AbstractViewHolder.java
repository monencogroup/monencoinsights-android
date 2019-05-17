package com.monenco.insights.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monenco.insights.MainActivity;
import com.monenco.insights.MainView;
import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;

public abstract class AbstractViewHolder implements ViewController {
    private View rootView;
    private LayoutInflater inflater;
    private Context context;
    private boolean alive = true;


    public AbstractViewHolder(View rootView) {
        this.rootView = rootView;
        this.context = rootView.getContext();
        getMainActivity().setupUI(this.rootView);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AbstractViewHolder(ViewGroup parent, int layoutID) {
        this.context = parent.getContext();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rootView = inflate(layoutID, parent);
        getMainActivity().setupUI(this.rootView);
    }

    public AbstractViewHolder(Context context, int layoutID) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rootView = inflate(layoutID, null);
        getMainActivity().setupUI(this.rootView);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        rootView.setClickable(true);
        rootView.setFocusable(true);
        rootView.setOnClickListener(listener);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            rootView.setVisibility(View.VISIBLE);
        } else {
            rootView.setVisibility(View.GONE);
        }
    }

    @Override
    public View getRootView() {
        return this.rootView;
    }

    @Override
    public int getColor(int colorResID) {
        if (getRes() == null) {
            return 0;
        } else {
            return getRes().getColor(colorResID);
        }
    }

    @Override
    public Resources getRes() {
        if (getContext() == null) {
            return null;
        } else {
            return getContext().getResources();
        }
    }

    @Override
    public Drawable getDrawable(int drawableResId) {
        if (getRes() == null) {
            return null;
        } else {
            return getRes().getDrawable(drawableResId);
        }
    }

    @Override
    public String getStringValue(int id) {
        if (getRes() == null) {
            return null;
        } else {
            if (Translator.isPersian()) {
                return this.getRes().getString(id);
            } else return Translator.getEnglishString(id);
        }
    }

    @Override
    public float getDimen(int dimenResId) {
        if (getRes() == null) {
            return 0;
        } else {
            return getRes().getDimension(dimenResId);
        }
    }

    @Override
    public View findViewById(int viewResId) {
        if (getRootView() == null) {
            return null;
        } else {
            return getRootView().findViewById(viewResId);
        }
    }

    @Override
    public MainActivity getMainActivity() {
        if (getContext() == null) {
            return null;
        } else {
            return (MainActivity) getContext();
        }
    }

    @Override
    public Context getContext() {
        if (getRootView() == null) {
            return null;
        } else {
            return getRootView().getContext();
        }
    }

    @Override
    public MainView getMainView() {
        if (getMainActivity() == null) {
            return null;
        } else {
            return getMainActivity().getMainView();
        }
    }

    @Override
    public void pushFragment(AbstractFragment fragment) {
        pushFragment(fragment, MainView.ANIMATION_TYPE_PUSH);
    }

    @Override
    public void pushFragment(AbstractFragment fragment, int animationType) {
        getMainView().pushFragment(fragment, animationType);
    }

    @Override
    public View inflate(int layoutResId, ViewGroup parent) {
        return inflater.inflate(layoutResId, parent, false);
    }


    @Override
    public void popFragment(boolean shouldReloadBehind) {
        getMainView().popFragment(shouldReloadBehind);
    }

    @Override
    public void popFragment() {
        popFragment(false);
    }

    @Override
    public void request(Request request) {
        if (getMainActivity() != null) {
            getMainActivity().request(request);
        }
    }


    @Override
    @CallSuper
    public void destroy() {
        alive = false;
        rootView = null;
        inflater = null;
        context = null;
    }


    public boolean isAlive() {
        return alive;
    }

}
