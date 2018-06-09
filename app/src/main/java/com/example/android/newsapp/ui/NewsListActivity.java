package com.example.android.newsapp.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.newsapp.R;
import com.example.android.newsapp.core.NewsListAdapter;
import com.example.android.newsapp.core.NewsLoader;
import com.example.android.newsapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private final static String NEWS_PAGE_NUMBER_KEY = "news_page_number";
    private static int pageNumber = 1;

    private NewsListAdapter newsListAdapter;
    private ConnectivityManager connManager;
    private ProgressBar progressBar;
    private ScrollView newsListScrollView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        newsListScrollView = findViewById(R.id.news_list_scroll_view);
        progressBar = findViewById(R.id.news_list_progress_bar);
        emptyView = findViewById(R.id.empty_view_text_view);
        RecyclerView newsListRecyclerView = findViewById(R.id.news_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }

        newsListAdapter = new NewsListAdapter(this);
        newsListRecyclerView.setLayoutManager(layoutManager);
        newsListRecyclerView.setAdapter(newsListAdapter);

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        runNetworkActivity();
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        newsListScrollView.setVisibility(View.INVISIBLE);
        return new NewsLoader(NewsListActivity.this, args.getInt(NEWS_PAGE_NUMBER_KEY));
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsList) {

        if(newsList.isEmpty()){
            progressBar.setVisibility(View.GONE);
            emptyView.setText(getString(R.string.no_online_data_available));
            emptyView.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            newsListScrollView.setVisibility(View.VISIBLE);

            newsListAdapter.setNews(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        newsListAdapter.setNews(new ArrayList<NewsItem>());
    }

    private void runNetworkActivity() {

        NetworkInfo networkInfo = null;

        if (connManager != null) {
            networkInfo = connManager.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            Bundle args = new Bundle();
            args.putInt(NEWS_PAGE_NUMBER_KEY, pageNumber);

            getLoaderManager().initLoader(pageNumber, args, this);

        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText(getString(R.string.no_internet_connection));
            emptyView.setVisibility(View.VISIBLE);
        }
    }
}
