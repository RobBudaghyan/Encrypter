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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class QR_Activity_Encrypt extends AppCompatActivity {

    private BarcodeFormat BARCODE_FORMAT;
    // global values
    private int VAL1, VAL2, VAL3;
    private int BARCODE_INDEX = -1;
    private String INPUT;


    private EditText input_text;
    private ImageView delete_btn;
    private ImageView share_btn;
    private ImageView download_btn;
    private ImageView result_txt;
    private ImageView help_btn;
    private SwitchCompat switch_decrypt;
    private RadioGroup radioGroup;
    private RadioButton one_radio_btn;
    private RadioButton two_radio_btn;
    private RadioButton three_radio_btn;
    TextView one_txt;
    TextView two_txt;
    TextView three_txt;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

        input_text = findViewById(R.id.input_edittext);
        delete_btn = findViewById(R.id.delete_btn);
        share_btn = findViewById(R.id.share_btn);
        download_btn = findViewById(R.id.download_btn);
        result_txt = findViewById(R.id.result);
        switch_decrypt = findViewById(R.id.switch_decrypt);
        one_txt = findViewById(R.id.one_txt);
        two_txt = findViewById(R.id.two_txt);
        three_txt = findViewById(R.id.three_txt);
        radioGroup = findViewById(R.id.code_radio_group);
        one_radio_btn = findViewById(R.id.one_radio_btn);
        two_radio_btn = findViewById(R.id.two_radio_btn);
        three_radio_btn = findViewById(R.id.three_radio_btn);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        help_btn = findViewById(R.id.help_btn);

        // update globals from intent
        updateGlobals();

        // update radio buttons
        if(BARCODE_INDEX != -1){
            setRadioButton(BARCODE_INDEX);
            convertRun();
        }
        else{
            setRadioButton(0);
        }

        // set barcode conversion method
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.one_radio_btn:
                    BARCODE_FORMAT = BarcodeFormat.QR_CODE;
                    convertRun();
                    break;
                case R.id.two_radio_btn:
                    BARCODE_FORMAT = BarcodeFormat.AZTEC;
                    convertRun();
                    break;
                case R.id.three_radio_btn:
                    BARCODE_FORMAT = BarcodeFormat.CODE_128;
                    convertRun();
                    break;
            }
            makeClickSound();
        });

        // change radiobutton check by clicking
        one_txt.setOnClickListener(v -> {
            if(!one_radio_btn.isChecked())
                setRadioButton(0);
        });
        two_txt.setOnClickListener(v -> {
            if(!two_radio_btn.isChecked())
                setRadioButton(1);
        });
        three_txt.setOnClickListener(v -> {
            if(!three_radio_btn.isChecked())
                setRadioButton(2);
        });


        // bottom navigation update
        bottomNavigation();

        // theme color update
        setTheme();

        // encrypt/decrypt switch
        switch_decrypt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            makeClickSound();
            if (isChecked) openActivity(QR_Activity_Decrypt.class);
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
           try {
            Drawable d = result_txt.getDrawable();
            Bitmap bitmap = null;
            bitmap = ((BitmapDrawable) d).getBitmap();

            @SuppressLint("UseCompatLoadingForDrawables") Drawable check = getDrawable(R.drawable.empty_image_icon);
            Bitmap check_bitmap = ((BitmapDrawable) check).getBitmap();
            if (bitmap != check_bitmap) {
                shareBitmap(bitmap);
            }
            makeClickSound();

           } catch (Exception e) {
               throw new RuntimeException(e);
           }
        });


    }


    // execute to barcode/from barcode and update result view bar
    private void convertRun(){
        String input = Home_Conversion.runConversion(input_text.getText().toString(),VAL1,VAL2,VAL3,false,getPrefsBoolean("hex_on"));

            try {
                generateQR(input, BARCODE_FORMAT);
            }
            catch (Exception e) {
                if(BARCODE_FORMAT == BarcodeFormat.CODE_128)
                    result_txt.setImageResource(R.drawable.error_qr_1);
                else
                    result_txt.setImageResource(R.drawable.empty_image_icon);
            }
            if (Objects.equals(input, "")) result_txt.setImageResource(R.drawable.empty_image_icon);
    }

    // generate barcode given text and format
    private void generateQR(String input, BarcodeFormat barcode_format){
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(input, barcode_format,800,800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            result_txt.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            result_txt.setImageResource(R.drawable.empty_image_icon);
        }
    }
    //
    // share barcode given bitmap of it
    private void shareBitmap(Bitmap bitmap) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, getImageUri(QR_Activity_Encrypt.this, bitmap));
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
                getImageUri(QR_Activity_Encrypt.this, bitmap);
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

    // set radiobutton check by index
    private void setRadioButton(int index){
        one_radio_btn.setChecked(false);
        two_radio_btn.setChecked(false);
        three_radio_btn.setChecked(false);

        switch (index) {
            case 0:
                one_radio_btn.setChecked(true);
                BARCODE_FORMAT = BarcodeFormat.QR_CODE;
                convertRun();
                break;
            case 1:
                two_radio_btn.setChecked(true);
                BARCODE_FORMAT = BarcodeFormat.AZTEC;
                convertRun();
                break;
            case 2:
                three_radio_btn.setChecked(true);
                BARCODE_FORMAT = BarcodeFormat.CODE_128;
                convertRun();
                break;
        }
    }

    // open activity with class name given to it
    private void openActivity(Class activity_class){
        // send field values to new activity
        Intent i = new Intent(QR_Activity_Encrypt.this, activity_class);
        if(!input_text.getText().toString().equals("")) {
            String input = input_text.getText().toString();
            i.putExtra("input_text", input);
        }
        int barcode_index = 0;
        if(two_radio_btn.isChecked())
            barcode_index = 1;
        if(three_radio_btn.isChecked())
            barcode_index = 2;

        i.putExtra("val_1", VAL1);
        i.putExtra("val_2", VAL2);
        i.putExtra("val_3", VAL3);
        i.putExtra("barcode_index",barcode_index);
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
            input_text.setText(INPUT);
            convertRun();
        }
    }

    public void openPopUpWindow(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.help_popup_qr, null);

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

    // bottom navigation bar
    @SuppressLint("NonConstantResourceId")
    private void bottomNavigation(){
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
                case R.id.aes_menu:
                    openActivity(AES_Activity.class);
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
                delete_btn.setBackgroundResource(R.drawable.delete_btn_orange);
                share_btn.setBackgroundResource(R.drawable.share_btn_orange);
                result_txt.setBackgroundResource(R.drawable.qr_stroke_orange);
                download_btn.setBackgroundResource(R.drawable.download_btn_orange);
                bottomNavigationView.setItemIconTintList(colorStateList_orange);
                bottomNavigationView.setItemTextColor(colorStateList_orange);

                help_btn.setBackgroundResource(R.drawable.help_btn_orange);
                break;
            case 1:
                setTheme(R.style.Blue_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_blue);
                delete_btn.setBackgroundResource(R.drawable.delete_btn_blue);
                share_btn.setBackgroundResource(R.drawable.share_btn_blue);
                result_txt.setBackgroundResource(R.drawable.qr_stroke_blue);
                download_btn.setBackgroundResource(R.drawable.download_btn_blue);
                bottomNavigationView.setItemIconTintList(colorStateList_blue);
                bottomNavigationView.setItemTextColor(colorStateList_blue);

                help_btn.setBackgroundResource(R.drawable.help_btn_blue);
                break;
            case 2:
                setTheme(R.style.Red_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_red);
                delete_btn.setBackgroundResource(R.drawable.delete_btn_red);
                share_btn.setBackgroundResource(R.drawable.share_btn_red);
                result_txt.setBackgroundResource(R.drawable.qr_stroke_red);
                download_btn.setBackgroundResource(R.drawable.download_btn_red);
                bottomNavigationView.setItemIconTintList(colorStateList_red);
                bottomNavigationView.setItemTextColor(colorStateList_red);

                help_btn.setBackgroundResource(R.drawable.help_btn_red);
                break;
            case 3:
                setTheme(R.style.Green_Theme);
                switch_decrypt.setThumbResource(R.drawable.thumb_1_green);
                delete_btn.setBackgroundResource(R.drawable.delete_btn_green);
                share_btn.setBackgroundResource(R.drawable.share_btn_green);
                result_txt.setBackgroundResource(R.drawable.qr_stroke_green);
                download_btn.setBackgroundResource(R.drawable.download_btn_green);
                bottomNavigationView.setItemIconTintList(colorStateList_green);
                bottomNavigationView.setItemTextColor(colorStateList_green);

                help_btn.setBackgroundResource(R.drawable.help_btn_green);
                break;
        }
    }

}