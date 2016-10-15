package com.example.ivnte.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
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
    //JSON Key names
    private static final String KEY_ID = "id";
    private static final String KEY_SECTION_NAME = "sectionName";
    private static final String KEY_TITLE = "webTitle";
    private static final String KEY_WEB_URL = "webUrl";
    private static final String KEY_DATE = "webPublicationDate";


    ArrayList<HashMap<String, String>> articlesdata = new ArrayList<HashMap<String, String>>();

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
                finalArticlesURL = finalArticlesURL + "&q="+inputSearch;

                if(!isNetworkAvailable()){
                    Toast.makeText(MainActivity.this, "No internet connections available", Toast.LENGTH_SHORT).show();
                }
                else {
                    //new ProcessJSON().execute(articlesURL);
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}