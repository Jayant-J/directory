package com.patni.directory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    static SharedPreferences sharedPreferences;
    static String localhost = "192.168.1.10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        sharedPreferences = this.getSharedPreferences("com.patni.directory", MODE_PRIVATE);
//        sharedPreferences.edit().putBoolean("signIn", false).apply();
//        sharedPreferences.edit().putString("name", "").apply();
//        sharedPreferences.edit().putString("number", "phoneNumber").apply();
        getSupportActionBar().hide();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!sharedPreferences.getBoolean("signIn", false)) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}