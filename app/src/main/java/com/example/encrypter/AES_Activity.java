package com.example.encrypter;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.Objects;

public class AES_Activity extends AppCompatActivity {


    // global values
    int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    String INPUT;
    int BARCODE_INDEX = -1;


    EditText input_key;
    EditText input_text;

    TextView result_textview;

    SwitchCompat switch_decrypt;

    ImageView copy;
    ImageView share;
    ImageView delete;
    ImageView help_btn;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);

        switch_decrypt = findViewById(R.id.switch_decrypt);
        input_key = findViewById(R.id.input_key);
        input_text = findViewById(R.id.input_text);
        result_textview = findViewById(R.id.result_textview);
        copy = findViewById(R.id.copy_btn);
        share = findViewById(R.id.share_btn);
        delete = findViewById(R.id.delete_btn);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        help_btn = findViewById(R.id.help_btn);

        bottomNavigation();

        setTheme();

        updateGlobals();

        result_textview.setMovementMethod(new ScrollingMovementMethod());

        switch_decrypt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                input_key.setHint("Key for Decryption");
            }
            else {
                input_key.setHint("Key for Encryption");
            }
            input_text.setText("");
            result_textview.setText("");
        });


        input_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runConversion();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (!Objects.equals(INPUT, "") && !switch_decrypt.isChecked()) {
            input_text.setText(INPUT);
            runConversion();
        }
        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runConversion();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // delete input and result button
        delete.setOnClickListener(v -> {
            input_key.setText("");
            input_text.setText("");
            result_textview.setText("");
        });
        // share result button
        share.setOnClickListener(v -> {
            if (!result_textview.getText().toString().equals("") && !result_textview.getText().toString().equals("Invalid Key") && !result_textview.getText().toString().equals("Invalid Message")) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                String share1 = "";
                share1 = result_textview.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_TEXT, (CharSequence) share1);
                shareIntent.setType("text/plain");
                shareIntent = Intent.createChooser(shareIntent, "Share Via: ");
                startActivity(shareIntent);
            }
        });
        // copy result button
        copy.setOnClickListener(v -> {
            if (!result_textview.getText().toString().equals("") && !result_textview.getText().toString().equals("Invalid Key") && !result_textview.getText().toString().equals("Invalid Message")) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String cop1 = result_textview.getText().toString();
                ClipData clip1 = ClipData.newPlainText("text", cop1);
                clipboard.setPrimaryClip(clip1);
            }
        });

    }



    private void runConversion(){
        try {
            if (switch_decrypt.isChecked()) {
                decrypt();
            } else {
                encrypt();
            }
        } catch (GeneralSecurityException e) {
            result_textview.setText("General Security Exception");
        }

    }


    public void encrypt() throws GeneralSecurityException {
        if(input_key.getText().toString().equals("") || input_text.getText().toString().equals("")){
            result_textview.setText("");
        }
        else {
            String encrypted = AESCrypt.encrypt(input_key.getText().toString(), input_text.getText().toString());
            result_textview.setText(encrypted);
        }
    }

    public void decrypt() throws GeneralSecurityException {
        String decrypted = "";
        try {
            decrypted = AESCrypt.decrypt(input_key.getText().toString(), input_text.getText().toString());
            result_textview.setText(decrypted);
        } catch (Exception e) {
            result_textview.setText("Invalid Key or Message");
        }
    }



    private void openActivity(Class activity_class){
        // send field values to new activity
        Intent i = new Intent(AES_Activity.this, activity_class);
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
    private boolean getPrefsBoolean(String key){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);
        return prefs.getBoolean(key,false);
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

    public void openPopUpWindow(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.help_popup_aes, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
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
        bottomNavigationView.setSelectedItemId(R.id.aes_menu);

        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(sounds_on) clickSound.start();
            switch (item.getItemId()){
                case R.id.home_menu:
                    openActivity(HomeActivity.class);
                    return true;
                case R.id.qr_menu:
                    openActivity(QRActivity.class);
                    return true;
                case R.id.aes_menu:
                    return true;
                case R.id.rsa_menu:
                    openActivity(RSA_Encrypt.class);
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
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);

                help_btn.setBackgroundResource(R.drawable.help_btn_orange);
                break;
            case 1:
                setTheme(R.style.Blue_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_blue);
                delete.setBackgroundResource(R.drawable.delete_btn_blue);
                share.setBackgroundResource(R.drawable.share_btn_blue);
                copy.setBackgroundResource(R.drawable.copy_btn_blue);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);

                help_btn.setBackgroundResource(R.drawable.help_btn_blue);
                break;
            case 2:
                setTheme(R.style.Red_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_red);
                delete.setBackgroundResource(R.drawable.delete_btn_red);
                share.setBackgroundResource(R.drawable.share_btn_red);
                copy.setBackgroundResource(R.drawable.copy_btn_red);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);

                help_btn.setBackgroundResource(R.drawable.help_btn_red);
                break;
            case 3:
                setTheme(R.style.Green_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_green);
                delete.setBackgroundResource(R.drawable.delete_btn_green);
                share.setBackgroundResource(R.drawable.share_btn_green);
                copy.setBackgroundResource(R.drawable.copy_btn_green);
                bottomNavigationView.setItemIconTintList(colorStateList_green);
                bottomNavigationView.setItemTextColor(colorStateList_green);

                help_btn.setBackgroundResource(R.drawable.help_btn_green);
                break;
        }
    }

}
