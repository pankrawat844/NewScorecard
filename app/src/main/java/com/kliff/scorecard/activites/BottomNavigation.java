package com.kliff.scorecard.activites;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kliff.scorecard.R;
import com.kliff.scorecard.fragment.MainFragment;
import com.kliff.scorecard.fragment.NewsFragment;

public class BottomNavigation extends AppCompatActivity {

    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MainFragment()).commit();
                    return true;

                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new NewsFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        getSupportFragmentManager().beginTransaction().add(R.id.frame, new MainFragment()).commit();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
    }
}
