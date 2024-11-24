package com.laba.arkanoid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

public class GameActivity extends Activity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);

        setContentView(gameView);

        // Update the language whenever the activity is created
        gameView.updateLanguage(loadLanguagePreference());
    }
    private String loadLanguagePreference() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        return preferences.getString("language", "uk");  // Default language is 'uk'
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Ensure the GameView updates its language settings when the configuration changes
        gameView.updateLanguage(loadLanguagePreference());
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.resume();
        }
    }

    // Reset language configuration and restart activity
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Intent intent = new Intent(GameActivity.this, GameActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
