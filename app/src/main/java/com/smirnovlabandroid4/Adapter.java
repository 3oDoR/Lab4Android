package com.smirnovlabandroid4;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smirnovlabandroid4.databinding.BiblibEntryBinding;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import name.ank.lab4.BibDatabase;
import name.ank.lab4.BibEntry;
import name.ank.lab4.Keys;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    BibDatabase database;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView journalAndYear;
        TextView url;
        TextView pages;


        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            journalAndYear = view.findViewById(R.id.JournalAndYear);
            url = view.findViewById(R.id.url);
            pages = view.findViewById(R.id.pages);
        }
    }

    Adapter(InputStream articles) throws IOException {
        InputStreamReader reader = new InputStreamReader(articles);
        database = new BibDatabase(reader);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BiblibEntryBinding binding = BiblibEntryBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }
    int counter = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BibEntry entry = database.getEntry(position % database.size());

        viewHolder.title.setText(entry.getField(Keys.TITLE));
        viewHolder.author.setText("Authors : " + entry.getField(Keys.AUTHOR));
        viewHolder.journalAndYear.setText("Journal : " + entry.getField(Keys.JOURNAL) + " , " + entry.getField(Keys.YEAR));
        viewHolder.url.setText("URL : " + entry.getField(Keys.URL));
        viewHolder.pages.setText("Pages : " + entry.getField(Keys.PAGES));


    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}