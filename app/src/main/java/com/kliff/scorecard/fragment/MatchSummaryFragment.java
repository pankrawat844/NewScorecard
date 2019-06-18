package com.kliff.scorecard.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kliff.scorecard.R;
import com.kliff.scorecard.utils.nwUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchSummaryFragment extends Fragment {

    private static final String TAG = "MatchSummaryFragment";

    private String strike_player_id, non_strike_player_id;
    private String striker_bowler_id, non_striker_bowler_id;
    private TextView result;
    private TextView matchNumber;
    private TextView team1;
    private TextView team1Score;
    private TextView team2;
    private TextView team2Score;
    private TextView player1;
    private TextView player1Runs;
    private TextView player1Balls;
    private TextView player14s;
    private TextView player16s;
    private TextView player1Sr;
    private TextView player2;
    private TextView player2Runs;
    private TextView player2Balls;
    private TextView player24s;
    private TextView player26s;
    private TextView player2Sr;
    private TextView bowler1;
    private TextView bowler1Overs;
    private TextView bowler1Maiden;
    private TextView bowler1Runs;
    private TextView bowler1Wickets;
    private TextView bowler1Econ;
    private TextView bowler2;
    private TextView bowler2Overs;
    private TextView bowler2Maiden;
    private TextView bowler2Runs;
    private TextView bowler2Wickets;
    private TextView bowler2Econ;
    private String totleRunsplayer1, totleBallsplayer1;
    private String totleRunsplayer2, totleBallsplayer2;
    private Handler handler;
    private String matchid;
    private String url;

    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "isCalling: ");
            scoreData(url);
            Log.e(TAG, "run: " + url);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        handler = new Handler();
        Bundle bundle = getArguments();
        assert bundle != null;
        matchid = bundle.getString("matchid");
        assert getActivity() != null;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        url = "http://api.espncricinfo.com/4/match/" + matchid + "/scores?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68";
        scoreData(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(this.m_Runnable, (long) nwUtil.refresh_rate);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(this.m_Runnable);
    }

    private void scoreData(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("commentary");
                            JSONObject summary = response.getJSONObject("summary");
                            String description = summary.getString("description");
                            Log.e(TAG, "onResponse: " + description);
                            matchNumber.setText(description);
                            JSONObject player = summary.getJSONObject("player");
                            JSONObject live = summary.getJSONObject("live");
                            JSONObject liveInning = live.getJSONObject("innings");
                            String isLive = liveInning.getString("innings_number");
                            JSONArray team_id = summary.getJSONArray("team");
                            JSONArray innings = summary.getJSONArray("innings");

                            JSONObject inningOne = innings.getJSONObject(0);
                            JSONObject inningTwo = innings.getJSONObject(1);

                            String inningOneNumber = innings.getJSONObject(0).getString("innings_number");
                            Log.e(TAG, "inning_number: " + inningOneNumber);
                            String inningTwoNumber = innings.getJSONObject(1).getString("innings_number");
                            Log.e(TAG, "onResponse: " + inningTwoNumber);


                            String team_1_name = team_id.getJSONObject(0).getString("team_name");
                            String team_2_name = team_id.getJSONObject(1).getString("team_name");

                            Log.e(TAG, "team_1_name: " + team_1_name);
                            Log.e(TAG, "team_2_name: " + team_2_name);

                            team1.setText(team_1_name);
                            team2.setText(team_2_name);

                            if (isLive.equals("")) {
                                Log.e(TAG, "Match is not live: ");
                            } else {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject commentaryData = jsonArray.getJSONObject(0);
                                    strike_player_id = commentaryData.getString("batsman_player_id");
                                    non_strike_player_id = commentaryData.getString("nonstriker_player_id");
                                    striker_bowler_id = commentaryData.getString("bowler_player_id");
                                    non_striker_bowler_id = commentaryData.getString("other_bowler_player_id");
                                    player1Runs.setText(commentaryData.getString("batsman_total_runs"));
                                    player2Runs.setText(commentaryData.getString("nonstriker_total_runs"));
                                    player1Balls.setText(commentaryData.getString("batsman_balls_faced"));
                                    player2Balls.setText(commentaryData.getString("nonstriker_balls_faced"));
                                    player14s.setText(commentaryData.getString("batsman_fours"));
                                    player24s.setText(commentaryData.getString("nonstriker_fours"));
                                    player16s.setText(commentaryData.getString("batsman_sixes"));
                                    player26s.setText(commentaryData.getString("nonstriker_sixes"));
                                    /*String strikrate = String.valueOf(player1Runs / player1Balls);
                                    player1Sr.setText(strikrate);*/

                                    bowler1Overs.setText(commentaryData.getString("bowler_overs"));
                                    bowler2Overs.setText(commentaryData.getString("other_bowler_overs"));
                                    bowler1Maiden.setText(commentaryData.getString("bowler_maidens"));
                                    bowler2Maiden.setText(commentaryData.getString("other_bowler_maidens"));
                                    bowler1Runs.setText(commentaryData.getString("bowler_conceded"));
                                    bowler2Runs.setText(commentaryData.getString("other_bowler_conceded"));
                                    bowler1Wickets.setText(commentaryData.getString("bowler_wickets"));
                                    bowler2Wickets.setText(commentaryData.getString("other_bowler_wickets"));

                                    if (inningTwoNumber.equals("2")) {

                                        team1Score.setText(commentaryData.getString("runs") + "/" + commentaryData.getString("wickets")
                                                + " (" + commentaryData.getString("overs_actual") + "/" + 50 + ")");
                                        Log.e(TAG, "runs: " + commentaryData.getString("runs"));
                                        Log.e(TAG, "wickets: " + commentaryData.getString("wickets"));
                                        Log.e(TAG, "overs_actual: " + commentaryData.getString("overs_actual"));
                                    }

                                    if (inningOneNumber.equals("1")) {
                                        team2Score.setText(inningOne.getString("runs") + "/" + inningOne.getString("wickets"));
                                    }
                                }

                                if (player.has(strike_player_id)) {
                                    JSONObject players_name = player.getJSONObject(strike_player_id);
                                    player1.setText(players_name.getString("known_as"));
                                    Log.e(TAG, "player name: " + players_name.getString("known_as"));
                                }

                                if (player.has(non_strike_player_id)) {
                                    JSONObject players_name = player.getJSONObject(non_strike_player_id);
                                    player2.setText(players_name.getString("known_as"));
                                    Log.e("player name", players_name.getString("known_as"));
                                }

                                if (player.has(striker_bowler_id)) {
                                    JSONObject players_name = player.getJSONObject(striker_bowler_id);
                                    bowler1.setText(players_name.getString("known_as"));
                                    Log.e("player name", players_name.getString("known_as"));
                                }

                                if (player.has(non_striker_bowler_id)) {
                                    JSONObject players_name = player.getJSONObject(non_striker_bowler_id);
                                    bowler2.setText(players_name.getString("known_as"));
                                    Log.e("player name", players_name.getString("known_as"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        if (getActivity() != null) {
            Volley.newRequestQueue(getActivity()).add(request);
        }
    }


    private void initView(View view) {
        result = view.findViewById(R.id.result);
        matchNumber = view.findViewById(R.id.match_number);
        team1 = view.findViewById(R.id.team_1);
        team1Score = view.findViewById(R.id.team_1_score);
        team2 = view.findViewById(R.id.team_2);
        team2Score = view.findViewById(R.id.team_2_score);
        player1 = view.findViewById(R.id.player_1);
        player1Runs = view.findViewById(R.id.player_1_runs);
        player1Balls = view.findViewById(R.id.player_1_balls);
        player14s = view.findViewById(R.id.player_1_4s);
        player16s = view.findViewById(R.id.player_1_6s);
        player1Sr = view.findViewById(R.id.player_1_sr);
        player2 = view.findViewById(R.id.player_2);
        player2Runs = view.findViewById(R.id.player_2_runs);
        player2Balls = view.findViewById(R.id.player_2_balls);
        player24s = view.findViewById(R.id.player_2_4s);
        player26s = view.findViewById(R.id.player_2_6s);
        player2Sr = view.findViewById(R.id.player_2_sr);
        bowler1 = view.findViewById(R.id.bowler_1);
        bowler1Overs = view.findViewById(R.id.bowler_1_overs);
        bowler1Maiden = view.findViewById(R.id.bowler_1_maiden);
        bowler1Runs = view.findViewById(R.id.bowler_1_runs);
        bowler1Wickets = view.findViewById(R.id.bowler_1_wickets);
        bowler1Econ = view.findViewById(R.id.bowler_1_econ);
        bowler2 = view.findViewById(R.id.bowler_2);
        bowler2Overs = view.findViewById(R.id.bowler_2_overs);
        bowler2Maiden = view.findViewById(R.id.bowler_2_maiden);
        bowler2Runs = view.findViewById(R.id.bowler_2_runs);
        bowler2Wickets = view.findViewById(R.id.bowler_2_wickets);
        bowler2Econ = view.findViewById(R.id.bowler_2_econ);
    }
}
