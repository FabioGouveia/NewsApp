package com.example.android.newsapp.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private static final String LOG_TAG = NewsListActivity.class.getSimpleName();

    private NewsListAdapter newsListAdapter;
    private ConnectivityManager connManager;
    private ProgressBar progressBar;
    private ScrollView newsListScrollView;
    private ImageView emptyImageView;
    private TextView emptyView;
    private Button retryConnectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            String subtitle = String.format(getString(R.string.news_list_showing_subtitle), sharedPreferences.getString(getString(R.string.settings_search_key), getString(R.string.settings_search_default)));
            actionBar.setSubtitle(defineSubtitle(subtitle));
        }

        newsListScrollView = findViewById(R.id.news_list_scroll_view);
        progressBar = findViewById(R.id.news_list_progress_bar);
        emptyImageView = findViewById(R.id.empty_view_image_view);
        emptyView = findViewById(R.id.empty_view_text_view);
        retryConnectionButton = findViewById(R.id.retry_network_connection_button);
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

        retryConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidateOptionsMenu();
                runNetworkActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem userPreferenceMenuItem = menu.findItem(R.id.user_preferences_settings);

        if (isConnectedToTheNetwork()) userPreferenceMenuItem.setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.user_preferences_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(R.anim.enter_from_right_animation, R.anim.exit_to_left_animation);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        newsListScrollView.setVisibility(View.INVISIBLE);
        return new NewsLoader(NewsListActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsList) {

        try {
            if (newsList.isEmpty()) {
                setNoDataAvailableLayout(R.string.no_online_data_available);
            } else {
                progressBar.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                newsListScrollView.setVisibility(View.VISIBLE);

                newsListAdapter.setNews(newsList);
            }
        } catch (NullPointerException e) {
            setNoDataAvailableLayout(R.string.no_online_data_available);
            Log.e(LOG_TAG, "Error while getting the news list: ", e);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        newsListAdapter.setNews(new ArrayList<NewsItem>());
    }

    private String defineSubtitle(String subtitle) {
        if (subtitle.equalsIgnoreCase(getString(R.string.settings_search_art_and_design_value))) {
            return getString(R.string.settings_search_art_and_design_label);
        } else if (subtitle.equalsIgnoreCase(getString(R.string.settings_search_jobs_advice_value))) {
            return subtitle.substring(0, 3);
        } else {
            return subtitle.replaceAll("-", " ");
        }
    }

    private void runNetworkActivity() {

        if (isConnectedToTheNetwork()) {

            emptyImageView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            retryConnectionButton.setVisibility(View.GONE);

            getLoaderManager().initLoader(0, null, this);

        } else {
            setNoDataAvailableLayout(R.string.no_internet_connection);
        }
    }

    private boolean isConnectedToTheNetwork() {
        NetworkInfo networkInfo = null;

        if (connManager != null) networkInfo = connManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void setNoDataAvailableLayout(int messageResource) {
        progressBar.setVisibility(View.GONE);
        emptyImageView.setImageResource(R.drawable.no_available_news);
        emptyImageView.setVisibility(View.VISIBLE);
        emptyView.setText(getString(messageResource));
        emptyView.setVisibility(View.VISIBLE);
        retryConnectionButton.setVisibility(View.VISIBLE);
    }
}
