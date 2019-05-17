package com.monenco.insights.requester.listeners;


import com.monenco.insights.models.Model;

import java.util.List;

public interface OnPaginatedModelListReceivedListener<E extends Model> extends OnReceivedListener {
    void onSuccess(int requestId, int statusCode, List<E> receivedList, boolean hasPrevious, boolean hasNext);
}
