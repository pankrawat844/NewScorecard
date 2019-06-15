package com.kliff.scorecard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kliff.scorecard.R;
import com.kliff.scorecard.utils.Utils;
import com.kliff.scorecard.utils.nwUtil;
import com.kliff.scorecard.utils.tableUtil;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends Activity {

    private static final String TAG = "com.live_cric_scores";
    public static boolean IsMatchListAvailableESPN = true;
    public static boolean IsMatcheListAndMiniScoreCBZRan = false;
    public static boolean IsMatcheListESPNRan = false;
    public static boolean IsMiniScoreESPNRan = false;
    private static MainActivity MainActivity;
    private final View.OnClickListener OnSelectMatch1 = new C04563();
    private final View.OnClickListener OnSelectMatch2 = new C04596();
    private final View.OnClickListener detailScoreCBZListener = new C04629();
    private final View.OnClickListener detailScoreESPNListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                v.getContext().startActivity(new Intent(v.getContext(), CricInfoDetailScoreActivity.class));
                MainActivity.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } catch (Exception e) {
                Log.e(MainActivity.TAG, e.getMessage(), e.getCause());
            }
        }
    };
    private final Runnable m_Runnable = new C04552();
    private final View.OnClickListener refreshSetting = new View.OnClickListener() {
        public void onClick(View v) {
            MainActivity.this.ShowDialog(v);
        }
    };
    private final SwipeRefreshLayout.OnRefreshListener refreshSwipe = new C07131();
    public Handler mHandler;
    ImageButton btnRefresh;
    private ImageButton btnDetailScoreCBZ;
    private ImageButton btnDetailScoreESPN;
    private Button btnSelectMatch1;
    private Button btnSelectMatch2;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvCBZSpinner;
    private TextView tvCBZstatus;
    private TextView tvESPNSpinner;
    private TextView tvESPNstatus;
    private final View.OnClickListener refreshAllMacthtListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (nwUtil.ESPNMatchList.size() == 0) {
                MainActivity.this.executeURL(2, "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68", MainActivity.this.tvESPNstatus);
            } else {
                MainActivity.this.refreshMatchESPN(nwUtil.selectedESPNMatchID);
            }
            if (nwUtil.CBZMatchList.size() == 0) {
                MainActivity.this.executeURL(1, "http://mapps.cricbuzz.com/cbzandroid/2.0/livematches.json", MainActivity.this.tvCBZstatus);
            } else {
                MainActivity.this.refreshMatchCBZ();
            }
        }
    };

    public static Activity getActivity() {
        return MainActivity;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity = this;
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Utils.changeBackground(this);
        Utils.setAdsOnTime(this);
        this.swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        this.swipeContainer.setOnRefreshListener(this.refreshSwipe);
//        this.swipeContainer.setColorSchemeResources(17170459, 17170452, 17170456, 17170454);
        if (nwUtil.addOn) {
        } else {
//            ((RelativeLayout) findViewById(R.id.relViewMiniScore)).removeView(findViewById(R.id.myAdView));
        }


        Utils.setViewEnabled(this.tvESPNstatus, false);
        Utils.setViewEnabled(this.tvCBZstatus, false);
        this.tvESPNSpinner.setTextColor(Utils.MATCH_TITLE_TEXT_COLOR);
        this.tvESPNSpinner.setBackgroundColor(Utils.MATCH_TITLE_BG_COLOR);
        this.tvCBZSpinner.setTextColor(Utils.MATCH_TITLE_TEXT_COLOR);
        this.tvCBZSpinner.setBackgroundColor(Utils.MATCH_TITLE_BG_COLOR);

        this.btnRefresh.setOnClickListener(this.refreshAllMacthtListener);


        this.btnSelectMatch1.setOnClickListener(this.OnSelectMatch1);
        this.btnSelectMatch1.setText(nwUtil.selectedCBZMatch);

        this.btnSelectMatch2.setOnClickListener(this.OnSelectMatch2);
        this.btnSelectMatch2.setText(nwUtil.selectedESPNMatch);

        this.btnDetailScoreCBZ.setOnClickListener(this.detailScoreCBZListener);

        this.btnDetailScoreESPN.setOnClickListener(this.detailScoreESPNListener);
        fillMatchListESPN(nwUtil.ESPNMatchListJASONData);
        if (!nwUtil.ESPNMiniscoreJASONData.isEmpty()) {
            fillMiniScoreESPN(nwUtil.ESPNMiniscoreJASONData);
        }
        fillMatchListAndMiniScoreCBZ(nwUtil.CBZMatchListAndMiniscoreJASONData);
        if (nwUtil.ESPNMatchList.size() == 0) {
            executeURL(2, "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68", this.tvESPNstatus);
        }
        if (nwUtil.CBZMatchList.size() == 0) {
            executeURL(1, "http://mapps.cricbuzz.com/cbzandroid/2.0/livematches.json", this.tvCBZstatus);
        }
        if (Utils.isMatchSelected4ESPN(this)) {
            refreshMatchESPN(nwUtil.selectedESPNMatchID);
        }
        if (Utils.isMatchSelected4CBZ(this)) {
            refreshMatchCBZ();
        }
        setMatchButtonsAndStatus();
        this.mHandler = new Handler();
    }

    public void fetchTimelineAsync(int page) {
        this.btnRefresh.performClick();
        this.swipeContainer.setRefreshing(false);
        Log.d(TAG, "fetchTimelineAsync");
    }

    protected void onResume() {
        Log.d(TAG, " : onResume-->startRepeatingTask");
        super.onResume();
        if (nwUtil.auto_refresh) {
            startRepeatingTask();
            this.btnRefresh.setImageResource(R.drawable.ic_refresh_white_36dp);
            return;
        }
        this.btnRefresh.setImageResource(R.drawable.ic_refresh_white_36dp);
    }

    protected void onPause() {
        Log.d(TAG, " : onPause-->stopRepeatingTask");
        stopRepeatingTask();
        super.onPause();
    }

    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, " : onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putCharSequence("tvESPNstatus", this.tvESPNstatus.getText());
        outState.putCharSequence("tvCBZstatus", this.tvCBZstatus.getText());
    }

    void restoreState(Bundle savedInstanceState) {
        this.tvESPNstatus.setText(savedInstanceState.getCharSequence("tvESPNstatus"));
        this.tvCBZstatus.setText(savedInstanceState.getCharSequence("tvCBZstatus"));
        fillMatchListESPN(nwUtil.ESPNMatchListJASONData);
        if (!nwUtil.ESPNMiniscoreJASONData.isEmpty()) {
            fillMiniScoreESPN(nwUtil.ESPNMiniscoreJASONData);
        }
        fillMatchListAndMiniScoreCBZ(nwUtil.CBZMatchListAndMiniscoreJASONData);
        setMatchButtonsAndStatus();
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

    private void setMatchButtonsAndStatus() {
        if (nwUtil.CBZMatchList.size() <= 1) {
            Utils.setViewEnabled(this.tvCBZSpinner, false);
            Utils.setViewEnabled(this.btnSelectMatch1, false);
            Utils.setViewEnabled(this.btnDetailScoreCBZ, false);
        } else {
            Utils.setViewEnabled(this.tvCBZSpinner, true);
            Utils.setViewEnabled(this.btnSelectMatch1, true);
            Utils.setViewEnabled(this.btnDetailScoreCBZ, Utils.isMatchSelected4CBZ(this));
        }
        if (nwUtil.ESPNMatchList.size() <= 1) {
            Utils.setViewEnabled(this.tvESPNSpinner, false);
            Utils.setViewEnabled(this.btnSelectMatch2, false);
            Utils.setViewEnabled(this.btnDetailScoreESPN, false);
            return;
        }
        Utils.setViewEnabled(this.tvESPNSpinner, true);
        Utils.setViewEnabled(this.btnSelectMatch2, true);
        Utils.setViewEnabled(this.btnDetailScoreESPN, Utils.isMatchSelected4ESPN(this));
    }

    private void ClearTable4CBZMatch(String selectedMatch, int which) {
        if (!selectedMatch.equals(nwUtil.selectedCBZMatch)) {
            nwUtil.selectedCBZMatch = selectedMatch;
            nwUtil.selectedCBZMatchID = which;
            Utils.setSettingValue(getApplicationContext());
            this.btnSelectMatch1.setText(nwUtil.selectedCBZMatch);

            refreshMatchCBZ();
            setMatchButtonsAndStatus();
        }
    }

    private void ClearTable4ESPNMatch(String selectedMatch, int which) {
        if (!selectedMatch.equals(nwUtil.selectedESPNMatch)) {
            nwUtil.selectedESPNMatch = selectedMatch;
            nwUtil.selectedESPNMatchID = (String) nwUtil.ESPNMatchIDList.get(which);
            Utils.setSettingValue(getApplicationContext());
            this.btnSelectMatch2.setText(nwUtil.selectedESPNMatch);

            refreshMatchESPN(nwUtil.selectedESPNMatchID);
            setMatchButtonsAndStatus();
        }
    }

    int indexOfSelectedCBZMatch() {
        for (int i = 0; i < nwUtil.CBZMatchList.size(); i++) {
            if (((String) nwUtil.CBZMatchList.get(i)).contains(nwUtil.selectedCBZMatch)) {
                return i;
            }
        }
        return 0;
    }

    private void selectMatch1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectmatch1);
        final CharSequence[] matchList1 = (CharSequence[]) nwUtil.CBZMatchList.toArray(new CharSequence[nwUtil.CBZMatchList.size()]);
        builder.setSingleChoiceItems(matchList1, indexOfSelectedCBZMatch(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.ClearTable4CBZMatch(matchList1[which].toString().split("\\(")[0].trim(), which);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new C04585());
        builder.create().show();
    }

    private void selectMatch2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectmatch2);
        final CharSequence[] matchList2 = (CharSequence[]) nwUtil.ESPNMatchList.toArray(new CharSequence[nwUtil.ESPNMatchList.size()]);
        builder.setSingleChoiceItems(matchList2, nwUtil.ESPNMatchIDList.indexOf(nwUtil.selectedESPNMatchID), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.ClearTable4ESPNMatch(matchList2[which].toString(), which);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new C04618());
        builder.create().show();
    }

    public void ShowDialog(View v) {
//        Intent i = new Intent(v.getContext(), SettingActivity.class);
//        i.addFlags(DriveFile.MODE_READ_ONLY);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        v.getContext().startActivity(i);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    void executeURL(int Command, String URL, TextView tvStatus) {
        int i = R.string.SEND_ERR;
        Log.d(TAG, "executeURL :" + URL);
        if (nwUtil.isConnected(tvStatus.getContext())) {
            tvStatus.setText(tableUtil.fromHtml(getString(R.string.SEND_REQUEST)));
            Utils.setAnimation(this.btnRefresh);
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
            }
            tvStatus.setText(tableUtil.fromHtml(getString(i)));
            return;
        }
        Toast.makeText(getApplicationContext(), "You are not connected !", 0).show();
        tvStatus.setText(tableUtil.fromHtml(getString(R.string.SEND_ERR)));
    }

    private void refreshMatchCBZ() {
        if (Utils.isMatchSelected4CBZ(this)) {
            executeURL(1, "http://mapps.cricbuzz.com/cbzandroid/2.0/livematches.json", this.tvCBZstatus);
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void fillMatchListAndMiniScoreCBZ(String result) {
        if (result == null) {
            this.tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        } else if (result.isEmpty()) {
            this.tvCBZstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
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
                            if (header.getString("mchState").equals("inprogress")) {
                            } else {
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
        if (Utils.isMatchSelected4ESPN(this)) {
            executeURL(3, String.format("http://www.espncricinfo.com/ci/engine/match/%s.json?xhr=1", new Object[]{matchId.trim()}), this.tvESPNstatus);
            return;
        }
        Log.d(TAG, "matchId : " + matchId + "is invalid, disable refresh button ?");
    }

    public void fillMatchListESPN(String result) {
        if (result == null) {
            this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        } else if (result.isEmpty()) {
            this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        } else {
            try {
                nwUtil.ESPNMatchListJASONData = result;
                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_RESPONSE)));
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
                        String team1_Sname = series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team1_id")).getString("n");
                        nwUtil.ESPNMatchList.add(team1_Sname + " vs " + series.getJSONObject("team").getJSONObject(data.getJSONObject(j).getJSONObject("match").getString("team2_id")).getString("n"));
                        nwUtil.ESPNMatchIDList.add(data.getJSONObject(j).getString("object_id"));
                    }
                }
                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GOT_RESPONSE)));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
            }
        }
    }

    public void fillMiniScoreESPN(String result) {
        Log.d(TAG, "fillMiniScoreESPN - result :" + result);
        if (result == null) {
            this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        } else if (result.isEmpty()) {
            this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
        } else {
            nwUtil.ESPNMiniscoreJASONData = result;
            try {
                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_RESPONSE)));

                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GOT_RESPONSE)));
            } catch (Exception e) {
                Log.e("com.:" + getClass().toString(), e.getMessage(), e.getCause());
                this.tvESPNstatus.setText(tableUtil.fromHtml(getString(R.string.GET_ERR)));
            }
        }
    }

    /* renamed from: com.live_cric_scores.MainActivity$2 */
    class C04552 implements Runnable {
        C04552() {
        }

        public void run() {
            try {
                MainActivity.this.stopRepeatingTask();
                MainActivity.this.btnRefresh.performClick();
            } finally {
                MainActivity.this.mHandler.postDelayed(MainActivity.this.m_Runnable, (long) nwUtil.refresh_rate);
            }
        }
    }

    /* renamed from: com.live_cric_scores.MainActivity$3 */
    class C04563 implements View.OnClickListener {
        C04563() {
        }

        public void onClick(View v) {
            MainActivity.this.selectMatch1();
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

    /* renamed from: com.live_cric_scores.MainActivity$6 */
    class C04596 implements View.OnClickListener {
        C04596() {
        }

        public void onClick(View v) {
            MainActivity.this.selectMatch2();
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
            MainActivity.this.fillMatchListAndMiniScoreCBZ(result);
            MainActivity.IsMatcheListAndMiniScoreCBZRan = true;
            if (MainActivity.IsMatcheListESPNRan || MainActivity.IsMiniScoreESPNRan) {
                Utils.clearAnimation(MainActivity.this.btnRefresh);
            }
            MainActivity.this.setMatchButtonsAndStatus();
        }
    }

    public class DisplayMatcheListESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            MainActivity.IsMatcheListESPNRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MainActivity.this.fillMatchListESPN(result);
            MainActivity.IsMatcheListESPNRan = true;
            if (MainActivity.IsMatcheListAndMiniScoreCBZRan) {
                Utils.clearAnimation(MainActivity.this.btnRefresh);
            }
            MainActivity.this.setMatchButtonsAndStatus();
        }
    }

    public class DisplayMiniScoreESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            MainActivity.IsMiniScoreESPNRan = false;
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MainActivity.this.fillMiniScoreESPN(result);
            MainActivity.IsMiniScoreESPNRan = true;
            if (MainActivity.IsMatcheListAndMiniScoreCBZRan) {
                Utils.clearAnimation(MainActivity.this.btnRefresh);
            }
        }
    }

    /* renamed from: com.live_cric_scores.MainActivity$1 */
    class C07131 implements SwipeRefreshLayout.OnRefreshListener {
        C07131() {
        }

        public void onRefresh() {
            MainActivity.this.fetchTimelineAsync(0);
        }
    }
}
