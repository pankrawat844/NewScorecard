package com.kliff.scorecard.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kliff.scorecard.R;
import com.kliff.scorecard.adapter.ScoreSummaryAdapter;
import com.kliff.scorecard.model.ScoreSummaryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    private Handler handler;
    private String matchid;
    private String match_status;
    private String status;
    private String full_description;
    private String url;
    private String team1_id_inning;
    private String team2_id_inning;
    private CardView matchDetailCardView;
    private ContentLoadingProgressBar loading;
    private TextView recent;
    private RecyclerView recylerview;
    private ScoreSummaryAdapter summaryAdapter;
    private List<ScoreSummaryModel> modelList;

    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "isCalling: ");
            if (match_status.equals("dormant")) {
                matchDetailCardView.setVisibility(View.GONE);
                handler.postDelayed(m_Runnable, (long) 1000);
            } else {
                if (match_status.equals("current")) {
                    scoreData(url, 0);
                    handler.postDelayed(m_Runnable, 1000);
                } else
                    scoreData(url, 1);
            }
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
        match_status = bundle.getString("match_status");
        status = bundle.getString("status");
        full_description = bundle.getString("full_description");

        modelList = new ArrayList<>();
        recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.e(TAG, "match_live_status: " + match_status);
        loading.setVisibility(View.VISIBLE);
        assert getActivity() != null;
        url = "http://api.espncricinfo.com/4/match/" + matchid + "/scores?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68";
        if (match_status.equals("dormant")) {
            loading.setVisibility(View.GONE);
            result.setText(status);
            matchNumber.setText(full_description);
            matchDetailCardView.setVisibility(View.GONE);
        } else {
            if (match_status.equals("current"))
                scoreData(url, 0);
            else
                scoreData(url, 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(this.m_Runnable, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(this.m_Runnable);
    }

    private void scoreData(String url, final int val) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.setVisibility(View.GONE);
                        try {

                            JSONArray jsonArray = response.getJSONArray("commentary");
                            JSONObject summary = response.getJSONObject("summary");
                            String description = summary.getString("description");
                            Log.e(TAG, "onResponse: " + description);
                            matchNumber.setText(description);
                            JSONObject player = summary.getJSONObject("player");
                            if (summary.getJSONObject("live").getString("break").equals("")) {
                                result.setText(summary.getJSONObject("live").getString("status"));
                            } else {
                                result.setText(summary.getJSONObject("live").getString("break"));
                            }

                            recent.setText("Recent Deliveries : " + Html.fromHtml(summary.getJSONObject("live").getString("ball_string")));

                            //Team Name
                            team1_id_inning = summary.getJSONArray("innings").getJSONObject(0).getString("batting_team_id");

                            JSONObject inning_one_data = summary.getJSONArray("innings").getJSONObject(0);
                            JSONObject inning_two_data = summary.getJSONArray("innings").getJSONObject(1);

                            JSONArray team = summary.getJSONArray("team");
                            String team1_id_team = team.getJSONObject(0).getString("team_id");

                            if (team1_id_inning.equals(team1_id_team)) {
                                team1.setText(team.getJSONObject(0).getString("team_name"));
                                if (inning_one_data.getString("scheduled_overs").equals("0")) {
                                    team1Score.setText(inning_one_data.getString("runs") + "/" + inning_one_data.getString("wickets")
                                            + " (" + inning_one_data.getString("overs") + ")");
                                } else {
                                    team1Score.setText(inning_one_data.getString("runs") + "/" + inning_one_data.getString("wickets")
                                            + " (" + inning_one_data.getString("overs") + "/" + inning_one_data.getString("scheduled_overs") + ")");
                                }
                                team2.setText(team.getJSONObject(1).getString("team_name"));
                                if (inning_two_data.getString("scheduled_overs").equals("0")) {
                                    team2Score.setText(inning_two_data.getString("runs") + "/" + inning_two_data.getString("wickets")
                                            + " (" + inning_two_data.getString("overs") + ")");
                                } else {

                                    team2Score.setText(inning_two_data.getString("runs") + "/" + inning_two_data.getString("wickets")
                                            + " (" + inning_two_data.getString("overs") + "/" + inning_two_data.getString("scheduled_overs") + ")");
                                }
                            } else {
                                team1.setText(team.getJSONObject(1).getString("team_name"));
                                if (inning_one_data.getString("scheduled_overs").equals("0")) {

                                    team1Score.setText(inning_one_data.getString("runs") + "/" + inning_one_data.getString("wickets")
                                            + " (" + inning_one_data.getString("overs") + ")");
                                } else {

                                    team1Score.setText(inning_one_data.getString("runs") + "/" + inning_one_data.getString("wickets")
                                            + " (" + inning_one_data.getString("overs") + "/" + inning_one_data.getString("scheduled_overs") + ")");
                                }
                                team2.setText(team.getJSONObject(0).getString("team_name"));
                                if (inning_two_data.getString("scheduled_overs").equals("0")) {
                                    team2Score.setText(inning_two_data.getString("runs") + "/" + inning_two_data.getString("wickets")
                                            + " (" + inning_two_data.getString("overs") + ")");
                                } else {
                                    team2Score.setText(inning_two_data.getString("runs") + "/" + inning_two_data.getString("wickets")
                                            + " (" + inning_two_data.getString("overs") + "/" + inning_two_data.getString("scheduled_overs") + ")");
                                }
                            }

                            JSONObject commentaryData;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (val == 0)
                                    commentaryData = jsonArray.getJSONObject(0);
                                else
                                    commentaryData = jsonArray.getJSONObject(jsonArray.length() - 1);

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
                                DecimalFormat dec = new DecimalFormat("#0.00");

                                double p1r = Double.valueOf(commentaryData.getString("batsman_total_runs"));
                                double p2r = Double.valueOf(commentaryData.getString("nonstriker_total_runs"));
                                double p1b = Double.valueOf(commentaryData.getString("batsman_balls_faced"));
                                double p2b = Double.valueOf(commentaryData.getString("nonstriker_balls_faced"));

                                String p1StrikeRate = dec.format(Double.valueOf((p1r / p1b) * 100));
                                String p2StrikeRate = dec.format(Double.valueOf((p2r / p2b) * 100));

                                player1Sr.setText(p1StrikeRate);
                                player2Sr.setText(p2StrikeRate);

                                bowler1Overs.setText(commentaryData.getString("bowler_overs"));
                                bowler2Overs.setText(commentaryData.getString("other_bowler_overs"));
                                bowler1Maiden.setText(commentaryData.getString("bowler_maidens"));
                                bowler2Maiden.setText(commentaryData.getString("other_bowler_maidens"));
                                bowler1Runs.setText(commentaryData.getString("bowler_conceded"));
                                bowler2Runs.setText(commentaryData.getString("other_bowler_conceded"));
                                bowler1Wickets.setText(commentaryData.getString("bowler_wickets"));
                                bowler2Wickets.setText(commentaryData.getString("other_bowler_wickets"));

                                double b2r = 0.0;
                                double b2o = 0.0;
                                if (!commentaryData.getString("other_bowler_overs").equals("") &&
                                        !commentaryData.getString("other_bowler_conceded").equals("")) {
                                    b2r = Double.valueOf(commentaryData.getString("other_bowler_conceded"));
                                    b2o = Double.valueOf(commentaryData.getString("other_bowler_overs"));
                                }

                                double b1r = Double.valueOf(commentaryData.getString("bowler_conceded"));

                                double b1o = Double.valueOf(commentaryData.getString("bowler_overs"));


                                String b1Econ = dec.format(Double.valueOf((b1r / b1o)));
                                String b2Econ = dec.format(Double.valueOf((b2r / b2o)));
                                bowler1Econ.setText(b1Econ);
                                bowler2Econ.setText(b2Econ);
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
                            modelList.clear();

                            if (match_status.equals("current")) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    ScoreSummaryModel model = new ScoreSummaryModel();
                                    Log.e(TAG, "ruunig: ");
                                    JSONObject scoreCardData = jsonArray.getJSONObject(j);
                                    model.setBall_text("" + Html.fromHtml(scoreCardData.getString("ball_text")));
                                    model.setCms_text("" + Html.fromHtml(scoreCardData.getString("cms_text")));
                                    modelList.add(model);
                                }

                            } else {
                                for (int j = jsonArray.length() - 1; j >= 0; j--) {
                                    ScoreSummaryModel model = new ScoreSummaryModel();
                                    Log.e(TAG, "ruunig: ");
                                    JSONObject scoreCardData = jsonArray.getJSONObject(j);
                                    model.setBall_text("" + Html.fromHtml(scoreCardData.getString("ball_text")));
                                    model.setCms_text("" + Html.fromHtml(scoreCardData.getString("cms_text")));
                                    modelList.add(model);
                                }

                            }


                            if (summaryAdapter != null) {
                                summaryAdapter = null;
                                summaryAdapter = new ScoreSummaryAdapter(modelList);
                                recylerview.setAdapter(summaryAdapter);
                            } else {
                                summaryAdapter = new ScoreSummaryAdapter(modelList);
                                recylerview.setAdapter(summaryAdapter);
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
        matchDetailCardView = view.findViewById(R.id.matchDetailCardView);
        loading = view.findViewById(R.id.loading);
        recent = view.findViewById(R.id.recent);
        recylerview = view.findViewById(R.id.recylerview);
    }
}
