package com.monenco.insights.requester.utils;

public class StatusCode {
    public static final int NO_INTERNET_CONNECTION = -1;
    public static final int AUTHORIZATION_FAILED = 401;
    public static final int BAD_REQUEST = 400;


    public static boolean isSuccess(int statusCode) {
        return (statusCode / 100) == 2;
    }
}
