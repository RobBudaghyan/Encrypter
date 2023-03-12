package com.example.encrypter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    int THEME_COLOR = 0;
    // global values for rotor seekbars
    int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    // global value for input text
    String INPUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView orange_theme = findViewById(R.id.orange_theme);
        ImageView blue_theme = findViewById(R.id.blue_theme);
        ImageView red_theme = findViewById(R.id.red_theme);
        ImageView green_theme = findViewById(R.id.green_theme);
        ImageView reset = findViewById(R.id.reset_btn);
        SwitchCompat switch_sounds = findViewById(R.id.switch_sounds);
        SwitchCompat switch_hexadecimal = findViewById(R.id.switch_hexadecimal);

        // bottom navigation update
        bottomNavigation();

        // update shared preferences
        updatePrefs();

        // applying theme color
        setTheme();

        // orange theme button
        orange_theme.setOnClickListener(v -> {
            THEME_COLOR = 0;
            savePrefs();
            setTheme();
            makeClickSound();
        });

        // blue theme button
        blue_theme.setOnClickListener(v -> {
            THEME_COLOR = 1;
            savePrefs();
            setTheme();
            makeClickSound();
        });

        // red theme button
        red_theme.setOnClickListener(v -> {
            THEME_COLOR = 2;
            savePrefs();
            setTheme();
            makeClickSound();
        });

        // green theme button
        green_theme.setOnClickListener(v -> {
            THEME_COLOR = 3;
            savePrefs();
            setTheme();
            makeClickSound();
        });


        // reset settings button
        reset.setOnClickListener(view -> {
            for (int i = 0; i < 3; i++) {
                resetPrefs();
                updatePrefs();
                setTheme();
            }
        });

        // hexadecimal encryption switch
        switch_hexadecimal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePrefs();
            makeClickSound();
        });

        // sounds switch
        switch_sounds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePrefs();
            Intent i = new Intent(SettingsActivity.this, SettingsActivity.class);
            i.putExtra("val_1", VAL1);
            i.putExtra("val_2", VAL2);
            i.putExtra("val_3", VAL3);
            i.putExtra("input_text", INPUT);
            startActivity(i);
            overridePendingTransition(0,0);
            finish();
        });

        // get intent-extra values for updating fields
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            INPUT = intent.getExtras().getString("input_text");
            VAL1 = intent.getExtras().getInt("val_1");
            VAL2 = intent.getExtras().getInt("val_2");
            VAL3 = intent.getExtras().getInt("val_3");
        }

    }

    // save preferences after destroying activity
    @Override
    protected void onDestroy() {
        savePrefs();
        super.onDestroy();
    }

    // save preferences in shared preferences
    private void savePrefs(){
        SwitchCompat switch_sounds = findViewById(R.id.switch_sounds);
        SwitchCompat switch_hexadecimal = findViewById(R.id.switch_hexadecimal);

        SharedPreferences.Editor editor = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE).edit();

        editor.putInt("color_id", THEME_COLOR);
        editor.putBoolean("sounds_on",switch_sounds.isChecked());
        editor.putBoolean("hex_on",switch_hexadecimal.isChecked());

        editor.apply();
    }

    // reset all preferences
    private void resetPrefs(){
        SharedPreferences.Editor editor = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE).edit();
        editor.clear();
        editor.putInt("color_id", 0);
        editor.putBoolean("sounds_on",false);
        editor.putBoolean("hex_on",false);
        editor.apply();
    }

    // update the preferences for app
    private void updatePrefs(){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);

        SwitchCompat switch_sounds = findViewById(R.id.switch_sounds);
        SwitchCompat switch_hexadecimal = findViewById(R.id.switch_hexadecimal);

        switch_sounds.setChecked(prefs.getBoolean("sounds_on",false));
        switch_hexadecimal.setChecked(prefs.getBoolean("hex_on",false));
    }

    // open activity with class name given to it
    private void openActivity(Class activity_class){
        // send field values to new activity
        Intent i = new Intent(SettingsActivity.this, activity_class);
        i.putExtra("val_1", VAL1);
        i.putExtra("val_2", VAL2);
        i.putExtra("val_3", VAL3);
        i.putExtra("input_text", INPUT);
        startActivity(i);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    // return integer from shared preferences with given key
    private int getPrefsInt(String key){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);
        return prefs.getInt(key,0);
    }

    // return boolean from shared preferences with given key
    private boolean getPrefsBoolean(String key){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);
        return prefs.getBoolean(key,false);
    }

    private void makeClickSound(){
        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");
        if(sounds_on) clickSound.start();
    }

    // bottom navigation bar
    private void bottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.settings_menu);
        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(sounds_on) clickSound.start();
            switch (item.getItemId()) {
                case R.id.home_menu:
                    openActivity(HomeActivity.class);
                    return true;
                case R.id.qr_menu:
                    openActivity(QRActivity.class);
                    return true;
                case R.id.help_menu:
                    openActivity(HelpActivity.class);
                    return true;
                case R.id.settings_menu:
                    return true;
            }

            return false;
        });
    }

    // update color theme of app using shared preferences
    private void setTheme(){
        ImageView reset = findViewById(R.id.reset_btn);
        SwitchCompat switch_sounds = findViewById(R.id.switch_sounds);
        SwitchCompat switch_hexadecimal = findViewById(R.id.switch_hexadecimal);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        ImageView orange_theme = findViewById(R.id.orange_theme);
        ImageView blue_theme = findViewById(R.id.blue_theme);
        ImageView red_theme = findViewById(R.id.red_theme);
        ImageView green_theme = findViewById(R.id.green_theme);

        int [][] states = new int [][]{
                new int[] { android.R.attr.state_enabled, -android.R.attr.state_pressed, -android.R.attr.state_selected}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled, android.R.attr.state_selected}, // selected
                new int[] {android.R.attr.state_enabled, android.R.attr.state_pressed}  // pressed
        };
        int[] colors_orange = new int[0], colors_blue = new int[0], colors_red = new int[0], colors_green = new int[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colors_orange = new int[]{getColor(R.color.primary_gray_light), getColor(R.color.primary_gray_light),
                    getColor(R.color.primary_orange), getColor(R.color.primary_orange)};
            colors_blue = new int[]{getColor(R.color.primary_gray_light), getColor(R.color.primary_gray_light),
                    getColor(R.color.primary_blue), getColor(R.color.primary_blue)};
            colors_red = new int[]{getColor(R.color.primary_gray_light), getColor(R.color.primary_gray_light),
                    getColor(R.color.primary_red), getColor(R.color.primary_red)};
            colors_green = new int[]{getColor(R.color.primary_gray_light), getColor(R.color.primary_gray_light),
                    getColor(R.color.primary_green), getColor(R.color.primary_green)};
        }
        ColorStateList colorStateList_orange = new ColorStateList(states,colors_orange);
        ColorStateList colorStateList_blue = new ColorStateList(states,colors_blue);
        ColorStateList colorStateList_red = new ColorStateList(states,colors_red);
        ColorStateList colorStateList_green = new ColorStateList(states,colors_green);

        switch (getPrefsInt("color_id")) {
            case 0:
                THEME_COLOR = 0;
                setTheme(R.style.Orange_Theme);
                switch_sounds.setThumbResource(R.drawable.thumb_2_orange);
                switch_hexadecimal.setThumbResource(R.drawable.thumb_2_orange);
                reset.setBackgroundResource(R.drawable.reset_btn_orange);
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);

                orange_theme.setBackgroundResource(R.drawable.orange_theme_pressed);
                blue_theme.setBackgroundResource(R.drawable.blue_theme);
                red_theme.setBackgroundResource(R.drawable.red_theme);
                green_theme.setBackgroundResource(R.drawable.green_theme);
                break;
            case 1:
                THEME_COLOR = 1;
                setTheme(R.style.Blue_Theme);
                switch_sounds.setThumbResource(R.drawable.thumb_2_blue);
                switch_hexadecimal.setThumbResource(R.drawable.thumb_2_blue);
                reset.setBackgroundResource(R.drawable.reset_btn_blue);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);

                orange_theme.setBackgroundResource(R.drawable.orange_theme);
                blue_theme.setBackgroundResource(R.drawable.blue_theme_pressed);
                red_theme.setBackgroundResource(R.drawable.red_theme);
                green_theme.setBackgroundResource(R.drawable.green_theme);
                break;
            case 2:
                THEME_COLOR = 2;
                setTheme(R.style.Red_Theme);
                switch_sounds.setThumbResource(R.drawable.thumb_2_red);
                switch_hexadecimal.setThumbResource(R.drawable.thumb_2_red);
                reset.setBackgroundResource(R.drawable.reset_btn_red);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);

                orange_theme.setBackgroundResource(R.drawable.orange_theme);
                blue_theme.setBackgroundResource(R.drawable.blue_theme);
                red_theme.setBackgroundResource(R.drawable.red_theme_pressed);
                green_theme.setBackgroundResource(R.drawable.green_theme);
                break;
            case 3:
                THEME_COLOR = 3;
                setTheme(R.style.Green_Theme);
                switch_sounds.setThumbResource(R.drawable.thumb_2_green);
                switch_hexadecimal.setThumbResource(R.drawable.thumb_2_green);
                reset.setBackgroundResource(R.drawable.reset_btn_green);
                bottomNavigationView.setItemIconTintList(colorStateList_green);
                bottomNavigationView.setItemTextColor(colorStateList_green);

                orange_theme.setBackgroundResource(R.drawable.orange_theme);
                blue_theme.setBackgroundResource(R.drawable.blue_theme);
                red_theme.setBackgroundResource(R.drawable.red_theme);
                green_theme.setBackgroundResource(R.drawable.green_theme_pressed);
                break;
        }
    }

}