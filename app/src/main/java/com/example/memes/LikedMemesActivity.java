package com.example.memes;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LikedMemesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemeAdapter adapter;
    private List<String> likedMemesList = new ArrayList<>();
    private SharedPreferences preferences;
    private Set<String> likedSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_memes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.likedMemesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        preferences = getSharedPreferences("liked_memes", MODE_PRIVATE);
        likedSet = preferences.getStringSet("urls", new HashSet<>());

        if (likedSet == null || likedSet.isEmpty()) {
            Toast.makeText(this, "No liked memes!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        likedMemesList.addAll(likedSet);
        adapter = new MemeAdapter(this, likedMemesList, this::removeMeme);
        recyclerView.setAdapter(adapter);
    }

    private void removeMeme(String url) {
        likedMemesList.remove(url);
        likedSet.remove(url);
        preferences.edit().putStringSet("urls", likedSet).apply();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Meme removed", Toast.LENGTH_SHORT).show();

        if (likedMemesList.isEmpty()) {
            Toast.makeText(this, "All memes removed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.liked_memes_menu, menu);
        MenuItem deleteAll = menu.findItem(R.id.menu_delete_all);
        SpannableString s = new SpannableString("Delete All");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
        deleteAll.setTitle(s);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_delete_all) {
            confirmDeleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteAll() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All")
                .setMessage("Are you sure you want to delete all liked memes?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    likedSet.clear();
                    likedMemesList.clear();
                    preferences.edit().remove("urls").apply();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "All memes deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
