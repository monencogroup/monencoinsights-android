package com.monenco.insights.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.monenco.insights.R;

public class Translator implements Model {
    private static final String LANGUAGE_STATUS = "language_status";
    public static final String PERSIAN = "fa";
    public static final String ENGLISH = "en";
    private static String lang;

    public static void save(Context context, String language) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LANGUAGE_STATUS, language);
            editor.apply();
            lang = language;
        }
    }

    public static void initialLanguage(Context context, String defaultLanguage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences != null && sharedPreferences.contains(LANGUAGE_STATUS)) {
            lang = sharedPreferences.getString(LANGUAGE_STATUS, null);
        } else {
            save(context, defaultLanguage);
        }
    }

    public static boolean isPersian() {
        return lang.equals(PERSIAN);
    }

    public static boolean isEnglish() {
        return lang.equals(ENGLISH);
    }

    public static String getLang() {
        return lang;
    }

    public static String getEnglishString(int id) {
        switch (id) {
            case R.string.app_name:
                return "Monenco Insights";
            case R.string.image_description:
                return "Image";
            case R.string.connection_failed:
                return "Connection Failed";
            case R.string.connection_failed_please_try_again:
                return "Connection failed, please try again!";
            case R.string.no_internet_please_try_again:
                return "No Internet connection, please check your Internet connection and try again!";
            case R.string.out_of_support:
                return "Out of Support";
            case R.string.out_of_support_please_update:
                return "Out of support, please update to last version!";
            case R.string.get_new_version:
                return "Get New Version";
            case R.string.try_again:
                return "Try Again";
            case R.string.loading:
                return "Please wait";
            case R.string.welcome_title:
                return "Welcome to Monenco Insights!";
            case R.string.welcome_message:
                return "Sign up to start using this application or Sign in if you already did";
            case R.string.register:
                return "Sign up";
            case R.string.login:
                return "Sign in";
            case R.string.change_language:
                return "فارسی";
            case R.string.some_fields_are_wrong:
                return "Please enter fields correctly";
            case R.string.this_field_is_required:
                return "This field is required";
            case R.string.password_length_error:
                return "Password should be at least 6 characters";
            case R.string.username_length_error:
                return "Username should be at least 6 characters";
            case R.string.email_error:
                return "Please enter email correctly";
            case R.string.username:
                return "username…";
            case R.string.password:
                return "password…";
            case R.string.email:
                return "email…";
            case R.string.register_message:
                return "Please fill this form";
            case R.string.username_exists:
                return "This username is taken. Please choose another username";
            case R.string.register_completed:
                return "Register completed successfully";
            case R.string.login_completed:
                return "you are logged in successfully";
            case R.string.wrong_username_or_password:
                return "Username or password is wrong";
            case R.string.login_message:
                return "Enter your username and password";
            case R.string.select_categories:
                return "Select Categories";
            case R.string.select_categories_message:
                return "Please select at least";
            case R.string.select_categories_message_after:
                return "categories you like";
            case R.string.you_can_change_this:
                return "You can change this later";
            case R.string.submit:
                return "Submit";
            case R.string.select_categories_success:
                return "Categories selected successfully";
            case R.string.search_hint:
                return "Type here to search…";
            case R.string.read:
                return "Read";
            case R.string.bookmark:
                return "Bookmark";
            case R.string.share:
                return "Share";
            case R.string.logout:
                return "Logout";
            case R.string.bookmarks:
                return "Bookmarks";
            case R.string.setting:
                return "Settings";
            case R.string.logged_out:
                return "You are logged out from your account successfully";
            case R.string.font:
                return "Font";
            case R.string.empty_list:
                return "Nothing to show!";
            case R.string.is_banner:
                return "Exclusive";
            case R.string.new_articles:
                return "New Articles";
            case R.string.search_result:
                return "Search result for";
            case R.string.purchase:
                return "Purchase this article";
            case R.string.free_price:
                return "Free";
            case R.string.tooman:
                return "Tooman(s)";
            case R.string.price:
                return "Price:";
            case R.string.purchased:
                return "Purchased";
            case R.string.payment_done:
                return "Payment done successfully";
            case R.string.payment_failed:
                return "Payment Failed";
            case R.string.purchaseds:
                return "Purchased articles";
            case R.string.about_us:
                return "About us";
            case R.string.all_rights_reserved:
                return "Monenco © All rights reserved";
        }
        return "@@@@@@@@@@@@@@@@@@@@";
    }

}
