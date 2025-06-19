package com.example.memes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ImageView imgMeme;
    private Button btnPrev, btnNext, btnLike, btnShare, btnLikedMemes;
    private TextView tvCounter;

    private List<String> memeUrls = new ArrayList<>();
    private int currentIndex = 0;
    private Set<String> likedMemes = new HashSet<>();
    private SharedPreferences preferences;

    private List<String> allMemes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        loadMemeUrls();
        allMemes.addAll(memeUrls);

        preferences = getSharedPreferences("liked_memes", MODE_PRIVATE);
        likedMemes = new HashSet<>(preferences.getStringSet("urls", new HashSet<>()));

        Intent intent = getIntent();
        if (intent.hasExtra("preview_url")) {
            String previewUrl = intent.getStringExtra("preview_url");
            if (allMemes.contains(previewUrl)) {
                memeUrls = new ArrayList<>(allMemes);
                currentIndex = allMemes.indexOf(previewUrl);
            } else {
                Toast.makeText(this, "Preview meme not found!", Toast.LENGTH_SHORT).show();
                currentIndex = 0;
            }
        }

        displayMeme();

        btnNext.setOnClickListener(v -> {
            if (currentIndex < memeUrls.size() - 1) {
                currentIndex++;
                displayMeme();
            } else {
                Toast.makeText(this, "🎉 You've seen all memes!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayMeme();
            }
        });

        btnLike.setOnClickListener(v -> {
            String url = memeUrls.get(currentIndex);
            if (likedMemes.contains(url)) {
                likedMemes.remove(url);
                Toast.makeText(this, "Removed from liked", Toast.LENGTH_SHORT).show();
            } else {
                likedMemes.add(url);
                Toast.makeText(this, "Added to liked", Toast.LENGTH_SHORT).show();
            }
            updateLikeButton(url);
            saveLikedMemes();
        });

        btnShare.setOnClickListener(v -> shareMeme(memeUrls.get(currentIndex)));

        btnLikedMemes.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LikedMemesActivity.class);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 🔁 Refresh liked memes from preferences
        likedMemes = new HashSet<>(preferences.getStringSet("urls", new HashSet<>()));

        // 🔁 Update like button if current meme is still valid
        if (!memeUrls.isEmpty()) {
            String url = memeUrls.get(currentIndex);
            updateLikeButton(url);
        }
    }

    private void initializeViews() {
        imgMeme = findViewById(R.id.imgMeme);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnLike = findViewById(R.id.btnLike);
        btnShare = findViewById(R.id.btnShare);
        btnLikedMemes = findViewById(R.id.btnLikedMemes);
        tvCounter = findViewById(R.id.tvCounter);
    }

    private void loadMemeUrls() {
        memeUrls.add("https://i.pinimg.com/736x/8b/b4/5b/8bb45b03e2c7f160d2479c22d169ad35.jpg");
        memeUrls.add("https://i.pinimg.com/736x/ae/56/d7/ae56d70dfa8dfe7ae9f5cceefadee89a.jpg");
        memeUrls.add("https://i.pinimg.com/736x/da/d0/ad/dad0ad873e37cfb506169acde9c15f56.jpg");
        memeUrls.add("https://i.pinimg.com/736x/e9/18/8c/e9188cc54fedaf51e6307ecbb70874de.jpg");
        memeUrls.add("https://i.pinimg.com/736x/28/23/e1/2823e125f27e7f7d9c9510f31f11afbd.jpg");
        memeUrls.add("https://i.pinimg.com/736x/53/90/24/5390248c09f903d72a72e6a240eb72b6.jpg");
        memeUrls.add("https://i.pinimg.com/736x/9e/0a/8b/9e0a8bf1e6b0f76a2c0525ad97a9a9bd.jpg");
        memeUrls.add("https://i.pinimg.com/736x/a6/a2/c8/a6a2c8f07bb94a8a3292c1597f5d52ef.jpg");
        memeUrls.add("https://i.pinimg.com/736x/79/da/23/79da23ada30a8f5952d72d4dd9bbd05e.jpg");
        memeUrls.add("https://i.pinimg.com/736x/67/10/19/671019fb7bee9cbf524af6a3caa44e26.jpg");
        memeUrls.add("https://i.pinimg.com/736x/3c/05/1e/3c051ee61fea84527fbc63bd99039d69.jpg");
        memeUrls.add("https://i.pinimg.com/736x/95/c2/42/95c24232b1d2dc25855b6d8d46db7507.jpg");
        memeUrls.add("https://i.pinimg.com/736x/1a/7b/e7/1a7be7da5538b967610c194f1dfc764b.jpg");
        memeUrls.add("https://i.pinimg.com/736x/36/0a/11/360a11a38a6d7ceb06396df380b22531.jpg");
        memeUrls.add("https://i.pinimg.com/736x/29/85/a5/2985a5b18fd40b1c306e4e06499887af.jpg");
        memeUrls.add("https://i.pinimg.com/736x/db/d6/03/dbd60395877b06cd4242e6ce064f532a.jpg");
        memeUrls.add("https://i.pinimg.com/736x/f2/98/4b/f2984bd7641c8c876fccda59c95b0021.jpg");
        memeUrls.add("https://i.pinimg.com/736x/68/fa/88/68fa88e6c907010e62e6b7a7beaa8695.jpg");
        memeUrls.add("https://i.pinimg.com/736x/65/58/43/6558435c8308c052d327b16166391222.jpg");
        memeUrls.add("https://i.pinimg.com/736x/bb/b0/08/bbb0085fb9a50cf2897d39dc631d38c6.jpg");
    }

    private void displayMeme() {
        if (memeUrls.isEmpty()) return;

        String currentUrl = memeUrls.get(currentIndex);

        Glide.with(this)
                .load(currentUrl)
                .into(imgMeme);

        updateLikeButton(currentUrl);
        tvCounter.setText(String.format("#%02d", currentIndex + 1));
    }

    private void updateLikeButton(String url) {
        btnLike.setText(likedMemes.contains(url) ? "Unlike 🗑️" : "Like ❤️");
    }

    private void saveLikedMemes() {
        preferences.edit().putStringSet("urls", likedMemes).apply();
    }

    private void shareMeme(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            File cachePath = new File(getCacheDir(), "images");
                            cachePath.mkdirs();
                            File file = new File(cachePath, "shared_meme.jpg");
                            FileOutputStream out = new FileOutputStream(file);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.close();

                            Uri contentUri = FileProvider.getUriForFile(MainActivity.this,
                                    getPackageName() + ".provider", file);

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(shareIntent, "Share meme via"));
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Share failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {}
                });
    }
}
