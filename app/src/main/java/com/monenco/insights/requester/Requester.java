package com.monenco.insights.requester;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.monenco.insights.MainActivity;
import com.monenco.insights.MainView;
import com.monenco.insights.Settings;
import com.monenco.insights.models.Model;
import com.monenco.insights.models.authorization.AuthorizationToken;
import com.monenco.insights.requester.listeners.OnModelReceivedListener;
import com.monenco.insights.requester.utils.StatusCode;
import com.monenco.insights.requester.utils.URLs;
import com.monenco.insights.views.WelcomePageFragment;

import org.json.JSONObject;

import java.util.ArrayList;

public class Requester {
    private MainActivity mainActivity;
    private ArrayList<Request> pendingAuthorizaedRequests;
    private boolean handlingAuthorizationFailure;
    private AuthorizationToken token;

    public Requester(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        pendingAuthorizaedRequests = new ArrayList<>();
        handlingAuthorizationFailure = false;
        token = AuthorizationToken.getSavedToken(mainActivity);
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm == null || cm.getActiveNetworkInfo() != null;
    }

    public <E extends Model> void request(Request<E> request) {
        request.setRequester(this);
        if (request.isAuthorizationNeeded()) {
            pendingAuthorizaedRequests.add(request);
            if (token == null) {
                authorizationFailure();
                return;
            }
        }
        if (isInternetAvailable()) {
            Builders.Any.B b;
            String url = request.getAbsoluteUrl();
            switch (request.getMethod()) {
                case Request.METHOD_GET:
                    b = Ion.with(mainActivity).load("GET", url);
                    if (request.isAuthorizationNeeded()) {
                        b.setHeader("Authorization", token.getAuthorizationToken());
                    }
                    b.addQueries(request.getQuery())
                            .asByteArray()
                            .withResponse()
                            .setCallback(request);
                    break;
                case Request.METHOD_POST:
                    b = Ion.with(mainActivity).load("POST", url);
                    if (request.isAuthorizationNeeded()) {
                        b.setHeader("Authorization", token.getAuthorizationToken());
                    }
                    if (request.getParams() != null) {
                        RequestParams params = request.getParams();
                        if (params.getFile() != null) {
                            b.setMultipartFile(params.getFileKey(), params.getFileType(), params.getFile())
                                    .setMultipartParameters(params.getQuery());
                        } else {
                            JSONObject jsonObject = params.getJson();
                            JsonParser parser = new JsonParser();
                            JsonObject o = parser.parse(jsonObject.toString()).getAsJsonObject();
                            b.setJsonObjectBody(o);
                        }


                    }
                    b.asByteArray()
                            .withResponse()
                            .setCallback(request);
                    break;
            }
        } else {
            request.getListener().onFailure(request.getId(), StatusCode.NO_INTERNET_CONNECTION);
        }

    }

    public <E extends Model> void removeFromPendingList(Request<E> request) {
        pendingAuthorizaedRequests.remove(request);
    }

    public boolean isHandlingAuthorizationFailure() {
        return handlingAuthorizationFailure;
    }

    void authorizationFailure() {
        if (!handlingAuthorizationFailure) {
            handlingAuthorizationFailure = true;
            if (token == null) {
                notifyRegisterNeeded();
            } else {
                RequestParams params = new RequestParams();
                params.put("client_id", Settings.CLIENT_ID);
                params.put("client_secret", Settings.CLIENT_SECRET);
                params.put("refresh_token", token.refresh_token);
                params.put("grant_type", "refresh_token");
                Request<AuthorizationToken> refreshTokenRequest = new Request<>(Request.METHOD_POST, URLs.AUTHENTICATION + URLs.TOKEN + URLs.REFRESH);
                refreshTokenRequest.setAuthorizationNeeded(false);
                refreshTokenRequest.setParams(params);
                refreshTokenRequest.setModelReceivedListener(new AuthorizationTokenModelListener(), AuthorizationToken.class);
                request(refreshTokenRequest);
            }
        }
    }

    private void notifyRegisterNeeded() {
        mainActivity.getMainView().pushFragment(new WelcomePageFragment(), MainView.ANIMATION_TYPE_FADE_IN);
    }

    public void notifyToken(AuthorizationToken receivedModel) {
        handlingAuthorizationFailure = false;
        token = receivedModel;
        token.saveToken(mainActivity);
        ArrayList<Request> failedRequests = new ArrayList<>(pendingAuthorizaedRequests);
        pendingAuthorizaedRequests.clear();
        for (Request request : failedRequests) {
            request(request);
        }
    }

    public void clear() {
        pendingAuthorizaedRequests.clear();
        handlingAuthorizationFailure = false;
    }

    public String getAccessToken() {
        return token.access_token;
    }

    private class AuthorizationTokenModelListener implements OnModelReceivedListener<AuthorizationToken> {
        @Override
        public void onSuccess(int requestId, int statusCode, AuthorizationToken receivedModel) {
            notifyToken(receivedModel);
        }

        @Override
        public void onFailure(int requestId, int statusCode) {
            notifyRegisterNeeded();
        }

        @Override
        public boolean isAlive() {
            return true;
        }
    }


}

