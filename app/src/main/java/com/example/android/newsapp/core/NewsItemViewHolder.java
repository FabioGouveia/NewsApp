package com.example.android.newsapp.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.newsapp.R;

class NewsItemViewHolder extends RecyclerView.ViewHolder {

    TextView sectionTextView;
    TextView titleTextView;
    TextView authorTextView;
    TextView dateTextView;

    NewsItemViewHolder(View itemView) {
        super(itemView);

        sectionTextView = itemView.findViewById(R.id.news_item_section_text_view);
        titleTextView = itemView.findViewById(R.id.news_item_title_text_view);
        authorTextView = itemView.findViewById(R.id.news_item_author_text_view);
        dateTextView = itemView.findViewById(R.id.news_item_date_text_view);
    }
}
