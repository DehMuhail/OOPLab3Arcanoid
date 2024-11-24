package com.laba.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button playButton; // Play button
    private Button languageSwitchButton; // Language switch button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the saved language preference
        String currentLanguage = loadLanguagePreference();
        setLocale(currentLanguage);  // Set the language based on saved preference

        setContentView(R.layout.activity_main);

        // Initialize buttons
        playButton = findViewById(R.id.playButton);
        languageSwitchButton = findViewById(R.id.languageSwitchButton);

        // Set listeners for buttons
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start GameActivity when play button is clicked
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        // Set language switch button functionality
        languageSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();  // Switch language between Ukrainian and English
            }
        });
    }

    // Function to switch between languages (Ukrainian and English in this example)
    private void switchLanguage() {
        String currentLanguage = loadLanguagePreference();
        String newLanguage = "uk";  // Default language is Ukrainian

        // Toggle between languages
        switch (currentLanguage) {
            case "uk":
                newLanguage = "en";  // If current is Ukrainian, change to English
                break;
            case "en":
                newLanguage = "de";  // If current is English, change to German
                break;
            case "de":
                newLanguage = "uk";  // If current is German, change to Ukrainian
                break;
        }

        // Save the new language choice in SharedPreferences
        saveLanguagePreference(newLanguage);

        // Apply the new language configuration
        setLocale(newLanguage);

        // Restart the activity to apply the language change
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    // Function to load the saved language preference from SharedPreferences
    private String loadLanguagePreference() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        return preferences.getString("language", "uk");  // Default language is 'uk'
    }

    // Function to save the chosen language in SharedPreferences
    private void saveLanguagePreference(String language) {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", language);
        editor.apply();
    }

    // Function to set the app's locale to the chosen language
    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
