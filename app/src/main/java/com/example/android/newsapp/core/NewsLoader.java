package com.example.android.newsapp.core;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.newsapp.model.NewsItem;
import com.example.android.newsapp.utils.QueryUtils;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>>{

    private int pageNumber;

    public NewsLoader(Context context, int pageNumber){
        super(context);
        this.pageNumber = pageNumber;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        return QueryUtils.getNews(pageNumber);
    }
}
