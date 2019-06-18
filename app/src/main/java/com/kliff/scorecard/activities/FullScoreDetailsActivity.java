package com.kliff.scorecard.activities;

import android.content.Intent;
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
        MatchSummaryFragment matchSummaryFragment = new MatchSummaryFragment();
        CommentrayFragment commentrayFragment = new CommentrayFragment();
        CricInfoDetailScoreFragment info= new CricInfoDetailScoreFragment();
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString("matchid", intent.getStringExtra("matchid"));
        matchSummaryFragment.setArguments(bundle);
        commentrayFragment.setArguments(bundle);
        info.setArguments(bundle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(matchSummaryFragment, "Match Summary");
        adapter.addFragment(info, "Full Score");
        adapter.addFragment(commentrayFragment, "Commentary");
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
    }
}
