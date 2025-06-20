package com.example.memes.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserProfile userProfile);

    @Query("SELECT * FROM user_profiles LIMIT 1")
    UserProfile getProfile();

    @Query("DELETE FROM user_profiles")
    void clearProfile();
}
