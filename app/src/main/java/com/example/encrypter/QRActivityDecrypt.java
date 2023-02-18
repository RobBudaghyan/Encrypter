package com.example.encrypter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class QRActivityDecrypt extends AppCompatActivity {

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity_decrypt);

        ImageView camera = findViewById(R.id.camera_btn);
        ImageView copy = findViewById(R.id.copy_btn);
        ImageView share = findViewById(R.id.share_btn);
        ImageView delete = findViewById(R.id.delete_btn);
        ImageView gallery = findViewById(R.id.gallery_btn);

        TextView result_textview = findViewById(R.id.result_textview);

        SwitchCompat switch_decrypt = findViewById(R.id.switch_decrypt);

//switch
        switch_decrypt.setChecked(true);
        switch_decrypt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            makeClickSound();
            if(!isChecked) openActivity(QRActivity.class);
        });

//bottom navigation
        bottomNavigation();

//theme color
        setTheme();

//scan
        camera.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(
                    QRActivityDecrypt.this);
            intentIntegrator.setPrompt("For flash use volume up key");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setBeepEnabled(false);
            //intentIntegrator.setRequestCode(SCAN);
            intentIntegrator.setCaptureActivity(Capture.class);
            intentIntegrator.initiateScan();

            makeClickSound();
        });

//gallery btn
        gallery.setOnClickListener(v -> {

            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

        });

//copy button
        copy.setOnClickListener(v -> {
            makeClickSound();

            if (!result_textview.getText().toString().equals("")) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String cop1 = result_textview.getText().toString();
                ClipData clip1 = ClipData.newPlainText("a1", cop1);
                clipboard.setPrimaryClip(clip1);
            }

        });

//share button
        share.setOnClickListener(v -> {
            makeClickSound();

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
        });

//delete button
        delete.setOnClickListener(v -> {
            result_textview.setText("");

            makeClickSound();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView result_textview = findViewById(R.id.result_textview);

//select from gallery and decrypt
        if(requestCode == SELECT_PICTURE){
            Uri selectedImageUri = null;
            if(data != null)
                selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap  input = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    printQrString(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
            }
        }

//scan and decrypt
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null){
            if (intentResult.getContents() != null) {
                result_textview.setText(intentResult.getContents());
            } else {
                result_textview.setText("");
            }
        }
    }

    private void openActivity(Class activity_class){
        startActivity(new Intent(getApplicationContext(),activity_class));
        finish();
        overridePendingTransition(250,250);
    }

    private void printQrString(Bitmap input){
        TextView result_textview = findViewById(R.id.result_textview);

        MultiFormatReader mReader = new MultiFormatReader();
        Map<DecodeHintType,Object> hints = new EnumMap<DecodeHintType,Object>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, true);
// select your barcode formats here
        List<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE,BarcodeFormat.AZTEC,BarcodeFormat.DATA_MATRIX);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);

        mReader.setHints(hints);

// your camera image here
        int width = input.getWidth(), height = input.getHeight();
        int[] pixels = new int[width * height];
        input.getPixels(pixels, 0, width, 0, 0, width, height);
        input.recycle();
        input = null;
        BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(width, height, pixels)));
        Result result = null;
        try {
            result = mReader.decodeWithState(bb);
            String resultString = result.getText();
            result_textview.setText(resultString);
        } catch (NotFoundException e) {
            result_textview.setText("");
            Toast.makeText(getApplicationContext(), "Couldn't find Barcode here", Toast.LENGTH_SHORT).show();
        }

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

    private void setTheme(){
        ImageView camera = findViewById(R.id.camera_btn);
        ImageView gallery = findViewById(R.id.gallery_btn);
        ImageView delete = findViewById(R.id.delete_btn);
        ImageView share = findViewById(R.id.share_btn);
        ImageView copy = findViewById(R.id.copy_btn);
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
                camera.setBackgroundResource(R.drawable.camera_btn_orange);
                gallery.setBackgroundResource(R.drawable.gallery_btn_orange);
                delete.setBackgroundResource(R.drawable.delete_btn_orange);
                share.setBackgroundResource(R.drawable.share_btn_orange);
                copy.setBackgroundResource(R.drawable.copy_btn_orange);
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);
                break;
            case 1:
                setTheme(R.style.Blue_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_blue);
                camera.setBackgroundResource(R.drawable.camera_btn_blue);
                gallery.setBackgroundResource(R.drawable.gallery_btn_blue);
                delete.setBackgroundResource(R.drawable.delete_btn_blue);
                share.setBackgroundResource(R.drawable.share_btn_blue);
                copy.setBackgroundResource(R.drawable.copy_btn_blue);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);
                break;
            case 2:
                setTheme(R.style.Red_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_red);
                camera.setBackgroundResource(R.drawable.camera_btn_red);
                gallery.setBackgroundResource(R.drawable.gallery_btn_red);
                delete.setBackgroundResource(R.drawable.delete_btn_red);
                share.setBackgroundResource(R.drawable.share_btn_red);
                copy.setBackgroundResource(R.drawable.copy_btn_red);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);
                break;
            case 3:
                setTheme(R.style.Green_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_green);
                camera.setBackgroundResource(R.drawable.camera_btn_green);
                gallery.setBackgroundResource(R.drawable.gallery_btn_green);
                delete.setBackgroundResource(R.drawable.delete_btn_green);
                share.setBackgroundResource(R.drawable.share_btn_green);
                copy.setBackgroundResource(R.drawable.copy_btn_green);
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
}
