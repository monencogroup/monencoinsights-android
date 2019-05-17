package com.monenco.insights.requester.listeners;

public interface OnReceivedListener {
    void onFailure(int requestId, int statusCode);

    boolean isAlive();
}
