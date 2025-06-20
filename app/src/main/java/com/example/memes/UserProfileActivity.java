package com.example.memes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.memes.database.AppDatabase;
import com.example.memes.database.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressWarnings("rawtypes")
public class UserProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private EditText edtName, edtEmail, edtDob, edtAge, edtUsername, edtPhone, edtAddress;
    private Spinner spinnerGender;
    private Button btnSave, btnLogout;

    private final Calendar calendar = Calendar.getInstance();
    private String imageUriString = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setupToolbar();
        initViews();
        setupGenderSpinner();
        initDatabase();
        setupImagePicker();
        loadProfileFromDB();

        edtDob.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveProfileToDB());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initViews() {
        imgProfile = findViewById(R.id.imgProfile);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtDob = findViewById(R.id.edtDob);
        edtAge = findViewById(R.id.edtAge);
        edtUsername = findViewById(R.id.edtUsername);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }

    private void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            imageUriString = imageUri.toString();
                            Glide.with(this).load(imageUri).into(imgProfile);
                        }
                    }
                });

        imgProfile.setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(pickIntent);
        });
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            String dob = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
            edtDob.setText(dob);
            calculateAge(year, month, dayOfMonth);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void calculateAge(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - year;
        if (today.get(Calendar.MONTH) < month ||
                (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) < day)) {
            age--;
        }
        edtAge.setText(String.valueOf(age));
    }

    private void saveProfileToDB() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String age = edtAge.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || age.isEmpty() || username.isEmpty() ||
                gender.equals("Select Gender") || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile profile = new UserProfile(username, name, email, dob, age, gender, phone, address, imageUriString);
        db.userProfileDao().insert(profile);

        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity
        startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
        finish();
    }

    private void loadProfileFromDB() {
        UserProfile profile = db.userProfileDao().getProfile();
        if (profile != null) {
            edtName.setText(profile.name);
            edtEmail.setText(profile.email);
            edtDob.setText(profile.dob);
            edtAge.setText(profile.age);
            edtUsername.setText(profile.username);
            edtPhone.setText(profile.phone);
            edtAddress.setText(profile.address);
            imageUriString = profile.imageUri;

            if (profile.gender != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerGender.getAdapter();

                if (adapter != null) {
                    int position = adapter.getPosition(profile.gender);
                    spinnerGender.setSelection(position);
                }
            }

            if (imageUriString != null) {
                Glide.with(this).load(Uri.parse(imageUriString)).into(imgProfile);
            }
        }
    }

    private void logout() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
