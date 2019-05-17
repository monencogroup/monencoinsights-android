package com.monenco.insights.views;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.monenco.insights.R;
import com.monenco.insights.Settings;
import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnResponseReceivedListener;
import com.monenco.insights.requester.utils.URLs;

public class SettingFragment extends AbstractFragment implements View.OnClickListener {
    private View back;
    private SettingViewHolder categories;
    private SettingViewHolder bookmarks;
    private SettingViewHolder purchases;
    private SettingViewHolder monencoWebsite;
    private SettingViewHolder aboutUs;
    private SettingViewHolder language;
    private SettingViewHolder logout;
    private boolean loading;

    @Override
    public int getLayoutResId() {
        return R.layout.setting_fragment_layout;
    }

    @Override
    public void initialArguments() {

    }

    @Override
    public void initialViews() {
        TextView title = (TextView) findViewById(R.id.setting_fragment_title);
        title.setText(getStringValue(R.string.setting));
        back = findViewById(R.id.setting_fragment_back);
        if (Translator.isPersian()) {
            back.setScaleX(-1);
        }
        back.setOnClickListener(this);
        categories = new SettingViewHolder(findViewById(R.id.setting_fragment_categories), R.string.select_categories, R.drawable.ic_category, false);
        bookmarks = new SettingViewHolder(findViewById(R.id.setting_fragment_bookmarks), R.string.bookmarks, R.drawable.ic_bookmark, false);
        purchases = new SettingViewHolder(findViewById(R.id.setting_fragment_purchases), R.string.purchaseds, R.drawable.ic_price, false);
        monencoWebsite = new SettingViewHolder(findViewById(R.id.setting_fragment_monenco_website), getMainView().getConfigs().monencoWebsiteTitle, R.drawable.ic_link, false);
        aboutUs = new SettingViewHolder(findViewById(R.id.setting_fragment_about_us), R.string.about_us, R.drawable.ic_author, false);
        language = new SettingViewHolder(findViewById(R.id.setting_fragment_language), R.string.change_language, R.drawable.ic_language, false);
        logout = new SettingViewHolder(findViewById(R.id.setting_fragment_logout), R.string.logout, R.drawable.ic_logout, true);
        categories.setOnClickListener(this);
        bookmarks.setOnClickListener(this);
        purchases.setOnClickListener(this);
        monencoWebsite.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        language.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (!loading) {
            if (view == back) {
                popFragment();
            } else if (view == categories.getRootView()) {
                pushFragment(new CategorySelectFragment());
            } else if (view == bookmarks.getRootView()) {
                pushFragment(new BookmarksFragment());
            } else if (view == purchases.getRootView()) {
                pushFragment(new PurchasedsFragment());
            } else if (view == monencoWebsite.getRootView()) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getMainView().getConfigs().monencoWebsite));
                startActivity(i);
            } else if (view == aboutUs.getRootView()) {
                pushFragment(new AboutUsFragment());
            } else if (view == language.getRootView()) {
                String newLang;
                if (Translator.isPersian()) {
                    newLang = Translator.ENGLISH;
                } else {
                    newLang = Translator.PERSIAN;
                }
                Translator.save(getContext(), newLang);
                getMainActivity().clear();
            } else if (view == logout.getRootView()) {
                logout();
            }
        }
    }

    public void logout() {
        loading = true;
        Toast.makeText(getMainActivity(), getStringValue(R.string.loading), Toast.LENGTH_SHORT).show();
        RequestParams params = new RequestParams();
        params.put("token", getMainActivity().getRequester().getAccessToken());
        params.put("client_id", Settings.CLIENT_ID);
        params.put("client_secret", Settings.CLIENT_SECRET);
        Request request = new Request(Request.METHOD_POST, URLs.AUTHENTICATION + URLs.LOGOUT);
        request.setParams(params);
        request.setResponseReceivedListener(new LogoutResponseHandler());
        request(request);
    }

    private class LogoutResponseHandler implements OnResponseReceivedListener {

        @Override
        public void onSuccess(int requestId, int statusCode) {
            loading = false;
            Toast.makeText(getContext(), getStringValue(R.string.logged_out), Toast.LENGTH_LONG).show();
            getMainActivity().clear();
        }

        @Override
        public void onFailure(int requestId, int statusCode) {
            loading = false;
            Toast.makeText(getContext(), getStringValue(R.string.connection_failed_please_try_again), Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean isAlive() {
            return SettingFragment.this.isAlive();
        }
    }
}
