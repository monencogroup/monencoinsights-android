package com.monenco.insights.requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RequestParams {
    private JSONObject json;
    private File file;
    private String fileKey;
    private String fileType;

    public RequestParams() {
        json = new JSONObject();
    }

    public void put(String key, String value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, int value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, boolean value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, double value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, File file, String fileType) {
        this.file = file;
        this.fileKey = key;
        this.fileType = fileType;
    }

    public void put(String key, RequestParams params) {
        try {
            json.put(key, params.json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putAllToList(String key, List<RequestParams> paramsList) {
        if (paramsList != null) {
            for (RequestParams requestParams : paramsList) {
                putToList(key, requestParams);
            }
        }
    }

    public void putToList(String key, RequestParams params) {
        JSONArray toPut = null;
        if (json.has(key)) {
            try {
                toPut = json.getJSONArray(key);
            } catch (JSONException ignored) {
            }
        }
        if (toPut == null) {
            toPut = new JSONArray();
        }
        toPut.put(params.json);
        try {
            json.put(key, toPut);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public String getFileKey() {
        return fileKey;
    }

    public String getFileType() {
        return fileType;
    }

    public JSONObject getJson() {
        return json;
    }


    public HashMap<String, List<String>> getQuery() {
        HashMap<String, List<String>> toReturn = new HashMap<>();
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();
            try {
                Object o = json.get(key);
                ArrayList<String> toPut = new ArrayList<>();
                toPut.add(o.toString());
                toReturn.put(key, toPut);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return toReturn;
    }
}
