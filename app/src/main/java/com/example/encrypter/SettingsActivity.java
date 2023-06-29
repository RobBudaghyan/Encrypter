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
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SettingsActivity extends AppCompatActivity {

    private int THEME_COLOR = 0;
    // global values
    private int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    private String INPUT;
    private int BARCODE_INDEX = -1;

    // key values
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private static int KEY_SIZE = 2048;

    private ImageView copy_public_key;
    private ImageView copy_private_key;
    private ImageView change_keys_btn;
    private ImageView orange_theme;
    private ImageView blue_theme;
    private ImageView red_theme;
    private ImageView green_theme;
    private ImageView reset;
    private TextView public_key_txt;
    private TextView private_key_txt;
    private SwitchCompat switch_sounds;
    private SwitchCompat switch_hexadecimal;
    private SwitchCompat switch_biometric;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        orange_theme = findViewById(R.id.orange_theme);
        blue_theme = findViewById(R.id.blue_theme);
        red_theme = findViewById(R.id.red_theme);
        green_theme = findViewById(R.id.green_theme);
        reset = findViewById(R.id.reset_btn);
        switch_sounds = findViewById(R.id.switch_sounds);
        switch_hexadecimal = findViewById(R.id.switch_hexadecimal);
        switch_biometric = findViewById(R.id.switch_biometric);
        copy_public_key = findViewById(R.id.copy_public_key);
        copy_private_key = findViewById(R.id.copy_private_key);
        public_key_txt = findViewById(R.id.public_key_txt);
        private_key_txt = findViewById(R.id.private_key_txt);
        change_keys_btn = findViewById(R.id.change_keys_btn);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // bottom navigation update
        bottomNavigation();

        // update shared preferences
        updatePrefs();

        if(privateKey == null || publicKey == null){
            generateNewKeys();
            savePrefsToSharedPreferences();
            updatePrefs();
        }

        // applying theme color
        setTheme();

        // update globals from intent
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

        // biometric verification switch
        switch_biometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            savePrefsToSharedPreferences();
            makeClickSound();
        });

        // reset settings button
        reset.setOnClickListener(view -> {
            for (int i = 0; i < 4; i++) {
                resetPrefs();
                updatePrefs();
                setTheme();
            }
            generateNewKeys();
            savePrefsToSharedPreferences();
            updatePrefs();
            openActivity(SettingsActivity.class);
        });

//
        change_keys_btn.setOnClickListener(v -> {
            generateNewKeys();
            savePrefsToSharedPreferences();
            updatePrefs();
            makeClickSound();
        });

        copy_public_key.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String cop1 = publicKeyToString(publicKey);
            ClipData clip1 = ClipData.newPlainText("public_key", cop1);
            clipboard.setPrimaryClip(clip1);
            makeClickSound();
        });

        copy_private_key.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String cop1 = privateKeyToString(privateKey);
            ClipData clip1 = ClipData.newPlainText("private_key", cop1);
            clipboard.setPrimaryClip(clip1);
            makeClickSound();
        });


    }

    // save preferences after destroying activity
    @Override
    protected void onDestroy() {
        savePrefsToSharedPreferences();
        super.onDestroy();
    }

    public void generateNewKeys(){
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        kpg.initialize(KEY_SIZE);
        KeyPair kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
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


    // save preferences in shared preferences
    private void savePrefsToSharedPreferences(){
        SharedPreferences.Editor editor = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE).edit();

        editor.putInt("color_id", THEME_COLOR);
        editor.putBoolean("sounds_on",switch_sounds.isChecked());
        editor.putBoolean("hex_on",switch_hexadecimal.isChecked());
        editor.putBoolean("biometric_on",switch_biometric.isChecked());

        editor.putString("private_key",privateKeyToString(privateKey));
        editor.putString("public_key",publicKeyToString(publicKey));

        editor.apply();
    }

    // reset all preferences
    private void resetPrefs(){
        SharedPreferences.Editor editor = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE).edit();
        editor.clear();
        editor.putInt("color_id", 0);
        editor.putBoolean("sounds_on",false);
        editor.putBoolean("hex_on",false);
        editor.putBoolean("biometric_on",false);
        VAL1 = 0;
        VAL2 = 0;
        VAL3 = 0;
        INPUT = "";
        BARCODE_INDEX = 0;
        editor.apply();
    }

    // update the preferences for app
    private void updatePrefs(){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);

        switch_sounds.setChecked(prefs.getBoolean("sounds_on",false));
        switch_hexadecimal.setChecked(prefs.getBoolean("hex_on",false));
        switch_biometric.setChecked(prefs.getBoolean("biometric_on",false));

        privateKey = stringToPrivateKey(prefs.getString("private_key",getResources().getString(R.string.default_private_key)));
        publicKey = stringToPublicKey(prefs.getString("public_key",getResources().getString(R.string.default_public_key)));

        String pub_temp = getResources().getString(R.string.your_public_key), priv_temp = getResources().getString(R.string.your_private_key);
        pub_temp = pub_temp + getPreviewOfKey(publicKeyToString(publicKey));
        priv_temp = priv_temp + getPreviewOfKey(privateKeyToString(privateKey));
        public_key_txt.setText(pub_temp);
        private_key_txt.setText(priv_temp);
    }

    public PrivateKey stringToPrivateKey(String privateK) {
        PrivateKey prvKey = null;
        try {
            byte[] privateBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                privateBytes = Base64.getDecoder().decode(privateK);
            }
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            prvKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return prvKey;
    }

    public static PublicKey stringToPublicKey(String publicK) {
        PublicKey pubKey = null;
        try {
            byte[] publicBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                publicBytes = Base64.getDecoder().decode(publicK);
            }
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pubKey;
    }

    private String getPreviewOfKey(String txt) {
        String result = "";

        result += "***";
        for (int i = 65; i < 75; i++) {
            result += txt.charAt(i);
        }
        result += "***";

        return result;
    }

    private static String privateKeyToString(PrivateKey privateKey){
        String str = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            str = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        }
        return str;
    }
    private static String publicKeyToString(PublicKey publicKey){
        String str = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            str = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        }
        return str;
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

    // bottom navigation bar
    @SuppressLint("NonConstantResourceId")
    private void bottomNavigation(){
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
                    openActivity(QR_Activity_Encrypt.class);
                    return true;
                case R.id.aes_menu:
                    openActivity(AES_Activity.class);
                    return true;
                case R.id.rsa_menu:
                    openActivity(RSA_Encrypt.class);
                    return true;
                case R.id.settings_menu:
                    return true;
            }

            return false;
        });
    }

    // update color theme of app using shared preferences
    private void setTheme(){
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
                switch_biometric.setThumbResource(R.drawable.thumb_2_orange);
                reset.setBackgroundResource(R.drawable.reset_btn_orange);
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);
                orange_theme.setBackgroundResource(R.drawable.orange_theme_pressed);
                blue_theme.setBackgroundResource(R.drawable.blue_theme);
                red_theme.setBackgroundResource(R.drawable.red_theme);
                green_theme.setBackgroundResource(R.drawable.green_theme);

                copy_private_key.setBackgroundResource(R.drawable.copy_btn_orange);
                copy_public_key.setBackgroundResource(R.drawable.copy_btn_orange);
                change_keys_btn.setBackgroundResource(R.drawable.change_keys_btn_orange);
                break;
            case 1:
                THEME_COLOR = 1;
                setTheme(R.style.Blue_Theme);
                switch_sounds.setThumbResource(R.drawable.thumb_2_blue);
                switch_hexadecimal.setThumbResource(R.drawable.thumb_2_blue);
                switch_biometric.setThumbResource(R.drawable.thumb_2_blue);
                reset.setBackgroundResource(R.drawable.reset_btn_blue);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);
                orange_theme.setBackgroundResource(R.drawable.orange_theme);
                blue_theme.setBackgroundResource(R.drawable.blue_theme_pressed);
                red_theme.setBackgroundResource(R.drawable.red_theme);
                green_theme.setBackgroundResource(R.drawable.green_theme);

                copy_private_key.setBackgroundResource(R.drawable.copy_btn_blue);
                copy_public_key.setBackgroundResource(R.drawable.copy_btn_blue);
                change_keys_btn.setBackgroundResource(R.drawable.change_keys_btn_blue);
                break;
            case 2:
                THEME_COLOR = 2;
                setTheme(R.style.Red_Theme);
                switch_sounds.setThumbResource(R.drawable.thumb_2_red);
                switch_hexadecimal.setThumbResource(R.drawable.thumb_2_red);
                switch_biometric.setThumbResource(R.drawable.thumb_2_red);
                reset.setBackgroundResource(R.drawable.reset_btn_red);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);
                orange_theme.setBackgroundResource(R.drawable.orange_theme);
                blue_theme.setBackgroundResource(R.drawable.blue_theme);
                red_theme.setBackgroundResource(R.drawable.red_theme_pressed);
                green_theme.setBackgroundResource(R.drawable.green_theme);

                copy_private_key.setBackgroundResource(R.drawable.copy_btn_red);
                copy_public_key.setBackgroundResource(R.drawable.copy_btn_red);
                change_keys_btn.setBackgroundResource(R.drawable.change_keys_btn_red);
                break;
            case 3:
                THEME_COLOR = 3;
                setTheme(R.style.Green_Theme);
                switch_sounds.setThumbResource(R.drawable.thumb_2_green);
                switch_hexadecimal.setThumbResource(R.drawable.thumb_2_green);
                switch_biometric.setThumbResource(R.drawable.thumb_2_green);
                reset.setBackgroundResource(R.drawable.reset_btn_green);
                bottomNavigationView.setItemIconTintList(colorStateList_green);
                bottomNavigationView.setItemTextColor(colorStateList_green);
                orange_theme.setBackgroundResource(R.drawable.orange_theme);
                blue_theme.setBackgroundResource(R.drawable.blue_theme);
                red_theme.setBackgroundResource(R.drawable.red_theme);
                green_theme.setBackgroundResource(R.drawable.green_theme_pressed);

                copy_private_key.setBackgroundResource(R.drawable.copy_btn_green);
                copy_public_key.setBackgroundResource(R.drawable.copy_btn_green);
                change_keys_btn.setBackgroundResource(R.drawable.change_keys_btn_green);
                break;
        }
    }

}