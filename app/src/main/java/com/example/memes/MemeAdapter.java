package com.example.memes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.MemeViewHolder> {

    private final Context context;
    private final List<String> memeUrls;
    private final MemeDeleteListener deleteListener;

    public interface MemeDeleteListener {
        void onDelete(String url);
    }

    public MemeAdapter(Context context, List<String> memeUrls, MemeDeleteListener deleteListener) {
        this.context = context;
        this.memeUrls = memeUrls;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public MemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_liked_meme, parent, false);
        return new MemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeViewHolder holder, int position) {
        String url = memeUrls.get(position);

        // Set meme image
        Glide.with(context)
                .load(url)
                .into(holder.imgMeme);

        // Set meme number
        holder.tvNumber.setText(String.format("#%02d", position + 1));

        // Delete action
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(url);
            }
        });

        // On click open in MainActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("preview_url", url);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return memeUrls.size();
    }

    public static class MemeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMeme;
        Button btnDelete;
        TextView tvNumber;

        public MemeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMeme = itemView.findViewById(R.id.imgMeme);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvNumber = itemView.findViewById(R.id.tvNumber);
        }
    }
}