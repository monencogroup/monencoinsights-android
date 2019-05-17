package com.monenco.insights.models.authorization;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.monenco.insights.models.Model;


public class AuthorizationToken implements Model {
    private static final String ACCESS_TOKEN = "authorization_token_access_token";
    private static final String REFRESH_TOKEN = "authorization_token_refresh_token";
    private static final String TOKEN_TYPE = "authorization_token_token_type";

    public String access_token;
    public String refresh_token;
    public String token_type;

    public void saveToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ACCESS_TOKEN, this.access_token);
            editor.putString(REFRESH_TOKEN, this.refresh_token);
            editor.putString(TOKEN_TYPE, this.token_type);
            editor.apply();
        }
    }

    public static AuthorizationToken getSavedToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences != null && sharedPreferences.contains(ACCESS_TOKEN) && sharedPreferences.contains(REFRESH_TOKEN) && sharedPreferences.contains(TOKEN_TYPE)) {
            AuthorizationToken toReturn = new AuthorizationToken();
            toReturn.access_token = sharedPreferences.getString(ACCESS_TOKEN, null);
            toReturn.refresh_token = sharedPreferences.getString(REFRESH_TOKEN, null);
            toReturn.token_type = sharedPreferences.getString(TOKEN_TYPE, null);
            return toReturn;

        } else {
            return null;
        }
    }

    public String getAuthorizationToken() {
        return token_type + " " + access_token;
    }
}
