package com.example.android.newsapp.utils;

import android.content.Context;

import com.example.android.newsapp.model.NewsItem;

import java.util.List;

public class QueryUtils {

    public static List<NewsItem> getNews(Context context) {
        return JsonUtils.extractNews(HttpUtils.requestNews(context));
    }
}
