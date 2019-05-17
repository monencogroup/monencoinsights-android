package com.monenco.insights;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;

import com.monenco.insights.models.Article;
import com.monenco.insights.models.Category;
import com.monenco.insights.models.Configs;
import com.monenco.insights.models.SupportModel;
import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnModelReceivedListener;
import com.monenco.insights.requester.utils.StatusCode;
import com.monenco.insights.requester.utils.URLs;
import com.monenco.insights.views.AbstractFragment;
import com.monenco.insights.views.CategorySelectFragment;
import com.monenco.insights.views.LaunchScreenView;
import com.monenco.insights.views.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainView {
    public static final int STATE_LOADING = 0;
    public static final int STATE_DONE = 1;
    public static final int STATE_FAILED = 2;
    public boolean showedPaymentDonePopup;

    private MainActivity mainActivity;
    private FrameLayout fragmentContainer;
    private LaunchScreenView launchScreen;
    private ArrayList<AbstractFragment> fragments;

    private SupportModel supportModel;
    private Configs configs;

    public static final int ANIMATION_TYPE_PUSH = 1;
    public static final int ANIMATION_TYPE_POP = 2;
    public static final int ANIMATION_TYPE_FADE_IN = 3;


    private View findViewById(int id) {
        return mainActivity.findViewById(id);
    }

    private MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setState(int state) {
        launchScreen.setState(state);
        switch (state) {
            case STATE_LOADING:
            case STATE_FAILED:
                fragmentContainer.setVisibility(View.GONE);
                break;
            case STATE_DONE:
                fragmentContainer.setVisibility(View.VISIBLE);
                break;
        }
    }

    public MainView(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        fragmentContainer = (FrameLayout) findViewById(R.id.mainView_fragmentContainer);
        launchScreen = new LaunchScreenView(findViewById(R.id.mainView_launchScreen));
        initialSupport();
    }

    private void initialSupport() {
        setState(STATE_LOADING);
        RequestParams params = new RequestParams();
        params.put("version", Settings.VERSION);
        Request<SupportModel> supportModelRequest = new Request<>(Request.METHOD_GET, URLs.VERSION + URLs.CHECK);
        supportModelRequest.setBaseUrl(Settings.DOMAIN);
        supportModelRequest.setAuthorizationNeeded(false);
        supportModelRequest.setModelReceivedListener(new SupportModelListener(), SupportModel.class);
        supportModelRequest.setParams(params);
        getMainActivity().request(supportModelRequest);
    }

    private void checkSupport() {
        if (supportModel.isSupported) {
            initialCategoryList();
        } else {
            launchScreen.setFailedView(R.string.out_of_support, R.string.out_of_support_please_update, R.string.get_new_version, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(supportModel.newVersionLink));
                    getMainActivity().startActivity(browserIntent);
                }
            });
            setState(STATE_FAILED);
        }
    }


    private void initialCategoryList() {
        setState(STATE_LOADING);
        RequestParams params = new RequestParams();
        params.put("lang", Translator.getLang());
        Request<Configs> supportModelRequest = new Request<>(Request.METHOD_GET, URLs.CONFIGS);
        supportModelRequest.setModelReceivedListener(new ConfigsListener(), Configs.class);
        supportModelRequest.setParams(params);
        getMainActivity().request(supportModelRequest);
    }

    private void checkCategorySelect() {
        setState(MainView.STATE_DONE);

        int favoriteCount = 0;
        for (Category category : configs.categories) {
            if (category.isFavorite) {
                favoriteCount++;
            }
        }
        if (favoriteCount < configs.minCategorySelectCount) {
            pushFragment(new CategorySelectFragment(), ANIMATION_TYPE_FADE_IN);
        } else {
            pushFragment(new SearchFragment(), ANIMATION_TYPE_FADE_IN);
        }

    }

    public void popFragment(boolean shouldReloadBehind) {
        if (fragments.size() <= 1) {
            getMainActivity().handleOsBack();
        } else {
            fragments.remove(fragments.size() - 1);
            AbstractFragment fragment = fragments.get(fragments.size() - 1);
            fragmentTrans(shouldReloadBehind, fragment, ANIMATION_TYPE_POP);
        }
    }

    public void popFragment(int count) {
        for (int i = 0; i < count; i++) {
            fragments.remove(fragments.size() - 1);
        }
        if (!fragments.isEmpty()) {
            AbstractFragment fragment = fragments.get(fragments.size() - 1);
            fragmentTrans(false, fragment, ANIMATION_TYPE_POP);
        }
    }

    public void pushFragment(AbstractFragment fragment, int animationType) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        fragments.add(fragment);
        fragmentTrans(false, fragment, animationType);
    }

    private void fragmentTrans(boolean shouldReloadBehind, AbstractFragment toSwitchTo, int direction) {
        if (shouldReloadBehind) {
            toSwitchTo.shouldReload = true;
        }
        android.support.v4.app.FragmentManager fragmentManager = getMainActivity().getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        int enter, exit;
        switch (direction) {
            case ANIMATION_TYPE_FADE_IN:
                enter = R.anim.fade_in;
                exit = R.anim.fade_out;
                break;
            case ANIMATION_TYPE_POP:
                if (Translator.isPersian()) {
                    enter = R.anim.enter_pop_rtl;
                    exit = R.anim.exit_pop_rtl;
                } else {
                    enter = R.anim.enter_pop_ltr;
                    exit = R.anim.exit_pop_ltr;
                }
                break;
            case ANIMATION_TYPE_PUSH:
                if (Translator.isPersian()) {
                    enter = R.anim.enter_push_rtl;
                    exit = R.anim.exit_push_rtl;
                } else {
                    enter = R.anim.enter_push_ltr;
                    exit = R.anim.exit_push_ltr;
                }
                break;
            default:
                enter = 0;
                exit = 0;
        }
        if (enter != 0) {
            fragmentTransaction.setCustomAnimations(enter, exit);
        }
        fragmentTransaction.replace(fragmentContainer.getId(), toSwitchTo);
        fragmentTransaction.commit();
    }

    public boolean isEmpty() {
        return fragments.size() == 1;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.configs.categories = categories;
    }


    private class SupportModelListener implements OnModelReceivedListener<SupportModel> {

        @Override
        public void onSuccess(int requestId, int statusCode, SupportModel receivedModel) {
            supportModel = receivedModel;
            checkSupport();
        }

        @Override
        public void onFailure(int requestId, int statusCode) {
            int subTitleResId;
            if (statusCode == StatusCode.NO_INTERNET_CONNECTION) {
                subTitleResId = R.string.no_internet_please_try_again;
            } else {
                subTitleResId = R.string.connection_failed_please_try_again;
            }
            launchScreen.setFailedView(R.string.connection_failed, subTitleResId, R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initialSupport();
                }
            });
            setState(STATE_FAILED);
        }

        @Override
        public boolean isAlive() {
            return true;
        }
    }

    private class ConfigsListener implements OnModelReceivedListener<Configs> {

        @Override
        public void onSuccess(int requestId, int statusCode, Configs receivedModel) {
            configs = receivedModel;
            if (configs.bannerArticles == null) {
                configs.bannerArticles = new ArrayList<>();
            }
            if(configs.newArticles==null){
                configs.newArticles = new ArrayList<>();
            }
            checkCategorySelect();
        }

        @Override
        public void onFailure(int requestId, int statusCode) {
            int subTitleResId;
            if (statusCode == StatusCode.NO_INTERNET_CONNECTION) {
                subTitleResId = R.string.no_internet_please_try_again;
            } else {
                subTitleResId = R.string.connection_failed_please_try_again;
            }
            launchScreen.setFailedView(R.string.connection_failed, subTitleResId, R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initialCategoryList();
                }
            });
            setState(STATE_FAILED);
        }

        @Override
        public boolean isAlive() {
            return true;
        }


    }

    public void clear() {
        fragments = new ArrayList<>();
        initialSupport();
    }

    public List<Category> getCategories() {
        return configs.categories;
    }

    public Configs getConfigs() {
        return configs;
    }

}
