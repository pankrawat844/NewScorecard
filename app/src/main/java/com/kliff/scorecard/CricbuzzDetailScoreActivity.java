package com.kliff.scorecard;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CricbuzzDetailScoreActivity extends Activity {
    private static final String TAG = "com.live_cric_scores";
    private static CricbuzzDetailScoreActivity ourInstance = new CricbuzzDetailScoreActivity();
    private final OnClickListener OnBackPress = new C04522();
    private ImageButton btnRefresh;
    private TextView ds_tvStatus;
    public TabHost host;
    public Handler mHandler;
    private final Runnable m_Runnable = new C04533();
    private final OnClickListener refreshAllMacthtListener = new C04544();
    private final SwipeRefreshLayout.OnRefreshListener refreshSwipe = new C07121();
    private SwipeRefreshLayout swipeContainer;

    /* renamed from: com.live_cric_scores.CricbuzzDetailScoreActivity$2 */
    class C04522 implements OnClickListener {
        C04522() {
        }

        public void onClick(View v) {
            CricbuzzDetailScoreActivity.this.onBackPressed();
        }
    }

    /* renamed from: com.live_cric_scores.CricbuzzDetailScoreActivity$3 */
    class C04533 implements Runnable {
        C04533() {
        }

        public void run() {
            try {
                CricbuzzDetailScoreActivity.this.stopRepeatingTask();
                if (nwUtil.isConnected(CricbuzzDetailScoreActivity.this.getApplicationContext())) {
                    CricbuzzDetailScoreActivity.this.refreshMatchCBZ(nwUtil.selectedCBZMatchdataPath);
                } else {
                    CricbuzzDetailScoreActivity.this.ds_tvStatus.setText(tableUtil.fromHtml(CricbuzzDetailScoreActivity.this.getString(R.string.SEND_ERR)));
                    Toast.makeText(CricbuzzDetailScoreActivity.this.getApplicationContext(), "You are not connected !", 0).show();
                }
                CricbuzzDetailScoreActivity.this.mHandler.postDelayed(CricbuzzDetailScoreActivity.this.m_Runnable, (long) nwUtil.refresh_rate);
            } catch (Throwable th) {
                CricbuzzDetailScoreActivity.this.mHandler.postDelayed(CricbuzzDetailScoreActivity.this.m_Runnable, (long) nwUtil.refresh_rate);
            }
        }
    }

    /* renamed from: com.live_cric_scores.CricbuzzDetailScoreActivity$4 */
    class C04544 implements OnClickListener {
        C04544() {
        }

        public void onClick(View v) {
            CricbuzzDetailScoreActivity.this.refreshMatchCBZ(nwUtil.selectedCBZMatchdataPath);
        }
    }

    public class DisplayMatchesCBZ extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(CricbuzzDetailScoreActivity.TAG, "DisplayMatchesCBZ - result :" + result);
            TableLayout ds_lay_tab_matches = (TableLayout) CricbuzzDetailScoreActivity.this.findViewById(R.id.ds_lay_tab_matches);
            ds_lay_tab_matches.removeAllViews();
            if (result == null || result.equals("{}")) {
                tableUtil.addNoDataToTable(ds_lay_tab_matches);
                CricbuzzDetailScoreActivity.this.ds_tvStatus.setText(tableUtil.fromHtml(CricbuzzDetailScoreActivity.this.getString(R.string.GET_ERR)));
                Utils.clearAnimation(CricbuzzDetailScoreActivity.this.btnRefresh);
                return;
            }
            try {
                CricbuzzDetailScoreActivity.this.ds_tvStatus.setText(tableUtil.fromHtml(CricbuzzDetailScoreActivity.this.getString(R.string.GET_RESPONSE)));
                cbzdetailscore.onPostExecute(result, ds_lay_tab_matches, CricbuzzDetailScoreActivity.this.host);
                for (int i = 0; i < CricbuzzDetailScoreActivity.this.host.getTabWidget().getChildCount(); i++) {
                    TextView tv = (TextView) CricbuzzDetailScoreActivity.this.host.getTabWidget().getChildAt(i).findViewById(R.id.score1);
                    tv.setTextColor(Utils.MATCH_TITLE_TEXT_COLOR);
                    tv.setTextSize(16.0f);
                    tv.setTypeface(Typeface.createFromAsset(CricbuzzDetailScoreActivity.this.getAssets(), Utils.fonrString));
                    tv.setGravity(17);
                }
                CricbuzzDetailScoreActivity.this.ds_tvStatus.setText(tableUtil.fromHtml(CricbuzzDetailScoreActivity.this.getString(R.string.GOT_RESPONSE)));
                Utils.clearAnimation(CricbuzzDetailScoreActivity.this.btnRefresh);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
                CricbuzzDetailScoreActivity.this.ds_tvStatus.setText(tableUtil.fromHtml(CricbuzzDetailScoreActivity.this.getString(R.string.GET_ERR)));
                Utils.clearAnimation(CricbuzzDetailScoreActivity.this.btnRefresh);
            }
        }
    }

    /* renamed from: com.live_cric_scores.CricbuzzDetailScoreActivity$1 */
    class C07121 implements SwipeRefreshLayout.OnRefreshListener {
        C07121() {
        }

        public void onRefresh() {
            CricbuzzDetailScoreActivity.this.fetchTimelineAsync(0);
        }
    }

    public static CricbuzzDetailScoreActivity getInstance() {
        return ourInstance;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ourInstance = this;
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.detailedscore_activity);
        Utils.changeBackground(this);
        Utils.setAdsOnTime(this);
        this.swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        this.swipeContainer.setOnRefreshListener(this.refreshSwipe);
//        this.swipeContainer.setColorSchemeResources(17170459, 17170452, 17170456, 17170454);
        if (nwUtil.addOn) {
//            ((AdView) findViewById(R.id.myAdView)).loadAd(new Builder().build());
        } else {
//            ((RelativeLayout) findViewById(R.id.relViewDetScore)).removeView(findViewById(R.id.myAdView));
        }
        ((TextView) findViewById(R.id.ds_tvTitle)).setTypeface(Utils.fontFace);
        findViewById(R.id.ds_ibBack).setOnClickListener(this.OnBackPress);
        this.btnRefresh = (ImageButton) findViewById(R.id.ds_btnRefresh);
        this.btnRefresh.setOnClickListener(this.refreshAllMacthtListener);
        this.ds_tvStatus = (TextView) findViewById(R.id.ds_tvStatus);
        Utils.setViewEnabled(this.ds_tvStatus, false);
        this.mHandler = new Handler();
        this.host = (TabHost) findViewById(R.id.tab_host);
        this.host.setup();
        if (nwUtil.isConnected(getApplicationContext())) {
            refreshMatchCBZ(nwUtil.selectedCBZMatchdataPath);
            return;
        }
        this.ds_tvStatus.setText(tableUtil.fromHtml(getString(R.string.SEND_ERR)));
        Toast.makeText(getApplicationContext(), "You are not connected !", 0).show();
    }

    public void fetchTimelineAsync(int page) {
        this.btnRefresh.performClick();
        this.swipeContainer.setRefreshing(false);
        Log.d(TAG, "fetchTimelineAsync");
    }

    protected void onResume() {
        super.onResume();
        if (nwUtil.auto_refresh) {
            startRepeatingTask();
            this.btnRefresh.setImageResource(R.drawable.ic_refresh_auto_white_36dp);
            return;
        }
        this.btnRefresh.setImageResource(R.drawable.ic_refresh_white_36dp);
    }

    protected void onPause() {
        stopRepeatingTask();
        super.onPause();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void startRepeatingTask() {
        this.m_Runnable.run();
    }

    public void stopRepeatingTask() {
        this.mHandler.removeCallbacks(this.m_Runnable);
    }

    private void refreshMatchCBZ(String matchId) {
        if (matchId != null && !matchId.equals("-1")) {
            Log.d(TAG, "onItemSelected :" + String.format(nwUtil.URL_DETAILSCORE_CBZ_FORMAT, new Object[]{matchId.trim()}));
            this.ds_tvStatus.setText(tableUtil.fromHtml(getString(R.string.SEND_REQUEST)));
            Utils.setAnimation(this.btnRefresh);
            if (new DisplayMatchesCBZ().execute(new String[]{matchId}) != null) {
                this.ds_tvStatus.setText(tableUtil.fromHtml(getString(R.string.SENT_REQUEST)));
            } else {
                this.ds_tvStatus.setText(tableUtil.fromHtml(getString(R.string.SEND_ERR)));
            }
        }
    }
}
