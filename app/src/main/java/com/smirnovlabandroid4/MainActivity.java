package com.smirnovlabandroid4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.smirnovlabandroid4.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        InputStream articles = getResources().openRawResource(R.raw.articles);
        LinearLayoutManager viewManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(viewManager);
        try {
            adapter = new Adapter(articles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.recyclerView.setAdapter(adapter);
    }
}