package com.monenco.insights.requester.listeners;

public interface OnResponseReceivedListener extends OnReceivedListener {
    void onSuccess(int requestId, int statusCode);
}
