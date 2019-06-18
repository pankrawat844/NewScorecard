package com.kliff.scorecard.utils;

import android.view.View;
import android.widget.TextView;

import com.kliff.scorecard.R;

public class ScoreSummaryUtil {

    public static void init(View view) {
        TextView player_1 = view.findViewById(R.id.player_1);
        TextView player_2 = view.findViewById(R.id.player_2);
        TextView bowler_1 = view.findViewById(R.id.bowler_1);
        TextView bowler_2 = view.findViewById(R.id.bowler_2);
        TextView player_1_runs = view.findViewById(R.id.player_1_runs);
        TextView player_2_runs = view.findViewById(R.id.player_2_runs);
        TextView bowler_1_runs = view.findViewById(R.id.bowler_1_runs);
        TextView bowler_2_runs = view.findViewById(R.id.bowler_2_runs);
        TextView player_1_balls = view.findViewById(R.id.player_1_balls);
        TextView player_2_balls = view.findViewById(R.id.player_2_balls);
        TextView player_1_fours = view.findViewById(R.id.player_1_4s);
        TextView player_2_fours = view.findViewById(R.id.player_2_4s);
        TextView player_1_sixes = view.findViewById(R.id.player_1_6s);
        TextView player_2_sixes = view.findViewById(R.id.player_2_6s);
        TextView player_1_strikRate = view.findViewById(R.id.player_1_sr);
        TextView player_2_strikRate = view.findViewById(R.id.player_2_sr);
        TextView bowler_1_overs = view.findViewById(R.id.bowler_1_overs);
        TextView bowler_2_overs = view.findViewById(R.id.bowler_2_overs);
        TextView bowler_1_maidens = view.findViewById(R.id.bowler_1_maiden);
        TextView bowler_2_maidens = view.findViewById(R.id.bowler_2_maiden);
        TextView bowler_1_wickets = view.findViewById(R.id.bowler_1_wickets);
        TextView bowler_2_wickets = view.findViewById(R.id.bowler_2_wickets);
        TextView bowler_1_econ = view.findViewById(R.id.bowler_1_econ);
        TextView bowler_2_econ = view.findViewById(R.id.bowler_2_econ);
    }
}
