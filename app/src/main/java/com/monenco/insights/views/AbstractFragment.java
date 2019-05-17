package com.monenco.insights.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.monenco.insights.MainActivity;
import com.monenco.insights.MainView;
import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;


public abstract class AbstractFragment extends Fragment implements ViewController {
    private View rootView;
    private boolean alive = true;
    public boolean shouldReload = true;
    private LayoutInflater inflater;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        if (rootView == null) {
            shouldReload = true;
            initialArguments();
            rootView = inflater.inflate(getLayoutResId(), container, false);
            getMainActivity().setupUI(this.rootView);
            initialViews();

        }
        onShowActions();
        return rootView;
    }
    public abstract int getLayoutResId();

    public abstract void initialArguments();

    public abstract void initialViews();

    public void onShowActions() {

    }


    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }

        if (animation != null) {
            if (getView() != null) {
                getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        if (getView() != null) {
                            getView().setLayerType(View.LAYER_TYPE_NONE, null);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }

        return animation;
    }

    @Override
    public void onViewCreated(@NonNull final View v, Bundle savedInstanceState) {
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
        rootView = null;
        inflater = null;
        alive = false;
    }


    public boolean isAlive() {
        return alive;
    }

}
