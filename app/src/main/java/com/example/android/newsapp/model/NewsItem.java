package com.example.android.newsapp.model;

public class NewsItem {

    private String title;
    private String section;
    private String author;
    private String date;
    private String url;

    public NewsItem(String title, String section, String url){
        this.title = title;
        this.section = section;
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
