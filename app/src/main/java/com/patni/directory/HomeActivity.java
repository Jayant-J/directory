package com.patni.directory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_news:
                if (!getSupportFragmentManager().getFragments().get(0).toString().startsWith("NewsFragment"))
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                            .replace(R.id.fragment_layout, new NewsFragment())
                            .commit();
                break;
            case R.id.nav_home:
                if (!getSupportFragmentManager().getFragments().get(0).toString().startsWith("HomeFragment"))
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new HomeFragment())
                            .commit();
                break;
            case R.id.nav_favourites:
                if (!getSupportFragmentManager().getFragments().get(0).toString().startsWith("SavedFragment"))
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.fragment_layout, new SavedFragment())
                            .commit();
        }
        return true;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutApp:
                startActivity(new Intent(getApplicationContext(), AboutAppActivity.class));
                break;
            case R.id.contact:
                startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
                break;
            case R.id.share:
                //Ask user to share the app
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}