package com.laba.arkanoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView highestScoreTextView;
    private Button playButton;
    private Button languageSwitchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String currentLanguage = loadLanguagePreference();
        setLocale(currentLanguage);

        setContentView(R.layout.activity_main);


        playButton = findViewById(R.id.playButton);
        languageSwitchButton = findViewById(R.id.languageSwitchButton);
        highestScoreTextView = findViewById(R.id.highestScoreTextView);

        int highestScore = loadHighestScore();
        highestScoreTextView.setText(getResources().getString(R.string.high_score) + highestScore);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        languageSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();
            }
        });
    }

    private void switchLanguage() {
        String currentLanguage = loadLanguagePreference();
        String newLanguage = "uk";

        switch (currentLanguage) {
            case "uk":
                newLanguage = "en";
                break;
            case "en":
                newLanguage = "de";
                break;
            case "de":
                newLanguage = "uk";
                break;
        }

        saveLanguagePreference(newLanguage);

        setLocale(newLanguage);

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private String loadLanguagePreference() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        return preferences.getString("language", "uk");  // Default language is 'uk'
    }

    private void saveLanguagePreference(String language) {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", language);
        editor.apply();
    }

    private int loadHighestScore() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        return preferences.getInt("highest_score", 0);  // Default is 0 if no score is saved
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
