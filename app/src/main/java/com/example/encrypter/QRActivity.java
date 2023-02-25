package com.example.encrypter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class QRActivity extends AppCompatActivity {

    BarcodeFormat barcode_format = BarcodeFormat.QR_CODE;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        EditText input_text = findViewById(R.id.input_edittext);
        ImageView delete_btn = findViewById(R.id.delete_btn);
        ImageView share_btn = findViewById(R.id.share_btn);
        ImageView download_btn = findViewById(R.id.download_btn);
        ImageView result_txt = findViewById(R.id.result);
        SwitchCompat switch_decrypt = findViewById(R.id.switch_decrypt);
        RadioGroup radioGroup = findViewById(R.id.code_radio_group);

        // set barcode conversion method
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.one_radio_btn:
                    barcode_format = BarcodeFormat.QR_CODE;
                    convertRun();
                    break;
                case R.id.two_radio_btn:
                    barcode_format = BarcodeFormat.AZTEC;
                    convertRun();
                    break;
                case R.id.three_radio_btn:
                    barcode_format = BarcodeFormat.DATA_MATRIX;
                    convertRun();
                    break;
            }
            makeClickSound();
        });

        // bottom navigation update
        bottomNavigation();

        // theme color update
        setTheme();

        // encrypt/decrypt switch
        switch_decrypt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            makeClickSound();
            if (isChecked) openActivity(QRActivityDecrypt.class);
        });


        // input text field
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

        // delete input and barcode button
        delete_btn.setOnClickListener(v -> {
            input_text.setText("");
            result_txt.setImageResource(R.drawable.empty_image_icon);
            makeClickSound();
        });

        // download barcode button
        download_btn.setOnClickListener(v -> {
            Drawable d = result_txt.getDrawable();
            Bitmap bitmap = null;
            bitmap = ((BitmapDrawable) d).getBitmap();
            @SuppressLint("UseCompatLoadingForDrawables") Drawable check = getDrawable(R.drawable.empty_image_icon);
            Bitmap check_bitmap = ((BitmapDrawable) check).getBitmap();
            if(bitmap != check_bitmap) {
                try {
                    downloadBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
                }
            }
            makeClickSound();
        });

        // share barcode button
        share_btn.setOnClickListener(v -> {
            Drawable d = result_txt.getDrawable();
            Bitmap bitmap = null;
            bitmap = ((BitmapDrawable) d).getBitmap();

            @SuppressLint("UseCompatLoadingForDrawables") Drawable check = getDrawable(R.drawable.empty_image_icon);
            Bitmap check_bitmap = ((BitmapDrawable) check).getBitmap();
            if (bitmap != check_bitmap) {
                shareBitmap(bitmap);
            }
            makeClickSound();
        });


    }

    // execute to barcode/from barcode and update result view bar
    private void convertRun(){
        EditText input_text = findViewById(R.id.input_edittext);
        ImageView result = findViewById(R.id.result);
                String input = input_text.getText().toString();
                try {
                    generateQR(input, barcode_format);
                }
                catch (Exception e) {
                    result.setImageResource(R.drawable.empty_image_icon);
                }
                if (Objects.equals(input, "")) result.setImageResource(R.drawable.empty_image_icon);
    }

    // generate barcode given text and format
    private void generateQR(String input, BarcodeFormat barcode_format){
        ImageView result = findViewById(R.id.result);
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(input, barcode_format,800,800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            result.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            result.setImageResource(R.drawable.empty_image_icon);
        }
    }

    // share barcode given bitmap of it
    private void shareBitmap(Bitmap bitmap) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, getImageUri(QRActivity.this, bitmap));
        try {
            startActivity(Intent.createChooser(i, "My Profile"));
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Couldn't share",Toast.LENGTH_SHORT).show();
        }
    }

    // download barcode given bitmap of it
    private void downloadBitmap(Bitmap bitmap){
        Runnable runnable = () -> {
            try {
                getImageUri(QRActivity.this, bitmap);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_SHORT).show();
    }

    // return uri of barcode for download and share
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        int rand = (int) (Math.random() * 10000);
        String str = rand + "";
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, str, null);
        return Uri.parse(path);
    }

    // open activity with class name given to it
    private void openActivity(Class activity_class){
        startActivity(new Intent(getApplicationContext(),activity_class));
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
        bottomNavigationView.setSelectedItemId(R.id.qr_menu);
        final MediaPlayer clickSound = MediaPlayer.create(this,R.raw.click_sound);
        boolean sounds_on = getPrefsBoolean("sounds_on");
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(sounds_on) clickSound.start();
            switch (item.getItemId()){
                case R.id.home_menu:
                    openActivity(HomeActivity.class);
                    return true;
                case R.id.qr_menu:
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
    private void setTheme(){
        ImageView delete = findViewById(R.id.delete_btn);
        ImageView share = findViewById(R.id.share_btn);
        ImageView download = findViewById(R.id.download_btn);
        ImageView result = findViewById(R.id.result);
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
                result.setBackgroundResource(R.drawable.qr_stroke_orange);
                download.setBackgroundResource(R.drawable.download_btn_orange);
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);
                break;
            case 1:
                setTheme(R.style.Blue_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_blue);
                delete.setBackgroundResource(R.drawable.delete_btn_blue);
                share.setBackgroundResource(R.drawable.share_btn_blue);
                result.setBackgroundResource(R.drawable.qr_stroke_blue);
                download.setBackgroundResource(R.drawable.download_btn_blue);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);
                break;
            case 2:
                setTheme(R.style.Red_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_red);
                delete.setBackgroundResource(R.drawable.delete_btn_red);
                share.setBackgroundResource(R.drawable.share_btn_red);
                result.setBackgroundResource(R.drawable.qr_stroke_red);
                download.setBackgroundResource(R.drawable.download_btn_red);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);
                break;
            case 3:
                setTheme(R.style.Green_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_green);
                delete.setBackgroundResource(R.drawable.delete_btn_green);
                share.setBackgroundResource(R.drawable.share_btn_green);
                result.setBackgroundResource(R.drawable.qr_stroke_green);
                download.setBackgroundResource(R.drawable.download_btn_green);
                bottomNavigationView.setItemIconTintList(colorStateList_green);
                bottomNavigationView.setItemTextColor(colorStateList_green);
                break;
        }
    }

}