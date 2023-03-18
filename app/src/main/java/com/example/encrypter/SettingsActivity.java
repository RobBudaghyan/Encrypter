package com.example.encrypter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    int THEME_COLOR = 0;
    // global values
    int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    String INPUT;
    int BARCODE_INDEX = -1;

    // key values
    int[] KEY1 = {0,0,0};
    int[] KEY2 = {0,0,0};
    int[] KEY3 = {0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView orange_theme = findViewById(R.id.orange_theme);
        ImageView blue_theme = findViewById(R.id.blue_theme);
        ImageView red_theme = findViewById(R.id.red_theme);
        ImageView green_theme = findViewById(R.id.green_theme);
        ImageView reset = findViewById(R.id.reset_btn);
        ImageView open_key_1 = findViewById(R.id.open_key_1_btn);
        ImageView open_key_2 = findViewById(R.id.open_key_2_btn);
        ImageView open_key_3 = findViewById(R.id.open_key_3_btn);
        ImageView change_key_1 = findViewById(R.id.change_key_1_btn);
        ImageView change_key_2 = findViewById(R.id.change_key_2_btn);
        ImageView change_key_3 = findViewById(R.id.change_key_3_btn);
        SwitchCompat switch_sounds = findViewById(R.id.switch_sounds);
        SwitchCompat switch_hexadecimal = findViewById(R.id.switch_hexadecimal);

        // bottom navigation update
        bottomNavigation();

        // update shared preferences
        updatePrefs();

        // applying theme color
        setTheme();

        // get intent-extra values for updating fields
        updateGlobals();

        // orange theme button
        orange_theme.setOnClickListener(v -> {
            THEME_COLOR = 0;
            savePrefsToSharedPreferences();
            setTheme();
            makeClickSound();
        });
        // blue theme button
        blue_theme.setOnClickListener(v -> {
            THEME_COLOR = 1;
            savePrefsToSharedPreferences();
            setTheme();
            makeClickSound();
        });
        // red theme button
        red_theme.setOnClickListener(v -> {
            THEME_COLOR = 2;
            savePrefsToSharedPreferences();
            setTheme();
            makeClickSound();
        });
        // green theme button
        green_theme.setOnClickListener(v -> {
            THEME_COLOR = 3;
            savePrefsToSharedPreferences();
            setTheme();
            makeClickSound();
        });

        // hexadecimal encryption switch
        switch_hexadecimal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePrefsToSharedPreferences();
            makeClickSound();
        });

        // sounds switch
        switch_sounds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePrefsToSharedPreferences();
            openActivity(SettingsActivity.class);
        });

        // reset settings button
        reset.setOnClickListener(view -> {
            for (int i = 0; i < 3; i++) {
                resetPrefs();
                updatePrefs();
                setTheme();
            }
            resetKeys();
            updateKeys();
            updateKeyTextViews();
        });


        // update keys and its texts
        updateKeys();
        updateKeyTextViews();

        // change values of selected key
        change_key_1.setOnClickListener(v -> changeKey(0));
        change_key_2.setOnClickListener(v -> changeKey(1));
        change_key_3.setOnClickListener(v -> changeKey(2));

        // open home with values of selected key
        open_key_1.setOnClickListener(v -> {
            VAL1 = KEY1[0];
            VAL2 = KEY1[1];
            VAL3 = KEY1[2];
            openActivity(HomeActivity.class);
        });
        open_key_2.setOnClickListener(v -> {
            VAL1 = KEY2[0];
            VAL2 = KEY2[1];
            VAL3 = KEY2[2];
            openActivity(HomeActivity.class);
        });
        open_key_3.setOnClickListener(v -> {
            VAL1 = KEY3[0];
            VAL2 = KEY3[1];
            VAL3 = KEY3[2];
            openActivity(HomeActivity.class);
        });



    }

    // save preferences after destroying activity
    @Override
    protected void onDestroy() {
        savePrefsToSharedPreferences();
        updateKeys();
        saveKeysToSharedPreferences();
        super.onDestroy();
    }

    // update key texts
    private void updateKeyTextViews(){
        TextView key_1_values = findViewById(R.id.key_1_values);
        TextView key_2_values = findViewById(R.id.key_2_values);
        TextView key_3_values = findViewById(R.id.key_3_values);

        String str = "";
        str += String.valueOf(KEY1[0]) + ":" + String.valueOf(KEY1[1]) + ":" + String.valueOf(KEY1[2]);
        key_1_values.setText(str);

        str = "";
        str += String.valueOf(KEY2[0]) + ":" + String.valueOf(KEY2[1]) + ":" + String.valueOf(KEY2[2]);
        key_2_values.setText(str);
        str = "";

        str += String.valueOf(KEY3[0]) + ":" + String.valueOf(KEY3[1]) + ":" + String.valueOf(KEY3[2]);
        key_3_values.setText(str);
    }

    // update all globals
    private void updateGlobals(){
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            INPUT = intent.getExtras().getString("input_text");
            VAL1 = intent.getExtras().getInt("val_1");
            VAL2 = intent.getExtras().getInt("val_2");
            VAL3 = intent.getExtras().getInt("val_3");
            BARCODE_INDEX = intent.getExtras().getInt("barcode_index");
        }
    }

    // change selected key
    private void changeKey(int index){
        switch(index){
            case 0:
                KEY1[0] = VAL1;
                KEY1[1] = VAL2;
                KEY1[2] = VAL3;
                break;
            case 1:
                KEY2[0] = VAL1;
                KEY2[1] = VAL2;
                KEY2[2] = VAL3;
                break;
            case 2:
                KEY3[0] = VAL1;
                KEY3[1] = VAL2;
                KEY3[2] = VAL3;
                break;
        }
        saveKeysToSharedPreferences();
        updateKeys();
        updateKeyTextViews();
    }

    // update all keys from shared preferences
    private void updateKeys(){
        SharedPreferences prefs = getSharedPreferences("SAVED_KEYS", MODE_PRIVATE);

        KEY1 = new int[]{prefs.getInt("key1_val1",0),
                prefs.getInt("key1_val2",0), prefs.getInt("key1_val3",0)};

        KEY2 = new int[]{prefs.getInt("key2_val1",0),
                prefs.getInt("key2_val2",0), prefs.getInt("key2_val3",0)};

        KEY3 = new int[]{prefs.getInt("key3_val1",0),
                prefs.getInt("key3_val2",0), prefs.getInt("key3_val3",0)};
    }

    // save values to shared preferences
    private void saveKeysToSharedPreferences(){
        SharedPreferences.Editor editor = getSharedPreferences("SAVED_KEYS", MODE_PRIVATE).edit();
        editor.putInt("key1_val1", KEY1[0]);
        editor.putInt("key1_val2", KEY1[1]);
        editor.putInt("key1_val3", KEY1[2]);

        editor.putInt("key2_val1", KEY2[0]);
        editor.putInt("key2_val2", KEY2[1]);
        editor.putInt("key2_val3", KEY2[2]);

        editor.putInt("key3_val1", KEY3[0]);
        editor.putInt("key3_val2", KEY3[1]);
        editor.putInt("key3_val3", KEY3[2]);

        editor.apply();
    }

    // reset all keys
    private void resetKeys(){
        SharedPreferences.Editor editor = getSharedPreferences("SAVED_KEYS", MODE_PRIVATE).edit();
        editor.putInt("key1_val1", 0);
        editor.putInt("key1_val2", 0);
        editor.putInt("key1_val3", 0);

        editor.putInt("key2_val1", 0);
        editor.putInt("key2_val2", 0);
        editor.putInt("key2_val3", 0);

        editor.putInt("key3_val1", 0);
        editor.putInt("key3_val2", 0);
        editor.putInt("key3_val3", 0);

        editor.apply();
    }

    // save preferences in shared preferences
    private void savePrefsToSharedPreferences(){
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
        VAL1 = 0;
        VAL2 = 0;
        VAL3 = 0;
        INPUT = "";
        BARCODE_INDEX = 0;
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
        i.putExtra("barcode_index",BARCODE_INDEX);
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

    // make click sound
    private void makeClickSound(){
        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");
        if(sounds_on) clickSound.start();
    }

    // bottom navigation bar
    @SuppressLint("NonConstantResourceId")
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

        ImageView open_key_1 = findViewById(R.id.open_key_1_btn);
        ImageView open_key_2 = findViewById(R.id.open_key_2_btn);
        ImageView open_key_3 = findViewById(R.id.open_key_3_btn);
        ImageView change_key_1 = findViewById(R.id.change_key_1_btn);
        ImageView change_key_2 = findViewById(R.id.change_key_2_btn);
        ImageView change_key_3 = findViewById(R.id.change_key_3_btn);

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

                open_key_1.setBackgroundResource(R.drawable.open_btn_orange);
                open_key_2.setBackgroundResource(R.drawable.open_btn_orange);
                open_key_3.setBackgroundResource(R.drawable.open_btn_orange);
                change_key_1.setBackgroundResource(R.drawable.change_btn_orange);
                change_key_2.setBackgroundResource(R.drawable.change_btn_orange);
                change_key_3.setBackgroundResource(R.drawable.change_btn_orange);

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

                open_key_1.setBackgroundResource(R.drawable.open_btn_blue);
                open_key_2.setBackgroundResource(R.drawable.open_btn_blue);
                open_key_3.setBackgroundResource(R.drawable.open_btn_blue);
                change_key_1.setBackgroundResource(R.drawable.change_btn_blue);
                change_key_2.setBackgroundResource(R.drawable.change_btn_blue);
                change_key_3.setBackgroundResource(R.drawable.change_btn_blue);

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

                open_key_1.setBackgroundResource(R.drawable.open_btn_red);
                open_key_2.setBackgroundResource(R.drawable.open_btn_red);
                open_key_3.setBackgroundResource(R.drawable.open_btn_red);
                change_key_1.setBackgroundResource(R.drawable.change_btn_red);
                change_key_2.setBackgroundResource(R.drawable.change_btn_red);
                change_key_3.setBackgroundResource(R.drawable.change_btn_red);

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

                open_key_1.setBackgroundResource(R.drawable.open_btn_green);
                open_key_2.setBackgroundResource(R.drawable.open_btn_green);
                open_key_3.setBackgroundResource(R.drawable.open_btn_green);
                change_key_1.setBackgroundResource(R.drawable.change_btn_green);
                change_key_2.setBackgroundResource(R.drawable.change_btn_green);
                change_key_3.setBackgroundResource(R.drawable.change_btn_green);

                orange_theme.setBackgroundResource(R.drawable.orange_theme);
                blue_theme.setBackgroundResource(R.drawable.blue_theme);
                red_theme.setBackgroundResource(R.drawable.red_theme);
                green_theme.setBackgroundResource(R.drawable.green_theme_pressed);
                break;
        }
    }

}