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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA_Decrypt extends AppCompatActivity {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    // global values
    private int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    private String INPUT;
    private int BARCODE_INDEX = -1;

    private EditText input_text;

    private TextView result_textview;

    private SwitchCompat switch_decrypt;

    private ImageView copy;
    private ImageView share;
    private ImageView delete;
    private ImageView help_btn;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa_decrypt);

        switch_decrypt = findViewById(R.id.switch_decrypt);
        copy = findViewById(R.id.copy_btn);
        share = findViewById(R.id.share_btn);
        delete = findViewById(R.id.delete_btn);
        input_text = findViewById(R.id.input_text);
        result_textview = findViewById(R.id.result_textview);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        help_btn = findViewById(R.id.help_btn);


        privateKey = getPrivateKey();
        publicKey = getPublicKey();

        bottomNavigation();

        setTheme();

        updateGlobals();

        switch_decrypt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            openActivity(RSA_Encrypt.class);
            makeClickSound();
        });


        // delete input and result button
        delete.setOnClickListener(v -> {
            input_text.setText("");
            result_textview.setText("");
            makeClickSound();
        });
        // share result button
        share.setOnClickListener(v -> {
            if(!result_textview.getText().toString().equals("") && !result_textview.getText().toString().equals(getResources().getString(R.string.private_key_error))) {
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
            if (!result_textview.getText().toString().equals("") && !result_textview.getText().toString().equals(getResources().getString(R.string.private_key_error))) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String cop1 = result_textview.getText().toString();
                ClipData clip1 = ClipData.newPlainText("text", cop1);
                clipboard.setPrimaryClip(clip1);
            }
            makeClickSound();
        });

        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    runConversion();
                } catch (NoSuchPaddingException | IllegalBlockSizeException |
                         NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
                    result_textview.setText(R.string.private_key_error);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }


    private static PrivateKey stringToPrivateKey(String privateK) {
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

    private PrivateKey getPrivateKey(){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);
        String private_key = prefs.getString("private_key","");
        return stringToPrivateKey(private_key);
    }

    private PublicKey getPublicKey(){
        SharedPreferences prefs = getSharedPreferences("SAVED_PREFERENCES", MODE_PRIVATE);
        String public_key = prefs.getString("public_key","");
        return stringToPublicKey(public_key);
    }

    private void runConversion()
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String result = RSA_Conversion.runDecrypt(privateKey,input_text.getText().toString());
        result_textview.setText(result);

        if(input_text.getText().toString().equals(""))
            result_textview.setText("");
    }





    private void openActivity(Class activity_class){
        // send field values to new activity
        Intent i = new Intent(RSA_Decrypt.this, activity_class);
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
        View popupView = inflater.inflate(R.layout.help_popup_rsa, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        makeClickSound();

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
        bottomNavigationView.setSelectedItemId(R.id.rsa_menu);

        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(sounds_on) clickSound.start();
            switch (item.getItemId()){
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
