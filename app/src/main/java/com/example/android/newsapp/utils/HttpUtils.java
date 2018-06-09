package com.example.android.newsapp.utils;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.newsapp.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

final class HttpUtils {

    private final static String LOG_TAG = HttpUtils.class.getName();

    private final static String API_URL = BuildConfig.API_URL;
    private final static String API_KEY = BuildConfig.API_KEY;
    private final static String API_SEARCH_PATH = "search";
    private final static String API_RESPONSE_FORMAT = "json";
    private final static String API_QUERY_TERM = "android";
    private final static String API_SHOW_TAGS = "contributor";
    private final static String API_ORDER_BY = "newest";

    @Nullable
    static String requestNews() {

        String response = null;

        try{
            response = executeRequest(createQuery());
        }catch (IOException e){
            Log.e(LOG_TAG, "Error parsing http request", e);
        }
        return response;
    }

    private static String createQuery() {
        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(API_SEARCH_PATH);
        uriBuilder.appendQueryParameter("q", API_QUERY_TERM);
        uriBuilder.appendQueryParameter("format", API_RESPONSE_FORMAT);
        uriBuilder.appendQueryParameter("show-tags", API_SHOW_TAGS);
        uriBuilder.appendQueryParameter("order-by", API_ORDER_BY);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return uriBuilder.toString();
    }

    private static String executeRequest(String query) throws IOException {

        String response = null;
        HttpsURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpsURLConnection) createURL(query).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();

            inputStream = connection.getInputStream();
            if (inputStream != null) {
                response = readFromStream(inputStream);
            }else{
                Log.i(LOG_TAG, "Null InputStream..!");
            }
        } catch (IOException e) {
            Log.i(LOG_TAG, "Error with connection request");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return response;
    }

    private static URL createURL(String url) throws MalformedURLException {
        return new URL(url);
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            inputStreamReader.close();
            reader.close();
        }

        return output.toString();
    }
}
