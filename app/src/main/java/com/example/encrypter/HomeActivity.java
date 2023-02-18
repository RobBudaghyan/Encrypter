package com.example.encrypter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getPrefsBoolean("third_rotor_on")) setContentView(R.layout.activity_home_extended);
        else setContentView(R.layout.activity_home);

        EditText input_text = findViewById(R.id.input_edittext);

        TextView result_textview = findViewById(R.id.result_textview);
        TextView seekbar_text_1 = findViewById(R.id.seekbar_text_1);
        TextView seekbar_text_2 = findViewById(R.id.seekbar_text_2);
        TextView seekbar_text_3 = findViewById(R.id.seekbar_text_3);

        ImageView delete = findViewById(R.id.delete_btn);
        ImageView share = findViewById(R.id.share_btn);
        ImageView copy = findViewById(R.id.copy_btn);

        ImageView plus_1 = findViewById(R.id.plus_btn_1);
        ImageView plus_2 = findViewById(R.id.plus_btn_2);
        ImageView plus_3 = findViewById(R.id.plus_btn_3);
        ImageView minus_1 = findViewById(R.id.minus_btn_1);
        ImageView minus_2 = findViewById(R.id.minus_btn_2);
        ImageView minus_3 = findViewById(R.id.minus_btn_3);

        SeekBar seekbar_1 = findViewById(R.id.seekbar_1);
        SeekBar seekbar_2 = findViewById(R.id.seekbar_2);
        SeekBar seekbar_3 = findViewById(R.id.seekbar_3);

        SwitchCompat switch_decrypt = findViewById(R.id.switch_decrypt);

//bottom navigation
        bottomNavigation();

//theme color
        setTheme();

//switch
        switch_decrypt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            makeClickSound();
        });

//input
        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                convertRun();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

//delete button
        delete.setOnClickListener(v -> {
            input_text.setText("");
            result_textview.setText("");

            makeClickSound();
        });


//share button
        share.setOnClickListener(v -> {
            if(!result_textview.getText().toString().equals("")) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                String share1 = "";
                if(seekbar_3 != null) {
                    share1 = result_textview.getText().toString() + "\n" + "Rotor1 - " + seekbar_1.getProgress()
                        + ", Rotor2 - " + seekbar_2.getProgress() + ", Rotor3 - " + seekbar_3.getProgress();}
                else{
                    share1 = result_textview.getText().toString() + "\n" + "Rotor1 - " + seekbar_1.getProgress() +
                    ", Rotor2 - " + seekbar_2.getProgress();}
                shareIntent.putExtra(Intent.EXTRA_TEXT, (CharSequence) share1);
                shareIntent.setType("text/plain");
                shareIntent = Intent.createChooser(shareIntent, "Share Via: ");
                startActivity(shareIntent);
            }
            makeClickSound();
        });

//copy button
        copy.setOnClickListener(v -> {
            if (!result_textview.getText().toString().equals("")) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String cop1 = result_textview.getText().toString();
                ClipData clip1 = ClipData.newPlainText("a1", cop1);
                clipboard.setPrimaryClip(clip1);
            }
            makeClickSound();
        });

//seekbar progress
        seekbar_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_text_1.setText(String.valueOf(progress));
                convertRun();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                makeClickSound();
            }
        });
        seekbar_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_text_2.setText(String.valueOf(progress));
                convertRun();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                makeClickSound();
            }
        });
        if(seekbar_3 != null) {
            seekbar_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekbar_text_3.setText(String.valueOf(progress));
                    convertRun();
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    makeClickSound();
                }
            });
        }

//plus and minus buttons for seekbars
        plus_1.setOnClickListener(v -> {
            int k = seekbar_1.getProgress();
            seekbar_1.setProgress(k + 1);

            makeClickSound();
        });
        //plus_1.setOnLongClickListener();
        plus_2.setOnClickListener(v -> {
            int k = seekbar_2.getProgress();
            seekbar_2.setProgress(k + 1);

            makeClickSound();
        });
        if(plus_3 != null){
            plus_3.setOnClickListener(v -> {
                int k = seekbar_3.getProgress();
                seekbar_3.setProgress(k + 1);

                makeClickSound();
            });
        }
        minus_1.setOnClickListener(v -> {
            int k = seekbar_1.getProgress();
            seekbar_1.setProgress(k - 1);

            makeClickSound();
        });
        minus_2.setOnClickListener(v -> {
            int k = seekbar_2.getProgress();
            seekbar_2.setProgress(k - 1);

            makeClickSound();
        });
        if(minus_3 != null){
            minus_3.setOnClickListener(v -> {
            int k = seekbar_3.getProgress();
            seekbar_3.setProgress(k - 1);

            makeClickSound();
        });}
    }

    private void openActivity(Class activity_class){
        startActivity(new Intent(getApplicationContext(),activity_class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private int getPrefsInt(String key){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);
        return prefs.getInt(key,0);
    }

    private boolean getPrefsBoolean(String key){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);
        return prefs.getBoolean(key,false);
    }

    private void bottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_menu);
        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(sounds_on) clickSound.start();
            switch (item.getItemId()){
                case R.id.home_menu:
                    return true;
                case R.id.qr_menu:
                    openActivity(QRActivity.class);
                    return true;
                case R.id.help_menu:
                    openActivity(HelpActivity.class);
                    return true;
                case R.id.settings_menu:
                    openActivity(SettingsActivity.class);
                    return true;
            }
            return false;
        });
    }

    private void setTheme(){
        ImageView delete = findViewById(R.id.delete_btn);
        ImageView share = findViewById(R.id.share_btn);
        ImageView copy = findViewById(R.id.copy_btn);
        ImageView plus_1 = findViewById(R.id.plus_btn_1);
        ImageView plus_2 = findViewById(R.id.plus_btn_2);
        ImageView plus_3 = findViewById(R.id.plus_btn_3);
        ImageView minus_1 = findViewById(R.id.minus_btn_1);
        ImageView minus_2 = findViewById(R.id.minus_btn_2);
        ImageView minus_3 = findViewById(R.id.minus_btn_3);
        SeekBar seekbar_1 = findViewById(R.id.seekbar_1);
        SeekBar seekbar_2 = findViewById(R.id.seekbar_2);
        SeekBar seekbar_3 = findViewById(R.id.seekbar_3);
        SwitchCompat switch_decrypt = findViewById(R.id.switch_decrypt);
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
                switch_decrypt.setThumbResource(R.drawable.thumb_1_orange);
                delete.setBackgroundResource(R.drawable.delete_btn_orange);
                share.setBackgroundResource(R.drawable.share_btn_orange);
                copy.setBackgroundResource(R.drawable.copy_btn_orange);
                seekbar_1.setThumb(getDrawable(R.drawable.thumb_3_orange));
                seekbar_2.setThumb(getDrawable(R.drawable.thumb_3_orange));
                if(seekbar_3 != null) seekbar_3.setThumb(getDrawable(R.drawable.thumb_3_orange));
                plus_1.setBackgroundResource(R.drawable.plus_btn_orange);
                plus_2.setBackgroundResource(R.drawable.plus_btn_orange);
                if(plus_3 != null) plus_3.setBackgroundResource(R.drawable.plus_btn_orange);
                minus_1.setBackgroundResource(R.drawable.minus_btn_orange);
                minus_2.setBackgroundResource(R.drawable.minus_btn_orange);
                if(minus_3 != null) minus_3.setBackgroundResource(R.drawable.minus_btn_orange);
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);
                break;
            case 1:
                setTheme(R.style.Blue_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_blue);
                delete.setBackgroundResource(R.drawable.delete_btn_blue);
                share.setBackgroundResource(R.drawable.share_btn_blue);
                copy.setBackgroundResource(R.drawable.copy_btn_blue);
                seekbar_1.setThumb(getDrawable(R.drawable.thumb_3_blue));
                seekbar_2.setThumb(getDrawable(R.drawable.thumb_3_blue));
                if(seekbar_3 != null) seekbar_3.setThumb(getDrawable(R.drawable.thumb_3_blue));
                plus_1.setBackgroundResource(R.drawable.plus_btn_blue);
                plus_2.setBackgroundResource(R.drawable.plus_btn_blue);
                if(plus_3 != null) plus_3.setBackgroundResource(R.drawable.plus_btn_blue);
                minus_1.setBackgroundResource(R.drawable.minus_btn_blue);
                minus_2.setBackgroundResource(R.drawable.minus_btn_blue);
                if(minus_3 != null) minus_3.setBackgroundResource(R.drawable.minus_btn_blue);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);
                break;
            case 2:
                setTheme(R.style.Red_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_red);
                delete.setBackgroundResource(R.drawable.delete_btn_red);
                share.setBackgroundResource(R.drawable.share_btn_red);
                copy.setBackgroundResource(R.drawable.copy_btn_red);
                seekbar_1.setThumb(getDrawable(R.drawable.thumb_3_red));
                seekbar_2.setThumb(getDrawable(R.drawable.thumb_3_red));
                if(seekbar_3 != null) seekbar_3.setThumb(getDrawable(R.drawable.thumb_3_red));
                plus_1.setBackgroundResource(R.drawable.plus_btn_red);
                plus_2.setBackgroundResource(R.drawable.plus_btn_red);
                if(plus_3 != null) plus_3.setBackgroundResource(R.drawable.plus_btn_red);
                minus_1.setBackgroundResource(R.drawable.minus_btn_red);
                minus_2.setBackgroundResource(R.drawable.minus_btn_red);
                if(minus_3 != null) minus_3.setBackgroundResource(R.drawable.minus_btn_red);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);
                break;
            case 3:
                setTheme(R.style.Green_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_green);
                delete.setBackgroundResource(R.drawable.delete_btn_green);
                share.setBackgroundResource(R.drawable.share_btn_green);
                copy.setBackgroundResource(R.drawable.copy_btn_green);
                seekbar_1.setThumb(getDrawable(R.drawable.thumb_3_green));
                seekbar_2.setThumb(getDrawable(R.drawable.thumb_3_green));
                if(seekbar_3 != null) seekbar_3.setThumb(getDrawable(R.drawable.thumb_3_green));
                plus_1.setBackgroundResource(R.drawable.plus_btn_green);
                plus_2.setBackgroundResource(R.drawable.plus_btn_green);
                if(plus_3 != null) plus_3.setBackgroundResource(R.drawable.plus_btn_green);
                minus_1.setBackgroundResource(R.drawable.minus_btn_green);
                minus_2.setBackgroundResource(R.drawable.minus_btn_green);
                if(minus_3 != null) minus_3.setBackgroundResource(R.drawable.minus_btn_green);
                bottomNavigationView.setItemIconTintList(colorStateList_green);
                bottomNavigationView.setItemTextColor(colorStateList_green);
                break;
        }
    }

    private void makeClickSound(){
        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");

        if(sounds_on) clickSound.start();
    }

    private void convertRun(){
        EditText input_text = findViewById(R.id.input_edittext);
        TextView result_textview = findViewById(R.id.result_textview);
        SeekBar seekbar_1 = findViewById(R.id.seekbar_1);
        SeekBar seekbar_2 = findViewById(R.id.seekbar_2);
        SeekBar seekbar_3 = findViewById(R.id.seekbar_3);
        SwitchCompat switch_decrypt = findViewById(R.id.switch_decrypt);


        boolean hex_on = getPrefsBoolean("hex_on");

            result_textview.setMovementMethod(new ScrollingMovementMethod());

            String input = input_text.getText().toString();
            String result = "";
            int rotor_3;
            if(seekbar_3 == null) rotor_3 = -1;
            else rotor_3 = seekbar_3.getProgress();
            try {
                result = Conversion.runConversion(input, seekbar_1.getProgress(), seekbar_2.getProgress(),
                        rotor_3, switch_decrypt.isChecked(), hex_on);
            } catch (Exception e) {
                result = "Something Went Wrong :(";
            }
            result_textview.setText(result);


            makeClickSound();
    }


}