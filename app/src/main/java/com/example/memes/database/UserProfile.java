package com.example.memes.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profiles")
public class UserProfile {

    @PrimaryKey
    @NonNull
    public String username;  // Unique identifier — must not be null

    public String name;
    public String email;
    public String dob;
    public String age;
    public String gender;
    public String phone;
    public String address;
    public String imageUri;

    public UserProfile(@NonNull String username, String name, String email, String dob, String age,
                       String gender, String phone, String address, String imageUri) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.imageUri = imageUri;
    }
}
