package com.example.android.newsapp.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.newsapp.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

final class JsonUtils {

    private final static String LOG_TAG = JsonUtils.class.getSimpleName();

    static List<NewsItem> extractNews(@Nullable String jsonString){

        if(TextUtils.isEmpty(jsonString)) return null;

        List<NewsItem> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(jsonString);
            JSONObject responseObject = baseJsonObject.getJSONObject("response");
            String responseStatus = responseObject.getString("status");

            if(responseStatus.equals("ok")){

                JSONArray resultsArray = responseObject.getJSONArray("results");

                int resultsSize = resultsArray.length();
                for(int i = 0; i < resultsSize; i++){
                    JSONObject resultObject = resultsArray.getJSONObject(i);

                    String title = resultObject.getString("webTitle");
                    String section = resultObject.getString("sectionName");
                    String url = resultObject.getString("webUrl");

                    NewsItem newsItem = new NewsItem(title, section, url);

                    if(resultObject.has("tags")){
                        JSONArray tagsArray = resultObject.getJSONArray("tags");

                        if(tagsArray.length() > 0 && tagsArray.getJSONObject(0).has("webTitle")){
                            newsItem.setAuthor(tagsArray.getJSONObject(0).getString("webTitle"));
                        }

                    }

                    if(resultObject.has("webPublicationDate")){
                        newsItem.setDate(parseDate(resultObject.getString("webPublicationDate")));
                    }

                    newsList.add(newsItem);

                }
            }

        }catch (JSONException e){
            Log.e(LOG_TAG, "Error occurred parsing json base object", e);
        }

        return newsList;
    }

    private static String parseDate(String rawDate){

        Date date = null;

        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(rawDate);
        }catch(ParseException e){
            Log.e(LOG_TAG, "Error parsing publication date", e);
        }

        return new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(date);
    }
}
