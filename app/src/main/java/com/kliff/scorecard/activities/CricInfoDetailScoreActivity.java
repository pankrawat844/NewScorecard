package com.kliff.scorecard.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kliff.scorecard.R;
import com.kliff.scorecard.utils.Utils;
import com.kliff.scorecard.utils.cricinfodetailscore;
import com.kliff.scorecard.utils.nwUtil;
import com.kliff.scorecard.utils.tableUtil;

public class CricInfoDetailScoreActivity extends Activity {
    private static final String TAG = "com.live_cric_scores";
    static CricInfoDetailScoreActivity ourInstance = new CricInfoDetailScoreActivity();
   /* private final OnClickListener OnBackPress = new C04492();
    private final Runnable m_Runnable = new C04503();
    private final OnClickListener refreshAllMacthtListener = new C04514();
    private final SwipeRefreshLayout.OnRefreshListener refreshSwipe = new C07111();*/
    public TabHost host;
    public Handler mHandler;
    ImageButton btnRefresh;
    private SwipeRefreshLayout swipeContainer;

    public static CricInfoDetailScoreActivity getInstance() {
        return ourInstance;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ourInstance = this;
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.score_details);
       /* Utils.changeBackground(this);
        Utils.setAdsOnTime(this);
        this.swipeContainer = findViewById(R.id.swipeContainer);
        this.swipeContainer.setOnRefreshListener(this.refreshSwipe);

        ((TextView) findViewById(R.id.ds_tvTitle)).setTypeface(Utils.fontFace);
        ImageView back = findViewById(R.id.ds_ibBack);
        back.setOnClickListener(this.OnBackPress);
        back.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        this.btnRefresh = findViewById(R.id.ds_btnRefresh);
        this.btnRefresh.setOnClickListener(this.refreshAllMacthtListener);
        this.mHandler = new Handler();
        this.host = findViewById(R.id.tab_host);
        this.host.setup();
        if (nwUtil.isConnected(getApplicationContext())) {
            refreshMatchESPN(getIntent().getStringExtra("matchid"));
            return;
        }
        Toast.makeText(getApplicationContext(), "You are not connected !", Toast.LENGTH_SHORT).show();
        Utils.changeToTheme(this, 4, false);*/
    }
/*
    public void fetchTimelineAsync(int page) {
        this.btnRefresh.performClick();
        this.swipeContainer.setRefreshing(false);
        Log.d(TAG, "fetchTimelineAsync");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, " : onResume-->startRepeatingTask");
        super.onResume();
        if (nwUtil.auto_refresh) {
            this.btnRefresh.setImageResource(R.drawable.ic_refresh_auto_white_36dp);
            startRepeatingTask();
            return;
        }
        this.btnRefresh.setImageResource(R.drawable.ic_refresh_white_36dp);
    }

    protected void onPause() {
        Log.d(TAG, " : onPause-->stopRepeatingTask");
        stopRepeatingTask();
        super.onPause();
    }

    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        startActivity(new Intent(CricInfoDetailScoreActivity.this, BottomNavigation.class));
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void startRepeatingTask() {
        this.m_Runnable.run();
    }

    public void stopRepeatingTask() {
        this.mHandler.removeCallbacks(this.m_Runnable);
    }

    private void refreshMatchESPN(String matchId) {
        if (matchId != null && !matchId.equals("-1")) {
            Log.d(TAG, "onItemSelected :" + String.format(nwUtil.URL_DETAILSCORE_ESPN_FORMAT, matchId));
            Utils.setAnimation(this.btnRefresh);
            new DisplayMatchesESPN().execute(String.format(nwUtil.URL_DETAILSCORE_ESPN_FORMAT, matchId));
        }
    }

    *//* renamed from: com.live_cric_scores.CricInfoDetailScoreActivity$2 *//*
    class C04492 implements OnClickListener {
        C04492() {
        }

        public void onClick(View v) {
            CricInfoDetailScoreActivity.this.onBackPressed();
        }
    }

    *//* renamed from: com.live_cric_scores.CricInfoDetailScoreActivity$3 *//*
    class C04503 implements Runnable {
        C04503() {
        }

        public void run() {
            try {
                CricInfoDetailScoreActivity.this.stopRepeatingTask();
                if (nwUtil.isConnected(CricInfoDetailScoreActivity.this.getApplicationContext())) {
                    CricInfoDetailScoreActivity.this.refreshMatchESPN(getIntent().getStringExtra("matchid"));
                } else {
                    //Toast.makeText(CricInfoDetailScoreActivity.this.getApplicationContext(), "You are not connected !", 0).show();
                }
                CricInfoDetailScoreActivity.this.mHandler.postDelayed(CricInfoDetailScoreActivity.this.m_Runnable, 3000);
            } catch (Throwable th) {
                CricInfoDetailScoreActivity.this.mHandler.postDelayed(CricInfoDetailScoreActivity.this.m_Runnable, 3000);
            }
        }
    }

    *//* renamed from: com.live_cric_scores.CricInfoDetailScoreActivity$4 *//*
    class C04514 implements OnClickListener {
        C04514() {
        }

        public void onClick(View v) {
            CricInfoDetailScoreActivity.this.refreshMatchESPN(getIntent().getStringExtra("matchid"));
        }
    }

    public class DisplayMatchesESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(CricInfoDetailScoreActivity.TAG, "DisplayMatchesESPN - result :" + result);
            TableLayout ds_lay_tab_matches = CricInfoDetailScoreActivity.this.findViewById(R.id.ds_lay_tab_matches);
            ds_lay_tab_matches.removeAllViews();
            if (result != null) {
                try {
                    cricinfodetailscore.onPostExecute(result, ds_lay_tab_matches, CricInfoDetailScoreActivity.this.host);
                    for (int i = 0; i < CricInfoDetailScoreActivity.this.host.getTabWidget().getChildCount(); i++) {
                        TextView tv = CricInfoDetailScoreActivity.this.host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                        tv.setTextColor(Utils.MATCH_TITLE_TEXT_COLOR);
                        tv.setTextSize(16.0f);
                        tv.setTypeface(Typeface.createFromAsset(CricInfoDetailScoreActivity.this.getAssets(), Utils.fonrString));
                        tv.setGravity(17);
                    }
                    Utils.clearAnimation(CricInfoDetailScoreActivity.this.btnRefresh);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
                    Utils.clearAnimation(CricInfoDetailScoreActivity.this.btnRefresh);
                    return;
                }
            }
            tableUtil.addNoDataToTable(ds_lay_tab_matches);
            Utils.clearAnimation(CricInfoDetailScoreActivity.this.btnRefresh);
        }
    }

    *//* renamed from: com.live_cric_scores.CricInfoDetailScoreActivity$1 *//*
    class C07111 implements SwipeRefreshLayout.OnRefreshListener {
        C07111() {
        }

        public void onRefresh() {
            CricInfoDetailScoreActivity.this.fetchTimelineAsync(0);
        }
    }*/

}
