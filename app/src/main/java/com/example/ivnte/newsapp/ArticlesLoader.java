package com.example.ivnte.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ivnte on 2016-10-20.
 */

public class ArticlesLoader extends AsyncTaskLoader<List<Articles>> {
    private String mUrl;

    public ArticlesLoader(Context context, String url) {
        super(context);
        Log.i(TAG, "ArticlesLoader: " + url);
        mUrl = url;
    }

    @Override
    public List<Articles> loadInBackground() {
        if (mUrl == null) return null;

        List<Articles> articleList = QueryUtils.fetchNewsData(mUrl);
        return articleList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
