package com.kliff.scorecard.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kliff.scorecard.R;
import com.kliff.scorecard.adapter.ViewPagerAdapter;
import com.kliff.scorecard.fragment.CommentrayFragment;
import com.kliff.scorecard.fragment.CricInfoDetailScoreFragment;
import com.kliff.scorecard.fragment.MatchSummaryFragment;

public class FullScoreDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_score_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MatchSummaryFragment(), "Match Summary");
//        adapter.addFragment(new CricInfoDetailScoreFragment(), "Full Score");
        adapter.addFragment(new CommentrayFragment(), "Commentary");
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
    }
}
