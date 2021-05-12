package com.patni.directory;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.patni.directory.model.User;
import com.patni.directory.utils.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patni.directory.IntroActivity.sharedPreferences;

public class VerifyOtpActivity extends AppCompatActivity {

    EditText otpView;
    Button verifyButton;
    TextView timeLeftView;
    String phoneNumber;
    String name;
    TextView messageView;
    ProgressBar progressBar;
    String otpBackend;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        getSupportActionBar().hide();
        otpView = findViewById(R.id.otpView);
        verifyButton = findViewById(R.id.verifyButton);
        timeLeftView = findViewById(R.id.timeLeftView);
        messageView = findViewById(R.id.messageView);
        phoneNumber = getIntent().getStringExtra("number");
        name = getIntent().getStringExtra("name");
        otpBackend = getIntent().getStringExtra("otpBackend");
        progressBar = findViewById(R.id.verifyprogressBar);
        progressBar.setVisibility(View.GONE);
        messageView.setText("We have sent an OTP to +91" + phoneNumber + " for phone number verification");
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if ((int) (millisUntilFinished / 1000) < 10)
                    timeLeftView.setText("00:0" + (millisUntilFinished / 1000));
                else
                    timeLeftView.setText("00:" + (int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Toast.makeText(VerifyOtpActivity.this, "Time Out, Try Again!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("number", phoneNumber);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public void verifyOtp(View view) {
        String otpEntered = otpView.getText().toString();
        if (otpEntered.length() == 0) {
            Toast.makeText(this, "Please enter OTP first", Toast.LENGTH_SHORT).show();
        }
        if (otpBackend != null && otpEntered.length() != 0) {
            progressBar.setVisibility(View.VISIBLE);
            verifyButton.setVisibility(View.GONE);
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpBackend, otpEntered);
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        verifyButton.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            sharedPreferences.edit().putBoolean("signIn", true).apply();
                            sharedPreferences.edit().putString("name", name).apply();
                            sharedPreferences.edit().putString("number", phoneNumber).apply();
                            startActivity(intent);
                            countDownTimer.cancel();
                            loginUser(name, phoneNumber);

                        } else {
                            Toast.makeText(VerifyOtpActivity.this, "Wrong OTP, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "Check Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String name, String phoneNumber) {
        User user = new User(name, phoneNumber);
        Call<User> login = RetrofitConfig.apiCalls.loginUser(user);
        login.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.i("Response Code", String.valueOf(response.code()));
                    Toast.makeText(VerifyOtpActivity.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Response Code", String.valueOf(response.code()));
                    Toast.makeText(VerifyOtpActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("Error", t.getMessage());
                Toast.makeText(VerifyOtpActivity.this, "Failed " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}