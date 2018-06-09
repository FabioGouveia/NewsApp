package com.example.android.newsapp.core;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.newsapp.model.NewsItem;
import com.example.android.newsapp.utils.QueryUtils;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>>{

    public NewsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        return QueryUtils.getNews();
    }
}
