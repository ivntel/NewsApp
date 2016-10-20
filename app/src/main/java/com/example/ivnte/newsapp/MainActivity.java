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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Articles>>{
    public static final String GUARDIAN_API_URL = "http://content.guardianapis.com/technology";
    private ListView articlesListView;
    private ArticlesAdapter articlesAdapter;
    private static final int NEWS_LOADER_ID = 1;

    ListView articleList;
    String APIKEY = "&api-key=789ae62c-2002-4eea-96fe-78f69d996ec4";
    String pageSize = "&page-size=20";
    public String articlesURL = "https://content.guardianapis.com/search?"+APIKEY+pageSize;
    public String finalArticlesURL;
    public int pageCount=1;
    public TextView noResults;
    public TextView titles;
    public TextView authors;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isNetworkAvailable();

        Button but = (Button) findViewById(R.id.search_button);
        noResults = (TextView) findViewById(R.id.no_results);
        titles = (TextView) findViewById(R.id.titles_row);
        authors = (TextView) findViewById(R.id.authors_row);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) findViewById(R.id.search_text);
                String inputSearch = searchText.getText().toString();
                inputSearch = inputSearch.replace(" ", "+");
                finalArticlesURL = finalArticlesURL + "&q=" + inputSearch;

                if (!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, "No internet connections available", Toast.LENGTH_SHORT).show();
                } else {
                    LoaderManager loaderManager = getSupportLoaderManager();
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
            Uri baseUri = Uri.parse(GUARDIAN_API_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            // Set api key
            uriBuilder.appendQueryParameter("api-key", "test");

            return new ArticlesLoader(this, uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Articles>> loader, List<Articles> articleList) {
            articlesAdapter.clear();

            if(articleList != null && !articleList.isEmpty()) {
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