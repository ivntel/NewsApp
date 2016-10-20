package com.example.ivnte.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import static android.content.ContentValues.TAG;

/**
 * Created by ivnte on 2016-10-20.
 */
public class Articles {
    private String mWebTitle;
    private String mPublicationDate;
    private String mWebUrl;

    public Articles(JSONObject newsObject) {
        try {
            this.mWebTitle = newsObject.getString("webTitle");
            this.mPublicationDate = newsObject.getString("webPublicationDate");
            this.mWebUrl = newsObject.getString("webUrl");
        } catch (JSONException e) {
            Log.e(TAG, "Error creating a News object from JSONObject", e);
        }
    }

    public String getWebTitle() { return mWebTitle; }
    public String getPublicationDate() { return mPublicationDate; }
    public String getWebUrl() { return mWebUrl; }

    public static List<Articles> getNewsListFromJson(JSONArray jsonNewsObjects) {
        if( jsonNewsObjects == null || jsonNewsObjects.length() == 0) return null;

        List<Articles> newsList = new ArrayList<Articles>();

        //Extract news objects and from JSONArray
        for(int i = 0; i < jsonNewsObjects.length(); i++) {
            try {
                JSONObject newsObject = jsonNewsObjects.getJSONObject(i);
                newsList.add(new Articles(newsObject));
            } catch (JSONException e) {
                Log.e(TAG, "Error extracting news objects from JSONArray: ", e);
            }
        }

        return newsList;
    }
}