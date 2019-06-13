package com.kliff.scorecard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import customfont.GothamBold;


public class MainFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*getChildFragmentManager().beginTransaction()
                .replace(R.id.scoreFrame, new LiveScoreFragmnet())
                .commit();*/

        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.scoreFrame, new LiveScoreFragmnet())
                    .commit();
        }

        GothamBold live_score = view.findViewById(R.id.liveScore);
        GothamBold all_matches = view.findViewById(R.id.allMatches);

        live_score.setOnClickListener(this);
        all_matches.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liveScore:
                liveScore();
                break;

            case R.id.allMatches:
                allMatches();
                break;
        }
    }

    private void liveScore() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.scoreFrame, new LiveScoreFragmnet())
                .commit();
    }

    private void allMatches() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.scoreFrame, new AllMatchesFragment())
                .commit();
    }
}
