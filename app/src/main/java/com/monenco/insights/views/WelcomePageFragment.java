package com.monenco.insights.views;

import android.view.View;
import android.widget.TextView;

import com.monenco.insights.MainActivity;
import com.monenco.insights.MainView;
import com.monenco.insights.R;
import com.monenco.insights.models.Translator;

public class WelcomePageFragment extends AbstractFragment implements View.OnClickListener {
    private ButtonView login;
    private ButtonView register;
    private ButtonView changeLanguage;

    @Override
    public int getLayoutResId() {
        return R.layout.welcome_page_fragment_layout;
    }

    @Override
    public void initialArguments() {

    }

    @Override
    public void initialViews() {
        TextView welcomeTitle = (TextView) findViewById(R.id.welcome_page_title);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcome_page_message);
        welcomeTitle.setText(getStringValue(R.string.welcome_title));
        welcomeMessage.setText(getStringValue(R.string.welcome_message));
        login = new ButtonView(findViewById(R.id.welcome_page_login),R.string.login);
        register = new ButtonView(findViewById(R.id.welcome_page_register),R.string.register);
        changeLanguage = new ButtonView(findViewById(R.id.welcome_page_change_language),R.string.change_language);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        changeLanguage.setOnClickListener(this);
        getMainView().setState(MainView.STATE_DONE);
    }

    @Override
    public void onClick(View view) {
        if (view == register.getRootView()) {
            pushFragment(new RegisterFragment());
        } else if (view == login.getRootView()) {
            pushFragment(new LoginFragment());
        } else if (view == changeLanguage.getRootView()) {
            String newLang;
            if(Translator.isPersian()){
                newLang = Translator.ENGLISH;
            }else {
                newLang = Translator.PERSIAN;
            }
            Translator.save(getContext(),newLang);
            getMainActivity().clear();
        }
    }
}
