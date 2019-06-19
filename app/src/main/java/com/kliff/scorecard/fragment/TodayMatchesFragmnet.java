package com.kliff.scorecard.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kliff.scorecard.R;
import com.kliff.scorecard.activities.FullScoreDetailsActivity;
import com.kliff.scorecard.adapter.MatchListAdapter;
import com.kliff.scorecard.adapter.RecyclerItemClickListener;
import com.kliff.scorecard.model.MatchList;
import com.kliff.scorecard.utils.nwUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodayMatchesFragmnet extends Fragment {

    private static final String TAG = "com.live_cric_scores";

    private RecyclerView recyclerView;
    private MatchListAdapter adapter;
    private Handler mHandler;
    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {
            try {
                TodayMatchesFragmnet.this.stopRepeatingTask();
                TodayMatchesFragmnet.this.executeURL();
            } finally {
                TodayMatchesFragmnet.this.mHandler.postDelayed(TodayMatchesFragmnet.this.m_Runnable, 3000);
            }
        }
    };

    private List<MatchList> list = new ArrayList<>();
    private String match_status;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_matches, container, false);
    }

    /* renamed from: com.live_cric_scores.MainActivity$3 */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        recyclerView = view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fillMatchListESPN(nwUtil.ESPNMatchListJASONData);
        executeURL();

        this.mHandler = new Handler();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), FullScoreDetailsActivity.class);
                intent.putExtra("matchid", list.get(position).getMatchid());
                intent.putExtra("match_status", list.get(position).getMatch_status());
                startActivity(intent);
            }
        }));
    }

    public void onResume() {
        Log.d(TAG, " : onResume-->startRepeatingTask");
        super.onResume();
        startRepeatingTask();
    }

    public void onPause() {
        Log.d(TAG, " : onPause-->stopRepeatingTask");
        stopRepeatingTask();
        super.onPause();
    }

    private void startRepeatingTask() {
        mHandler.postDelayed(m_Runnable, 3000);
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(m_Runnable);
    }

    private void executeURL() {
        int i = R.string.SEND_ERR;
        Log.d(TAG, "executeURL :" + "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68");

        AsyncTask<String, String, String> executeReturn;
        executeReturn = new DisplayMatcheListESPN().execute("http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68");
        if (executeReturn != null) {
            i = R.string.SENT_REQUEST;
        }
        return;
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    private void fillMatchListESPN(String result) {
        try {
            nwUtil.ESPNMatchListJASONData = result;
            JSONArray matches = new JSONObject(result).getJSONArray("matches");

            list.clear();
            for (int i = 0; i < matches.length(); i++) {
                JSONObject series = matches.getJSONObject(i).getJSONObject(matches.getJSONObject(i).names().getString(0));
                JSONArray data = series.getJSONArray("data");
                for (int j = 0; j < data.length(); j++) {
                    MatchList matchList = new MatchList();
                    JSONObject jsonObject = data.getJSONObject(j);
                    String start_date = jsonObject.getJSONObject("match").getString("start_date");

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c);

                    if (start_date.equals(formattedDate)) {
                        String team1_Sname = series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team1_id")).getString("n");
                        String team2_Sname = series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team2_id")).getString("n");

                        matchList.setTeam_id1(data.getJSONObject(j).getJSONObject("match").getString("team1_id"));
                        matchList.setTeam_id2(data.getJSONObject(j).getJSONObject("match").getString("team2_id"));
                        matchList.setTeam1(team1_Sname);
                        matchList.setTeam2(team2_Sname);

                        matchList.setTeam1_img("http://api.espncricinfo.com" + series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team1_id")).getString("f"));
                        matchList.setTeam2_img("http://api.espncricinfo.com" + series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team2_id")).getString("f"));
                        matchList.setMatchid(data.getJSONObject(j).getString("object_id"));
                        matchList.setMatch_status(data.getJSONObject(j).getJSONObject("match").getString("match_status"));
                        matchList.setVenue(data.getJSONObject(j).getString("description"));
                        matchList.setResult(data.getJSONObject(j).getJSONObject("live").getString("status"));

                        if (data.getJSONObject(j).getJSONArray("innings").length() > 0) {
                            if (!data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("batting_team_id").equals(matchList.getTeam_id1())) {
                                matchList.setTeam1(team2_Sname);
                                matchList.setTeam2(team1_Sname);
                                matchList.setTeam2_img("http://api.espncricinfo.com" + series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team1_id")).getString("f"));
                                matchList.setTeam1_img("http://api.espncricinfo.com" + series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team2_id")).getString("f"));
                            }
                            matchList.setScore1(data.getJSONObject(j).getJSONArray("innings").optJSONObject(0).getString("runs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("wickets"));
                            if (!data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("scheduled_overs").equals("0")) {
                                matchList.setWicket1(data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("overs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("scheduled_overs"));
                            } else
                                matchList.setWicket1(data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("overs"));

                            matchList.setScore2(data.getJSONObject(j).getJSONArray("innings").optJSONObject(1).getString("runs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("wickets"));
                            if (!data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("scheduled_overs").equals("0"))
                                matchList.setWicket2(data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("overs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("scheduled_overs"));
                            else
                                matchList.setWicket2(data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("overs"));
                        }
                        list.add(matchList);
                    }
                }
            }
            if (adapter == null) {
                adapter = new MatchListAdapter(list);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
//                recyclerView.getLayoutManager().scrollToPosition(recylerview_position);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
        }
    }

    public class DisplayMatcheListESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            fillMatchListESPN(result);
        }
    }
}
