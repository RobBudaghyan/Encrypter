package com.example.encrypter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class BiometricPrompt extends AppCompatActivity {

    androidx.biometric.BiometricPrompt biometricPrompt;
    androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    // global values
    int VAL1 = -1, VAL2 = -1, VAL3 = -1;
    String INPUT;
    int BARCODE_INDEX = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // update global values
        updateGlobals();

        // manage error toasts
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Device Doesn't have fingerprint", Toast.LENGTH_SHORT).show();
                biometricUnlocked();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Not Working", Toast.LENGTH_SHORT).show();
                biometricUnlocked();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No FingerPrint Assigned", Toast.LENGTH_SHORT).show();
                biometricUnlocked();
                break;
        }

        // define action of success or failures
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new androidx.biometric.BiometricPrompt(this, executor,new androidx.biometric.BiometricPrompt.AuthenticationCallback(){
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                startBiometricPrompt();
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                biometricUnlocked();
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

        });
        promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder().setTitle("Encrypter")
                .setDescription("Use Fingerprint To Log In").setDeviceCredentialAllowed(true) .build() ;
        biometricPrompt.authenticate(promptInfo);

    }

    // restart verification if failed
    private void startBiometricPrompt(){
        Intent intent = new Intent(this, BiometricPrompt.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    // exit to home activity with success
    private void biometricUnlocked(){
        Intent i = new Intent(this, HomeActivity.class);
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

}