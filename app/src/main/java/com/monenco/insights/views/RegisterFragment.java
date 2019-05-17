package com.monenco.insights.views;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.monenco.insights.R;
import com.monenco.insights.Settings;
import com.monenco.insights.models.Translator;
import com.monenco.insights.models.authorization.AuthorizationToken;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnModelReceivedListener;
import com.monenco.insights.requester.utils.URLs;

public class RegisterFragment extends AbstractFragment implements View.OnClickListener, FormView.FormHandler {
    private FormView form;

    @Override
    public int getLayoutResId() {
        return R.layout.register_fragment_layout;
    }

    @Override
    public void initialArguments() {

    }

    @Override
    public void initialViews() {
        View back = findViewById(R.id.register_back);
        if (Translator.isPersian()) {
            back.setScaleX(-1);
        }
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.register_title);
        title.setText(getStringValue(R.string.register));
        TextView message = (TextView) findViewById(R.id.register_message);
        message.setText(getStringValue(R.string.register_message));
        form = new FormView(findViewById(R.id.register_form), R.string.register, this);

        form.addField("email", R.drawable.ic_email, R.string.email, FormTextFieldView.TYPE_EMAIL);
        form.addField("username", R.drawable.ic_username, R.string.username, FormTextFieldView.TYPE_USERNAME);
        form.addField("password", R.drawable.ic_password, R.string.password, FormTextFieldView.TYPE_PASSWORD);

    }

    @Override
    public void onClick(View view) {
        popFragment();
    }

    @Override
    public void handleParams(RequestParams params) {
        form.setLoading(true);
        params.put("client_id", Settings.CLIENT_ID);
        params.put("client_secret", Settings.CLIENT_SECRET);
        Request request = new Request(Request.METHOD_POST, URLs.AUTHENTICATION + URLs.REGISTER);
        request.setAuthorizationNeeded(false);
        request.setParams(params);
        request.setModelReceivedListener(new RegisterResponseHandler(), AuthorizationToken.class);
        request(request);
    }

    private class RegisterResponseHandler implements OnModelReceivedListener<AuthorizationToken> {

        @Override
        public void onSuccess(int requestId, int statusCode, AuthorizationToken response) {
            Toast.makeText(getContext(), getStringValue(R.string.register_completed), Toast.LENGTH_LONG).show();
            getMainView().popFragment(2);
            getMainActivity().getRequester().notifyToken(response);
        }

        @Override
        public void onFailure(int requestId, int statusCode) {
            form.setLoading(false);
            if (statusCode == 400) {
                Toast.makeText(getContext(), getStringValue(R.string.username_exists), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), getStringValue(R.string.connection_failed_please_try_again), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean isAlive() {
            return true;
        }
    }

}
