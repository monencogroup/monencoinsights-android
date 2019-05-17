package com.monenco.insights.requester;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.monenco.insights.Settings;
import com.monenco.insights.models.Model;
import com.monenco.insights.requester.listeners.OnModelListReceivedListener;
import com.monenco.insights.requester.listeners.OnModelReceivedListener;
import com.monenco.insights.requester.listeners.OnPaginatedModelListReceivedListener;
import com.monenco.insights.requester.listeners.OnReceivedListener;
import com.monenco.insights.requester.listeners.OnResponseReceivedListener;
import com.monenco.insights.requester.utils.StatusCode;
import com.monenco.insights.requester.utils.URLs;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Request<E extends Model> implements FutureCallback<Response<byte[]>> {

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;

    private static final int LISTENER_TYPE_NONE = 0;
    private static final int LISTENER_TYPE_RESPONSE = 1;
    private static final int LISTENER_TYPE_MODEL = 2;
    private static final int LISTENER_TYPE_MODEL_LIST = 3;
    private static final int LISTENER_TYPE_PAGINATED_MODEL_LIST = 4;


    private int id;
    private int method;
    private String relativeUrl;
    private String baseUrl;
    private boolean needsAuthorization;
    private RequestParams params;

    private Class<E> objectsClass;
    private OnReceivedListener listener;
    private int listenerType;
    private Requester requester;

    private static int nextID = 1;


    public Request(int method, String relativeUrl) {
        this.id = nextID++;
        this.method = method;
        this.relativeUrl = relativeUrl;
        this.baseUrl = Settings.DOMAIN + Settings.API_URL;
        this.needsAuthorization = true;
        this.listenerType = LISTENER_TYPE_NONE;
    }

    public void setBaseUrl(String baseUrl) {
        if (baseUrl == null) {
            baseUrl = "";
        }
        this.baseUrl = baseUrl;
    }

    public void setAuthorizationNeeded(boolean needsAuthorization) {
        this.needsAuthorization = needsAuthorization;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    public void setResponseReceivedListener(OnResponseReceivedListener listener) {
        this.listener = listener;
        this.listenerType = LISTENER_TYPE_RESPONSE;
    }

    public void setModelReceivedListener(OnModelReceivedListener<E> listener, Class<E> objectsClass) {
        this.listener = listener;
        this.listenerType = LISTENER_TYPE_MODEL;
        this.objectsClass = objectsClass;
    }

    public void setModelListReceivedListener(OnModelListReceivedListener<E> listener, Class<E> objectsClass) {
        this.listener = listener;
        this.listenerType = LISTENER_TYPE_MODEL_LIST;
        this.objectsClass = objectsClass;
    }

    public void setPaginatedModelListReceivedListener(OnPaginatedModelListReceivedListener<E> listener, Class<E> objectsClass) {
        this.listener = listener;
        this.listenerType = LISTENER_TYPE_PAGINATED_MODEL_LIST;
        this.objectsClass = objectsClass;
    }


    public String getAbsoluteUrl() {
        return this.baseUrl.concat(this.relativeUrl);
    }

    public boolean isAuthorizationNeeded() {
        return this.needsAuthorization;
    }

    public int getMethod() {
        return this.method;
    }

    public int getId() {
        return this.id;
    }


    public OnReceivedListener getListener() {
        return listener;
    }

    public RequestParams getParams() {
        return params;
    }


    public HashMap<String, List<String>> getQuery() {
        if (params == null) {
            return new HashMap<>();
        } else {
            return params.getQuery();
        }
    }

    public void setRequester(Requester requester) {
        this.requester = requester;
    }

    @Override
    public void onCompleted(Exception e, Response<byte[]> result) {
        handleResponse(e, result);
    }

    private void handleResponse(Exception e, Response<byte[]> result) {
        if (listener.isAlive()) {
            if (e != null) {
                requester.removeFromPendingList(this);
                listener.onFailure(id, StatusCode.NO_INTERNET_CONNECTION);
            } else {
                int statusCode = result.getHeaders().code();
                if (!(needsAuthorization && requester.isHandlingAuthorizationFailure())) {
                    if (StatusCode.isSuccess(statusCode)) {
                        requester.removeFromPendingList(this);
                        String receivedString = new String(result.getResult());
                        Gson gson = new Gson();
                        ArrayList<E> receivedModelList = new ArrayList<>();
                        boolean hasNext = false;
                        boolean hasPrevious = false;
                        switch (listenerType) {
                            case Request.LISTENER_TYPE_RESPONSE:
                                ((OnResponseReceivedListener) listener).onSuccess(id, statusCode);
                                break;
                            case Request.LISTENER_TYPE_MODEL:
                                E receivedModel = gson.fromJson(receivedString, objectsClass);
                                ((OnModelReceivedListener<E>) listener).onSuccess(id, statusCode, receivedModel);
                                break;
                            case Request.LISTENER_TYPE_MODEL_LIST:
                                try {
                                    JSONArray receivedModelListJson = new JSONArray(receivedString);
                                    for (int i = 0; i < receivedModelListJson.length(); i++) {
                                        String receivedModelString = receivedModelListJson.getJSONObject(i).toString();
                                        receivedModelList.add(gson.fromJson(receivedModelString, objectsClass));
                                    }
                                } catch (JSONException ignored) {
                                }
                                ((OnModelListReceivedListener<E>) listener).onSuccess(id, statusCode, receivedModelList);
                                break;
                            case Request.LISTENER_TYPE_PAGINATED_MODEL_LIST:
                                try {
                                    JSONObject receivedPaginatedJson = new JSONObject(receivedString);
                                    JSONArray receivedModelListJson = receivedPaginatedJson.getJSONArray("results");
                                    for (int i = 0; i < receivedModelListJson.length(); i++) {
                                        String receivedModelString = receivedModelListJson.getJSONObject(i).toString();
                                        receivedModelList.add(gson.fromJson(receivedModelString, objectsClass));
                                    }
                                    String nextString = receivedPaginatedJson.getString("next");
                                    String previousString = receivedPaginatedJson.getString("previous");
                                    hasNext = !(nextString == null || nextString.isEmpty() || nextString.equals("null"));
                                    hasPrevious = !(previousString == null || previousString.isEmpty() || previousString.equals("null"));
                                } catch (JSONException ignored) {
                                }
                                ((OnPaginatedModelListReceivedListener<E>) listener).onSuccess(id, statusCode, receivedModelList, hasNext, hasPrevious);

                        }

                    } else if (statusCode != StatusCode.AUTHORIZATION_FAILED) {
                        requester.removeFromPendingList(this);
                        listener.onFailure(id, statusCode);
                    } else {
                        if (needsAuthorization) {
                            requester.authorizationFailure();
                        } else {
                            listener.onFailure(id, statusCode);
                        }
                    }
                }
            }
        } else {
            requester.removeFromPendingList(this);
        }


    }
}
