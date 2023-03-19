package com.example.encrypter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class HomeActivity extends AppCompatActivity {


    // global values
    int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    int BARCODE_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        EditText input_text = findViewById(R.id.input_edittext);
        TextView result_textview = findViewById(R.id.result_textview);
        TextView seekbar_text_1 = findViewById(R.id.seekbar_text_1);
        TextView seekbar_text_2 = findViewById(R.id.seekbar_text_2);
        TextView seekbar_text_3 = findViewById(R.id.seekbar_text_3);
        ImageView delete = findViewById(R.id.delete_btn);
        ImageView share = findViewById(R.id.share_btn);
        ImageView copy = findViewById(R.id.copy_btn);
        ImageView random = findViewById(R.id.random_btn);
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

        // random values for seekbars
        setRandomValuesForRotors();

        //biometric verification
        if(getPrefsBoolean("biometric_on") && !checkBiometricPass()){
            openActivity(BiometricPrompt.class);
        }

        // update global values
        updateGlobals();

        // bottom navigation update
        bottomNavigation();

        // theme color update
        setTheme();

        // encrypt/decrypt switch
        switch_decrypt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            input_text.setText("");
            result_textview.setText("");
            makeClickSound();
        });

        // input edittext field
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

        // delete input and result button
        delete.setOnClickListener(v -> {
            input_text.setText("");
            result_textview.setText("");
            makeClickSound();
        });

        // share result button
        share.setOnClickListener(v -> {
            if(!result_textview.getText().toString().equals("")) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                String share1 = "";
                share1 = result_textview.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_TEXT, (CharSequence) share1);
                shareIntent.setType("text/plain");
                shareIntent = Intent.createChooser(shareIntent, "Share Via: ");
                startActivity(shareIntent);
            }
            makeClickSound();
        });

        // copy result button
        copy.setOnClickListener(v -> {
            if (!result_textview.getText().toString().equals("")) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String cop1 = result_textview.getText().toString();
                ClipData clip1 = ClipData.newPlainText("a1", cop1);
                clipboard.setPrimaryClip(clip1);
            }
            makeClickSound();
        });

        // random values for rotors button
        random.setOnClickListener(v -> {
            setRandomValuesForRotors();
            makeClickSound();
        });

        // rotor seekbars
        if(VAL1 != -1){
            seekbar_1.setProgress(VAL1);
            seekbar_2.setProgress(VAL2);
            seekbar_3.setProgress(VAL3);
            seekbar_text_1.setText(String.valueOf(seekbar_1.getProgress()));
            seekbar_text_2.setText(String.valueOf(seekbar_2.getProgress()));
            seekbar_text_3.setText(String.valueOf(seekbar_3.getProgress()));
        }
        convertRun();
        seekbar_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_text_1.setText(String.valueOf(progress));
                convertRun();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {makeClickSound();}
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
            public void onStopTrackingTouch(SeekBar seekBar) {makeClickSound();}
        });
        seekbar_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_text_3.setText(String.valueOf(progress));
                convertRun();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {makeClickSound();}
        });



        // plus and minus buttons of seekbars
        plus_1.setOnClickListener(v -> {
            int k = seekbar_1.getProgress();
            seekbar_1.setProgress(k + 1);
            makeClickSound();
        });
        plus_2.setOnClickListener(v -> {
            int k = seekbar_2.getProgress();
            seekbar_2.setProgress(k + 1);
            makeClickSound();
        });
            plus_3.setOnClickListener(v -> {
                int k = seekbar_3.getProgress();
                seekbar_3.setProgress(k + 1);
                makeClickSound();
            });
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
            minus_3.setOnClickListener(v -> {
            int k = seekbar_3.getProgress();
            seekbar_3.setProgress(k - 1);
            makeClickSound();
        });}

    // open activity with class name given to it
    private void openActivity(Class activity_class){
        EditText input_text = findViewById(R.id.input_edittext);
        SeekBar seekbar_1 = findViewById(R.id.seekbar_1);
        SeekBar seekbar_2 = findViewById(R.id.seekbar_2);
        SeekBar seekbar_3 = findViewById(R.id.seekbar_3);
        // send field values to new activity
        Intent i = new Intent(HomeActivity.this, activity_class);
        if(!input_text.getText().toString().equals("")) {
            String input = input_text.getText().toString();
            i.putExtra("input_text", input);
        }
        int val_1 = seekbar_1.getProgress();
        int val_2 = seekbar_2.getProgress();
        int val_3 = seekbar_3.getProgress();
        i.putExtra("val_1", val_1);
        i.putExtra("val_2", val_2);
        i.putExtra("val_3", val_3);
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

    // execute encryption/decryption and update result text bar
    public void convertRun(){
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
        try {
            result = Conversion.runConversion(input, seekbar_1.getProgress(), seekbar_2.getProgress(),
                    seekbar_3.getProgress(), switch_decrypt.isChecked(), hex_on);
        } catch (Exception e) {
            result = "Something Went Wrong :(";
        }
        result_textview.setText(result);
    }

    private void setRandomValuesForRotors(){
        SeekBar seekbar_1 = findViewById(R.id.seekbar_1);
        SeekBar seekbar_2 = findViewById(R.id.seekbar_2);
        SeekBar seekbar_3 = findViewById(R.id.seekbar_3);
        TextView seekbar_text_1 = findViewById(R.id.seekbar_text_1);
        TextView seekbar_text_2 = findViewById(R.id.seekbar_text_2);
        TextView seekbar_text_3 = findViewById(R.id.seekbar_text_3);

        Random rand = new Random();
        final int limit_1 = 26;
        final int limit_2 = 11;

        int val_1 = rand.nextInt(limit_1);
        int val_2 = rand.nextInt(limit_2);
        int val_3 = rand.nextInt(limit_2);

        seekbar_1.setProgress(val_1);
        seekbar_text_1.setText(String.valueOf(val_1));
        seekbar_2.setProgress(val_2);
        seekbar_text_2.setText(String.valueOf(val_2));
        seekbar_3.setProgress(val_3);
        seekbar_text_3.setText(String.valueOf(val_3));
    }

    // update all globals
    private void updateGlobals(){
        EditText input_text = findViewById(R.id.input_edittext);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            String input = intent.getExtras().getString("input_text");
            VAL1 = intent.getExtras().getInt("val_1");
            VAL2 = intent.getExtras().getInt("val_2");
            VAL3 = intent.getExtras().getInt("val_3");
            BARCODE_INDEX = intent.getExtras().getInt("barcode_index");
            input_text.setText(input);
        }
    }

    private boolean checkBiometricPass(){
        boolean pass = false;
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            pass = intent.getExtras().getBoolean("lock_passed");
        }
        return pass;
    }

    // bottom navigation bar
    @SuppressLint("NonConstantResourceId")
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

    // update color theme of app using shared preferences
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setTheme(){
        ImageView delete = findViewById(R.id.delete_btn);
        ImageView share = findViewById(R.id.share_btn);
        ImageView copy = findViewById(R.id.copy_btn);
        ImageView random = findViewById(R.id.random_btn);
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
                new int[] { android.R.attr.state_enabled, -android.R.attr.state_pressed, -android.R.attr.state_selected},
                new int[] {-android.R.attr.state_enabled},
                new int[] {android.R.attr.state_enabled, android.R.attr.state_selected},
                new int[] {android.R.attr.state_enabled, android.R.attr.state_pressed}
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
                random.setBackgroundResource(R.drawable.random_btn_orange);
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
                random.setBackgroundResource(R.drawable.random_btn_blue);
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
                random.setBackgroundResource(R.drawable.random_btn_red);
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
                random.setBackgroundResource(R.drawable.random_btn_green);
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


}