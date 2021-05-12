package com.patni.directory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.patni.directory.model.Adds;
import com.patni.directory.utils.RetrofitConfig;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.patni.directory.IntroActivity.sharedPreferences;

public class HomeActivity extends AppCompatActivity {

    ImageView addImageView;
    TextView welcomeMessageView;
    String fileName;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeMessageView = findViewById(R.id.welcomeMessage);
        addImageView = findViewById(R.id.addImageView1);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        String name = sharedPreferences.getString("name", "NA");
        welcomeMessageView.setText("Hello " + name + ", welcome to Directory feel free to explore and connect with people.");

        Call<Adds> addsCall = RetrofitConfig.apiCalls.getAdds(1L);
        addsCall.enqueue(new Callback<Adds>() {
            @Override
            public void onResponse(Call<Adds> call, Response<Adds> response) {
                if (!response.isSuccessful()) {
                    Log.i("response adds", String.valueOf(response.code()));
                } else {
                    fileName = response.body().getAddImageUrl();
                    ImageDownloader imageDownloader = new ImageDownloader();
                    Bitmap myImage;
                    try {
                        myImage = imageDownloader.execute(fileName).get();
                        addImageView.setImageBitmap(myImage);

                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onFailure(Call<Adds> call, Throwable t) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_news:
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.nav_favourites:
                    startActivity(new Intent(getApplicationContext(), SavedActivity.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.nav_home:
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        switch (item.getItemId()){
            case R.id.aboutApp:
                intent=new Intent(getApplicationContext(), AboutAppActivity.class);
                startActivity(intent);
                break;
            case R.id.contact:
                intent=new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.share:
                Log.i("Share App",item.toString());
                break;
            default:
                return false;
        }
        return true;
    }

    public void searchPeople(View view) {

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}