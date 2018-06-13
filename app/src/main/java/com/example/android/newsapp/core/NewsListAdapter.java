package com.example.android.newsapp.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.newsapp.R;
import com.example.android.newsapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsItemViewHolder> {

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
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_news_item, parent, false);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemViewHolder holder, int position) {

        final NewsItem newsItem = newsList.get(position);

        holder.sectionTextView.setText(newsItem.getSection());
        holder.titleTextView.setText(newsItem.getTitle());

        String author = newsItem.getAuthor();
        String date = newsItem.getDate();

        if(author != null){
            holder.authorTextView.setVisibility(View.VISIBLE);
            holder.authorTextView.setText(author);
        }

        if(date != null){
            holder.dateTextView.setVisibility(View.VISIBLE);
            holder.dateTextView.setText(date);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newsItemUri = Uri.parse(newsItem.getUrl());

                Intent browserImplicitIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);

                if (browserImplicitIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(browserImplicitIntent);
                } else {
                    Toast.makeText(context, "No intent available to handle this action", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
