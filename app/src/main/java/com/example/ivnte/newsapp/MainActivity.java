package com.example.ivnte.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Articles>> {
    public static final String GUARDIAN_API_URL = "http://content.guardianapis.com/search?";
    String APIKEY = "api-key=789ae62c-2002-4eea-96fe-78f69d996ec4&q=";
    private ListView articlesListView;
    private ArticlesAdapter articlesAdapter;
    private static final int NEWS_LOADER_ID = 1;

    public String inputSearch;
    public TextView noResults;
    public TextView titles;
    public TextView sections;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isNetworkAvailable();

        Button but = (Button) findViewById(R.id.search_button);
        noResults = (TextView) findViewById(R.id.no_results);
        titles = (TextView) findViewById(R.id.titles_row);
        sections = (TextView) findViewById(R.id.sections_row);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) findViewById(R.id.search_text);
                inputSearch = searchText.getText().toString();

                if (!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, "No internet connections available", Toast.LENGTH_SHORT).show();
                } else {
                    LoaderManager loaderManager = getSupportLoaderManager();

                    if (loaderManager.getLoader(NEWS_LOADER_ID) == null) {
                        loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    } else {
                        loaderManager.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    }
                    loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
                }
            }
        });

        articlesListView = (ListView) findViewById(R.id.articles_list);
        articlesAdapter = new ArticlesAdapter(this, new ArrayList<Articles>());
        articlesListView.setAdapter(articlesAdapter);
        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current news item that was clicked on
                Articles newsItem = articlesAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsItemUri = Uri.parse(newsItem.getWebUrl());

                // Create a new intent to view the News Article
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Articles>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        return new ArticlesLoader(this, GUARDIAN_API_URL + APIKEY + inputSearch);
    }

    @Override
    public void onLoadFinished(Loader<List<Articles>> loader, List<Articles> articleList) {
        articlesAdapter.clear();

        if (articleList != null && !articleList.isEmpty()) {
            titles.setVisibility(View.VISIBLE);
            sections.setVisibility(View.VISIBLE);
            articlesAdapter.addAll(articleList);
        } else {
            Toast.makeText(MainActivity.this, "No results available", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Articles>> loader) {
        articlesAdapter.clear();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}