package com.example.android.newsapp.utils;

import com.example.android.newsapp.model.NewsItem;

import java.util.List;

public class QueryUtils {

    public static List<NewsItem> getNews(int page){
        return JsonUtils.extractNews(HttpUtils.requestNews(page));
    }
}
