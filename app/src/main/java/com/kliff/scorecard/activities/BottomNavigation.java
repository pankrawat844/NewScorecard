package com.kliff.scorecard.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kliff.scorecard.R;
import com.kliff.scorecard.fragment.MainFragment;
import com.kliff.scorecard.fragment.NewsFragment;
import com.kliff.scorecard.fragment.WatchLiveFragment;

public class BottomNavigation extends AppCompatActivity {

    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selected = new MainFragment();
                    break;

                case R.id.navigation_notifications:
                    selected = new NewsFragment();
                    break;

                case R.id.navigation_watch:
                    selected = new WatchLiveFragment();
                    break;
            }
            assert selected != null;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, selected).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new MainFragment())
                .commit();
    }
}
