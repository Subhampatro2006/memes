package com.example.memes.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemeDao {

    @Insert
    void insert(MemeEntity meme);

    @Query("SELECT * FROM liked_memes")
    List<MemeEntity> getAllLikedMemes();
}
