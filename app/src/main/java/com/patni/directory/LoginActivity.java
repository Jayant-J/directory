package com.patni.directory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText nameText;
    String enteredName = "";
    EditText numberText;
    String enteredNumber = "";
    Button otpButton;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        nameText = findViewById(R.id.nameView);
        numberText = findViewById(R.id.numberView);
        enteredName = getIntent().getStringExtra("name");
        enteredNumber = getIntent().getStringExtra("number");
        if (enteredName != "" || enteredNumber != "") {
            nameText.setText(getIntent().getStringExtra("name"));
            numberText.setText(getIntent().getStringExtra("number"));
        }
        otpButton = findViewById(R.id.otpButton);
        nameText.requestFocus();
        progressBar = findViewById(R.id.getOtpprogressBar);
        progressBar.setVisibility(View.GONE);
    }

    public void getOtp(View view) {
        enteredName = nameText.getText().toString();
        enteredNumber = numberText.getText().toString();
        if (enteredName.length() == 0)
            Toast.makeText(this, "Please Enter a Name First", Toast.LENGTH_SHORT).show();
        else if (enteredNumber.length() != 10)
            Toast.makeText(this, "Please Enter a Valid Number", Toast.LENGTH_SHORT).show();
        else {
            progressBar.setVisibility(View.VISIBLE);
            otpButton.setVisibility(View.GONE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + enteredNumber,
                    30,
                    TimeUnit.SECONDS,
                    LoginActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressBar.setVisibility(View.VISIBLE);
                            otpButton.setVisibility(View.GONE);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            otpButton.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                            intent.putExtra("number", enteredNumber);
                            intent.putExtra("name", enteredName);
                            intent.putExtra("otpBackend", s);
                            startActivity(intent);
                            finish();
                        }

                    }
            );
        }
    }
}