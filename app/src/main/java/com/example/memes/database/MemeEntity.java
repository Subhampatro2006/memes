package com.example.memes.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "liked_memes")
public class MemeEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String url;

    public MemeEntity(String url) {
        this.url = url;
    }
}
