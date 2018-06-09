package com.example.android.newsapp.core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.R;
import com.example.android.newsapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private Context context;
    private List<NewsItem> newsList;

    public NewsListAdapter(Context context){
        this.context = context;
        newsList = new ArrayList<>();
    }

    public void setNews(List<NewsItem> newsList){
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final NewsItem newsItem = newsList.get(position);

        holder.section.setText(newsItem.getSection());
        holder.title.setText(newsItem.getTitle());

        String author = newsItem.getAuthor();
        String date = newsItem.getDate();

        if(author != null){
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText(author);
        }

        if(date != null){
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(date);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newsItemUri = Uri.parse(newsItem.getUrl());
                context.startActivity(new Intent(Intent.ACTION_VIEW, newsItemUri));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView section;
        private TextView title;
        private TextView author;
        private TextView date;

        private ViewHolder(View itemView) {
            super(itemView);

            section = itemView.findViewById(R.id.news_item_section);
            title = itemView.findViewById(R.id.news_item_title);
            author = itemView.findViewById(R.id.news_item_author);
            date = itemView.findViewById(R.id.news_item_date);
        }
    }
}
