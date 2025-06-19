package com.example.memes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imgMeme;
    private Button btnPrev, btnNext, btnLike;
    private TextView tvCounter;

    private List<String> memeUrls = new ArrayList<>();
    private int currentIndex = 0;
    private boolean[] liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgMeme = findViewById(R.id.imgMeme);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnLike = findViewById(R.id.btnLike);
        tvCounter = findViewById(R.id.tvCounter);

        loadMemeUrls();
        liked = new boolean[memeUrls.size()];
        displayMeme();

        btnNext.setOnClickListener(v -> {
            if (currentIndex < memeUrls.size() - 1) {
                currentIndex++;
                displayMeme();
            } else {
                Toast.makeText(this, "🎉 Congratulations! You've completed all the memes!", Toast.LENGTH_LONG).show();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayMeme();
            }
        });

        btnLike.setOnClickListener(v -> {
            liked[currentIndex] = !liked[currentIndex];
            btnLike.setText(liked[currentIndex] ? "Liked ❤️" : "Like");
        });
    }

    private void displayMeme() {
        Glide.with(this)
                .load(memeUrls.get(currentIndex))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgMeme);

        btnLike.setText(liked[currentIndex] ? "Liked ❤️" : "Like");

        // Update counter text like #001
        String memeNumber = String.format("#%03d", currentIndex + 1);
        tvCounter.setText(memeNumber);
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
        // Expand to 500 memes using API or JSON file
    }
}