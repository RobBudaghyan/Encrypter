package com.example.encrypter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelpActivity extends AppCompatActivity {

    int lang_selected = 0;
    // global values
    int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    String INPUT;
    int BARCODE_INDEX = -1;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ImageView eng_btn = findViewById(R.id.lang_eng);
        ImageView rus_btn = findViewById(R.id.lang_rus);
        ImageView arm_btn = findViewById(R.id.lang_arm);

        // bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.help_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            makeClickSound();
            switch (item.getItemId()){
                case R.id.home_menu:
                    openActivity(HomeActivity.class);
                    return true;
                case R.id.qr_menu:
                    openActivity(QRActivity.class);
                    return true;
                case R.id.help_menu:
                    return true;
                case R.id.settings_menu:
                    openActivity(SettingsActivity.class);
                    return true;
            }
            return false;
        });

        // theme color update
        setTheme();

        // update globals from intent
        updateGlobals();

        // english button
        eng_btn.setOnClickListener(v -> {
            eng_btn.setBackgroundResource(R.drawable.eng_flag_pressed);
            rus_btn.setBackgroundResource(R.drawable.rus_flag);
            arm_btn.setBackgroundResource(R.drawable.arm_flag);
            lang_selected = 0;
            makeClickSound();
            updateHelp();
        });

        // russian button
        rus_btn.setOnClickListener(v -> {
            eng_btn.setBackgroundResource(R.drawable.eng_flag);
            rus_btn.setBackgroundResource(R.drawable.rus_flag_pressed);
            arm_btn.setBackgroundResource(R.drawable.arm_flag);
            lang_selected = 1;
            makeClickSound();
            updateHelp();
        });

        // armenian button
        arm_btn.setOnClickListener(v -> {
            eng_btn.setBackgroundResource(R.drawable.eng_flag);
            rus_btn.setBackgroundResource(R.drawable.rus_flag);
            arm_btn.setBackgroundResource(R.drawable.arm_flag_pressed);
            lang_selected = 2;
            makeClickSound();
            updateHelp();
        });
    }

    // update guide after language select
    private void updateHelp(){
        TextView home_title = findViewById(R.id.home_title);
        TextView home_content= findViewById(R.id.home_content);
        TextView qr_title = findViewById(R.id.qr_title);
        TextView qr_content= findViewById(R.id.qr_content);
        TextView settings_title = findViewById(R.id.settings_title);
        TextView settings_content= findViewById(R.id.settings_content);

        if(lang_selected == 0){
            home_title.setText(getResources().getString(R.string.eng_home_title));
            home_content.setText(getResources().getString(R.string.eng_home_content));
            qr_title.setText(getResources().getString(R.string.eng_qr_title));
            qr_content.setText(getResources().getString(R.string.eng_qr_content));
            settings_title.setText(getResources().getString(R.string.eng_settings_title));
            settings_content.setText(getResources().getString(R.string.eng_settings_content));
        }
         if(lang_selected == 1){
            home_title.setText(getResources().getString(R.string.rus_home_title));
            home_content.setText(getResources().getString(R.string.rus_home_content));
            qr_title.setText(getResources().getString(R.string.rus_qr_title));
            qr_content.setText(getResources().getString(R.string.rus_qr_content));
            settings_title.setText(getResources().getString(R.string.rus_settings_title));
            settings_content.setText(getResources().getString(R.string.rus_settings_content));
        }
        if(lang_selected == 2){
            home_title.setText(getResources().getString(R.string.arm_home_title));
            home_content.setText(getResources().getString(R.string.arm_home_content));
            qr_title.setText(getResources().getString(R.string.arm_qr_title));
            qr_content.setText(getResources().getString(R.string.arm_qr_content));
            settings_title.setText(getResources().getString(R.string.arm_settings_title));
            settings_content.setText(getResources().getString(R.string.arm_settings_content));
        }
    }

    // open activity with class name given to it
    private void openActivity(Class activity_class){
        // send field values to new activity
        Intent i = new Intent(HelpActivity.this, activity_class);
        i.putExtra("val_1", VAL1);
        i.putExtra("val_2", VAL2);
        i.putExtra("val_3", VAL3);
        i.putExtra("input_text", INPUT);
        i.putExtra("barcode_index",BARCODE_INDEX);
        i.putExtra("lock_passed", true);
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

    // update color theme of app using shared preferences
    private void setTheme(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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
                setTheme(R.style.Orange_Theme);
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);
                break;
            case 1:
                setTheme(R.style.Blue_Theme);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);
                break;
            case 2:
                setTheme(R.style.Red_Theme);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);
                break;
            case 3:
                setTheme(R.style.Green_Theme);
                bottomNavigationView.setItemIconTintList(colorStateList_green);
                bottomNavigationView.setItemTextColor(colorStateList_green);
                break;
        }
    }

}