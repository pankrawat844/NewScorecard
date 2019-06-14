package com.kliff.scorecard.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kliff.scorecard.R;
import com.kliff.scorecard.activites.CricInfoDetailScoreActivity;
import com.kliff.scorecard.adapter.MatchListAdapter;
import com.kliff.scorecard.adapter.RecyclerItemClickListener;
import com.kliff.scorecard.model.MatchList;
import com.kliff.scorecard.utils.Utils;
import com.kliff.scorecard.utils.nwUtil;
import com.kliff.scorecard.utils.tableUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveScoreFragmnet extends Fragment {

    private static final String TAG = "com.live_cric_scores";
    private static boolean IsMatchListAvailableESPN = true;
    private static boolean IsMatcheListAndMiniScoreCBZRan = false;
    private static boolean IsMatcheListESPNRan = false;
    private static boolean IsMiniScoreESPNRan = false;
    private static MainFragment MainActivity;
    private final Runnable m_Runnable = new LiveScoreFragmnet.C04552();
    private final View.OnClickListener refreshAllMacthtListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (nwUtil.ESPNMatchList.size() == 0) {
                LiveScoreFragmnet.this.executeURL(2, "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68");
            } else {
                LiveScoreFragmnet.this.refreshMatchESPN(nwUtil.selectedESPNMatchID);
            }
        }
    };

    ImageButton btnRefresh;
    RecyclerView recyclerView;
    private Handler mHandler;
    private ImageButton btnDetailScoreCBZ;
    private ImageButton btnDetailScoreESPN;
    private Button btnSelectMatch1;
    private Button btnSelectMatch2;

    private TextView tvCBZSpinner;
    private List<MatchList> list = new ArrayList<>();
    private SwipeRefreshLayout swipeContainer;
    private TextView tvCBZstatus;
    private TextView tvESPNSpinner;
    private TextView tvESPNstatus;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_score_fragmnet, container, false);
    }

    /* renamed from: com.live_cric_scores.MainActivity$3 */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        recyclerView = view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fillMatchListESPN(nwUtil.ESPNMatchListJASONData);
        executeURL(2, "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68");

        this.mHandler = new Handler();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CricInfoDetailScoreActivity.class);
                intent.putExtra("matchid", list.get(position).getMatchid());
//                nwUtil.selectedESPNMatchID = list.get(position).getMatchid();
                getActivity().finish();
                startActivity(intent);
            }
        }));
    }

    /* renamed from: com.live_cric_scores.MainActivity$6 */

    public void onResume() {
        Log.d(TAG, " : onResume-->startRepeatingTask");
        super.onResume();
        if (nwUtil.auto_refresh) {
            startRepeatingTask();
//            this.btnRefresh.setImageResource(R.drawable.ic_refresh_white_36dp);
            return;
        }
//        this.btnRefresh.setImageResource(R.drawable.ic_refresh_white_36dp);
    }

    public void onPause() {
        Log.d(TAG, " : onPause-->stopRepeatingTask");
        stopRepeatingTask();
        super.onPause();
    }

    private void startRepeatingTask() {
        mHandler.postDelayed(m_Runnable, (long) nwUtil.refresh_rate);
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(m_Runnable);
    }

    private void executeURL(int Command, String URL) {
        int i = R.string.SEND_ERR;
        Log.d(TAG, "executeURL :" + URL);

        AsyncTask<String, String, String> executeReturn = null;
        switch (Command) {
            case 1:
                IsMatcheListAndMiniScoreCBZRan = false;
                executeReturn = new LiveScoreFragmnet.DisplayMatcheListAndMiniScoreCBZ().execute(new String[]{URL});
                break;
            case 2:
                IsMatcheListESPNRan = false;
                IsMiniScoreESPNRan = false;
                executeReturn = new LiveScoreFragmnet.DisplayMatcheListESPN().execute(new String[]{URL});
                break;
            case 3:
                IsMatcheListESPNRan = false;
                IsMiniScoreESPNRan = false;
                executeReturn = new LiveScoreFragmnet.DisplayMiniScoreESPN().execute(new String[]{URL});
                break;
        }
        if (executeReturn != null) {
            i = R.string.SENT_REQUEST;
        }
        return;

    }

    /* renamed from: com.live_cric_scores.MainActivity$1 */

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    private void fillMatchListAndMiniScoreCBZ(String result) {
        if (result == null) {
            tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        } else if (result.isEmpty()) {
            tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        } else {
            try {
                nwUtil.CBZMatchListAndMiniscoreJASONData = result;
                this.tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GET_RESPONSE)));
                boolean isCBZMatchListEmpty = false;
                if (nwUtil.CBZMatchList.isEmpty()) {
                    isCBZMatchListEmpty = true;
                }
                JSONArray matches = new JSONArray(result);
                if (matches.length() > 0 && isCBZMatchListEmpty) {
                    nwUtil.CBZMatchList.add(getString(R.string.none));
                }
                for (int i = 0; i < matches.length(); i++) {
                    String matchName = matches.getJSONObject(i).getJSONObject("header").getString("mchDesc").trim() + " (" + matches.getJSONObject(i).getJSONObject("header").getString("mchState") + ")";
                    if (isCBZMatchListEmpty) {
                        nwUtil.CBZMatchList.add(matchName);
                    } else if (matchName.contains(nwUtil.selectedCBZMatch)) {
                        nwUtil.selectedCBZMatchdataPath = matches.getJSONObject(i).getString("datapath");
                        JSONObject header = matches.getJSONObject(i).getJSONObject("header");
                        JSONObject miniscore = matches.getJSONObject(i).optJSONObject("miniscore");
                        JSONObject team1 = matches.getJSONObject(i).optJSONObject("team1");
                        JSONObject team2 = matches.getJSONObject(i).optJSONObject("team2");
                        if (miniscore != null) {
                            String batingTeamName;
                            String bowlingTeamName;
                            if (miniscore.getString("batteamid").equals(team1.getString("id"))) {
                                batingTeamName = team1.getString("name");
                                bowlingTeamName = team2.getString("name");
                            } else {
                                bowlingTeamName = team1.getString("name");
                                batingTeamName = team2.getString("name");
                            }


                        }
                        this.tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GOT_RESPONSE)));
                    }
                }
                this.tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GOT_RESPONSE)));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
                this.tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
            }
        }
    }

    private void refreshMatchESPN(String matchId) {
        if (Utils.isMatchSelected4ESPN(getActivity())) {
            //executeURL(3, String.format("http://www.espncricinfo.com/ci/engine/match/%s.json?xhr=1", new Object[]{matchId.trim()}));
            return;
        }
        Log.d(TAG, "matchId : " + matchId + "is invalid, disable refresh button ?");
    }


//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.d(TAG, " : onRestoreInstanceState");
//        super.onRestoreInstanceState(savedInstanceState);
//        restoreState(savedInstanceState);
//    }

    private void fillMatchListESPN(String result) {

        try {
            nwUtil.ESPNMatchListJASONData = result;
//                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_RESPONSE)));
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
                        matchList.setVenue(data.getJSONObject(j).getString("description"));
                        matchList.setResult(data.getJSONObject(j).getJSONObject("live").getString("status"));
//
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
//                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GOT_RESPONSE)));

            recyclerView.setAdapter(new MatchListAdapter(list));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
//                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        }

    }


    /* renamed from: com.live_cric_scores.MainActivity$2 */
    class C04552 implements Runnable {
        C04552() {
        }

        public void run() {
            try {
                LiveScoreFragmnet.this.stopRepeatingTask();
//            MainFragment.this.btnRefresh.performClick();
            } finally {
                LiveScoreFragmnet.this.mHandler.postDelayed(LiveScoreFragmnet.this.m_Runnable, (long) nwUtil.refresh_rate);
            }
        }
    }

    /* renamed from: com.live_cric_scores.MainActivity$5 */
    class C04585 implements DialogInterface.OnClickListener {
        C04585() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.live_cric_scores.MainActivity$8 */
    class C04618 implements DialogInterface.OnClickListener {
        C04618() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    /* renamed from: com.live_cric_scores.MainActivity$9 */
    class C04629 implements View.OnClickListener {
        C04629() {
        }

        public void onClick(View v) {
            try {
//            v.getContext().startActivity(new Intent(v.getContext(), CricbuzzDetailScoreActivity.class));
//            MainActivity.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e.getCause());
            }
        }
    }

    public class DisplayMatcheListAndMiniScoreCBZ extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            IsMatcheListAndMiniScoreCBZRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            LiveScoreFragmnet.this.fillMatchListAndMiniScoreCBZ(result);
            IsMatcheListAndMiniScoreCBZRan = true;
            if (IsMatcheListESPNRan || IsMiniScoreESPNRan) {
                Utils.clearAnimation(LiveScoreFragmnet.this.btnRefresh);
            }
        }
    }

    public class DisplayMatcheListESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            IsMatcheListESPNRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            LiveScoreFragmnet.this.fillMatchListESPN(result);
            IsMatcheListESPNRan = true;
            if (IsMatcheListAndMiniScoreCBZRan) {
                Utils.clearAnimation(LiveScoreFragmnet.this.btnRefresh);
            }
        }
    }

    public class DisplayMiniScoreESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            IsMiniScoreESPNRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            IsMiniScoreESPNRan = true;
            if (IsMatcheListAndMiniScoreCBZRan) {
                Utils.clearAnimation(LiveScoreFragmnet.this.btnRefresh);
            }
        }
    }
}
