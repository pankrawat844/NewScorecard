package com.kliff.scorecard.activites;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kliff.scorecard.R;
import com.kliff.scorecard.adapter.MatchListAdapter;
import com.kliff.scorecard.adapter.RecyclerItemClickListener;
import com.kliff.scorecard.model.MatchList;
import com.kliff.scorecard.utils.Utils;
import com.kliff.scorecard.utils.nwUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity1 extends Activity {
    private static final String TAG = "com.live_cric_scores";
    public static boolean IsMatchListAvailableESPN = true;
    public static boolean IsMatcheListAndMiniScoreCBZRan = false;
    public static boolean IsMatcheListESPNRan = false;
    public static boolean IsMiniScoreESPNRan = false;
    private static MainActivity1 MainActivity;
    private final View.OnClickListener detailScoreCBZListener = new C04629();
    private final View.OnClickListener detailScoreESPNListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                v.getContext().startActivity(new Intent(v.getContext(), CricInfoDetailScoreActivity.class));
                MainActivity1.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } catch (Exception e) {
                Log.e(MainActivity.TAG, e.getMessage(), e.getCause());
            }
        }
    };
    private final Runnable m_Runnable = new C04552();
    private final View.OnClickListener refreshSetting = new View.OnClickListener() {
        public void onClick(View v) {
            MainActivity1.this.ShowDialog(v);
        }
    };
    public Handler mHandler;
    RecyclerView recyclerView;

    private List<MatchList> list = new ArrayList<>();

    private final View.OnClickListener refreshAllMacthtListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (nwUtil.ESPNMatchList.size() == 0) {
                MainActivity1.this.executeURL(2, "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68");
            } else {
                MainActivity1.this.refreshMatchESPN(nwUtil.selectedESPNMatchID);
            }

        }
    };

    public static Activity getActivity() {
        return MainActivity;
    }

    /* renamed from: com.live_cric_scores.MainActivity$3 */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity = this;
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Utils.changeBackground(this);
        Utils.setAdsOnTime(this);
//        this.swipeContainer.setColorSchemeResources(17170459, 17170452, 17170456, 17170454);
        if (nwUtil.addOn) {
        } else {
//            ((RelativeLayout) findViewById(R.id.relViewMiniScore)).removeView(findViewById(R.id.myAdView));
        }


        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fillMatchListESPN(nwUtil.ESPNMatchListJASONData);
//
//            fillMiniScoreESPN(nwUtil.ESPNMiniscoreJASONData);
//
//        fillMatchListAndMiniScoreCBZ(nwUtil.CBZMatchListAndMiniscoreJASONData);
//        if (nwUtil.ESPNMatchList.size() == 0) {
//            executeURL(2, "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68", this.tvESPNstatus);
//        }
//        if (nwUtil.CBZMatchList.size() == 0) {
//            executeURL(1, "http://mapps.cricbuzz.com/cbzandroid/2.0/livematches.json", this.tvCBZstatus);
//        }
//        if (Utils.isMatchSelected4ESPN(this)) {
//            refreshMatchESPN(nwUtil.selectedESPNMatchID);
//        }
//        if (Utils.isMatchSelected4CBZ(this)) {
//            refreshMatchCBZ();
//        }
//        setMatchButtonsAndStatus();
        this.mHandler = new Handler();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity1.this, CricInfoDetailScoreActivity.class);
                intent.putExtra("matchid", list.get(position).getMatchid());
//                nwUtil.selectedESPNMatchID = list.get(position).getMatchid();
                startActivity(intent);
                MainActivity1.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

            }
        }));
    }

    /* renamed from: com.live_cric_scores.MainActivity$6 */

    public void fetchTimelineAsync(int page) {
        Log.d(TAG, "fetchTimelineAsync");
    }

    protected void onResume() {
        Log.d(TAG, " : onResume-->startRepeatingTask");
        super.onResume();
        if (nwUtil.auto_refresh) {
            startRepeatingTask();

            return;
        }
    }

    protected void onPause() {
        Log.d(TAG, " : onPause-->stopRepeatingTask");
        stopRepeatingTask();
        super.onPause();
    }

    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, " : onSaveInstanceState");
        super.onSaveInstanceState(outState);

    }

    void restoreState(Bundle savedInstanceState) {

        fillMatchListESPN(nwUtil.ESPNMatchListJASONData);
        if (!nwUtil.ESPNMiniscoreJASONData.isEmpty()) {
        }
        fillMatchListAndMiniScoreCBZ(nwUtil.CBZMatchListAndMiniscoreJASONData);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, " : onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        restoreState(savedInstanceState);
    }

    public void startRepeatingTask() {
        this.mHandler.postDelayed(this.m_Runnable, (long) nwUtil.refresh_rate);
    }

    public void stopRepeatingTask() {
        this.mHandler.removeCallbacks(this.m_Runnable);
    }



    public void ShowDialog(View v) {
//        Intent i = new Intent(v.getContext(), SettingActivity.class);
//        i.addFlags(DriveFile.MODE_READ_ONLY);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        v.getContext().startActivity(i);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    void executeURL(int Command, String URL) {
        int i = R.string.SEND_ERR;
        Log.d(TAG, "executeURL :" + URL);

            AsyncTask<String, String, String> executeReturn = null;
            switch (Command) {
                case 1:
                    IsMatcheListAndMiniScoreCBZRan = false;
                    executeReturn = new DisplayMatcheListAndMiniScoreCBZ().execute(new String[]{URL});
                    break;
                case 2:
                    IsMatcheListESPNRan = false;
                    IsMiniScoreESPNRan = false;
                    executeReturn = new DisplayMatcheListESPN().execute(new String[]{URL});
                    break;
                case 3:
                    IsMatcheListESPNRan = false;
                    IsMiniScoreESPNRan = false;
                    executeReturn = new DisplayMiniScoreESPN().execute(new String[]{URL});
                    break;
            }
            if (executeReturn != null) {
                i = R.string.SENT_REQUEST;

            return;
        }
        Toast.makeText(getApplicationContext(), "You are not connected !", 0).show();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void fillMatchListAndMiniScoreCBZ(String result) {
        if (result == null) {

        } else if (result.isEmpty()) {
        } else {
            try {
                nwUtil.CBZMatchListAndMiniscoreJASONData = result;


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
                            if (header.getString("mchState").equals("inprogress")) {
                            } else {
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
            }
        }
    }

    private void refreshMatchESPN(String matchId) {
        if (Utils.isMatchSelected4ESPN(this)) {
            executeURL(3, String.format("http://www.espncricinfo.com/ci/engine/match/%s.json?xhr=1", new Object[]{matchId.trim()}));
            return;
        }
        Log.d(TAG, "matchId : " + matchId + "is invalid, disable refresh button ?");
    }

    public void fillMatchListESPN(String result) {
        if (result == null) {
        } else if (result.isEmpty()) {
        } else {
            try {
                nwUtil.ESPNMatchListJASONData = result;
                JSONArray matches = new JSONObject(result).getJSONArray("matches");
                nwUtil.ESPNMatchList.clear();
                nwUtil.ESPNMatchIDList.clear();
                if (matches.length() > 0) {
                    nwUtil.ESPNMatchList.add(getString(R.string.none));
                    nwUtil.ESPNMatchIDList.add(getString(R.string.none));
                    IsMatchListAvailableESPN = true;
                } else if (IsMatchListAvailableESPN) {
                    IsMatchListAvailableESPN = false;
                    Toast.makeText(getApplicationContext(), "No Match available at server 2!", 0).show();
                }
                for (int i = 0; i < matches.length(); i++) {
                    JSONObject series = matches.getJSONObject(i).getJSONObject(matches.getJSONObject(i).names().getString(0));
                    JSONArray data = series.getJSONArray("data");
                    for (int j = 0; j < data.length(); j++) {
                        MatchList matchList = new MatchList();

                        String team1_Sname = series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team1_id")).getString("n");
                        String team2_Sname = series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team2_id")).getString("n");
                        nwUtil.ESPNMatchList.add(team1_Sname + " vs " + series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team2_id")).getString("n"));
                        nwUtil.ESPNMatchIDList.add(data.getJSONObject(j).getString("object_id"));
                        matchList.setTeam1(team1_Sname);
                        matchList.setTeam2(team2_Sname);
                        matchList.setMatchid(data.getJSONObject(j).getString("object_id"));

                        matchList.setVenue(data.getJSONObject(j).getString("description"));
                        matchList.setResult(data.getJSONObject(j).getJSONObject("live").getString("status"));
                        if (data.getJSONObject(j).getJSONArray("innings").length() > 0) {
                            matchList.setScore1(data.getJSONObject(j).getJSONArray("innings").optJSONObject(0).getString("runs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("wickets") + "(" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("overs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(0).getString("scheduled_overs") + ")");
                            matchList.setScore2(data.getJSONObject(j).getJSONArray("innings").optJSONObject(1).getString("runs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("wickets") + "(" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("overs") + "/" + data.getJSONObject(j).getJSONArray("innings").getJSONObject(1).getString("scheduled_overs") + ")");
                        }

                        list.add(matchList);

                    }
                }

                recyclerView.setAdapter(new MatchListAdapter(list));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
            }
        }
    }

//


    /* renamed from: com.live_cric_scores.MainActivity$2 */
    class C04552 implements Runnable {
        C04552() {
        }

        public void run() {
            try {
                MainActivity1.this.stopRepeatingTask();
            } finally {
                MainActivity1.this.mHandler.postDelayed(MainActivity1.this.m_Runnable, (long) nwUtil.refresh_rate);
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
                Log.e(MainActivity.TAG, e.getMessage(), e.getCause());
            }
        }
    }

    public class DisplayMatcheListAndMiniScoreCBZ extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            MainActivity.IsMatcheListAndMiniScoreCBZRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MainActivity1.this.fillMatchListAndMiniScoreCBZ(result);
            MainActivity.IsMatcheListAndMiniScoreCBZRan = true;
            if (MainActivity.IsMatcheListESPNRan || MainActivity.IsMiniScoreESPNRan) {
            }
        }
    }

    public class DisplayMatcheListESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            MainActivity.IsMatcheListESPNRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MainActivity1.this.fillMatchListESPN(result);
            MainActivity.IsMatcheListESPNRan = true;
            if (MainActivity.IsMatcheListAndMiniScoreCBZRan) {
            }
        }
    }

    public class DisplayMiniScoreESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            MainActivity.IsMiniScoreESPNRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MainActivity.IsMiniScoreESPNRan = true;
            if (MainActivity.IsMatcheListAndMiniScoreCBZRan) {
            }
        }
    }

    /* renamed from: com.live_cric_scores.MainActivity$1 */
    class C07131 implements SwipeRefreshLayout.OnRefreshListener {
        C07131() {
        }

        public void onRefresh() {
            MainActivity1.this.fetchTimelineAsync(0);
        }
    }
}
