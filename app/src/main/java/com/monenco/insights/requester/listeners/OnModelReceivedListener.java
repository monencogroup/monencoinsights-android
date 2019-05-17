package com.monenco.insights.requester.listeners;


import com.monenco.insights.models.Model;

public interface OnModelReceivedListener<E extends Model> extends OnReceivedListener {
    void onSuccess(int requestId, int statusCode, E receivedModel);
}
