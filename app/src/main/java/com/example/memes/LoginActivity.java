package com.example.memes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private CheckBox chkRemember;
    private ImageView imgFingerprint;

    private SharedPreferences prefs;
    private final String PREF_NAME = "user_data";

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private String rememberedUser = "", rememberedPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        chkRemember = findViewById(R.id.chkRemember);
        imgFingerprint = findViewById(R.id.imgFingerprint);

        // Load shared preferences
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean remember = prefs.getBoolean("remember", false);
        if (remember) {
            rememberedUser = prefs.getString("remember_user", "");
            rememberedPass = prefs.getString("remember_pass", "");
            edtUsername.setText(rememberedUser);
            edtPassword.setText(rememberedPass);
            chkRemember.setChecked(true);
        }

        setupBiometric();

        // Login button
        btnLogin.setOnClickListener(v -> loginUser());

        // Register button
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Fingerprint icon click
        imgFingerprint.setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));
    }

    private void loginUser() {
        String user = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        String savedPass = prefs.getString(user, null);
        if (savedPass != null && savedPass.equals(pass)) {

            if (chkRemember.isChecked()) {
                prefs.edit()
                        .putString("remember_user", user)
                        .putString("remember_pass", pass)
                        .putBoolean("remember", true)
                        .apply();
            } else {
                prefs.edit()
                        .remove("remember_user")
                        .remove("remember_pass")
                        .putBoolean("remember", false)
                        .apply();
            }

            prefs.edit().putString("logged_in_user", user).apply();

            startActivity(new Intent(this, WelcomeActivity.class));
            finish();

        } else {
            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBiometric() {
        // Check biometric availability
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuth = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL
        );

        if (canAuth == BiometricManager.BIOMETRIC_SUCCESS) {
            imgFingerprint.setVisibility(ImageView.VISIBLE);
        } else {
            imgFingerprint.setVisibility(ImageView.GONE);
            return;
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginActivity.this, "Biometric verified!", Toast.LENGTH_SHORT).show();

                if (!rememberedUser.isEmpty() && !rememberedPass.isEmpty()) {
                    edtUsername.setText(rememberedUser);
                    edtPassword.setText(rememberedPass);
                    loginUser();
                } else {
                    Toast.makeText(LoginActivity.this, "No saved credentials found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Set prompt UI
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use your fingerprint or device credentials to login")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

    }
}
