package com.kliff.scorecard.fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kliff.scorecard.R;
import com.kliff.scorecard.utils.Utils;
import com.kliff.scorecard.utils.cricinfodetailscore;
import com.kliff.scorecard.utils.nwUtil;
import com.kliff.scorecard.utils.tableUtil;

public class CricInfoDetailScoreFragment extends Fragment {
    private static final String TAG = "com.live_cric_scores";
    static CricInfoDetailScoreFragment ourInstance = new CricInfoDetailScoreFragment();
    //private final View.OnClickListener OnBackPress = new C04492();
    private final Runnable m_Runnable = new C04503();
    private final View.OnClickListener refreshAllMacthtListener = new C04514();
    private final SwipeRefreshLayout.OnRefreshListener refreshSwipe = new C07111();
//    ImageButton btnRefresh;
    private TabHost host;
    private Handler mHandler;
    private SwipeRefreshLayout swipeContainer;

    public static CricInfoDetailScoreFragment getInstance() {
        return ourInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailedscore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ourInstance = this;
        Utils.onActivityCreateSetTheme(getActivity());

        Utils.changeBackground(getActivity());
        Utils.setAdsOnTime(getContext());
        this.swipeContainer = view.findViewById(R.id.swipeContainer);
        this.swipeContainer.setOnRefreshListener(this.refreshSwipe);

//        ((TextView) view.findViewById(R.id.ds_tvTitle)).setTypeface(Utils.fontFace);
//        ImageView back = view.findViewById(R.id.ds_ibBack);
        // back.setOnClickListener(this.OnBackPress);
//        back.setImageResource(R.drawable.ic_arrow_back_white_24dp);
//        this.btnRefresh = view.findViewById(R.id.ds_btnRefresh);
//        this.btnRefresh.setOnClickListener(this.refreshAllMacthtListener);
        this.mHandler = new Handler();
        this.host = view.findViewById(R.id.tab_host);
        this.host.setup();
        if (nwUtil.isConnected(getActivity())) {
            refreshMatchESPN(getActivity().getIntent().getStringExtra("matchid"));
            return;
        }
        Toast.makeText(getActivity(), "You are not connected !", Toast.LENGTH_SHORT).show();
        Utils.changeToTheme(getActivity(), 4, false);
    }

    private void fetchTimelineAsync(int page) {
//        this.btnRefresh.performClick();
        this.swipeContainer.setRefreshing(false);
        Log.d(TAG, "fetchTimelineAsync");
    }

    @Override
    public void onResume() {
        Log.d(TAG, " : onResume-->startRepeatingTask");
        super.onResume();
        if (nwUtil.auto_refresh) {
//            this.btnRefresh.setImageResource(R.drawable.ic_refresh_auto_white_36dp);
            startRepeatingTask();
            return;
        }
//        this.btnRefresh.setImageResource(R.drawable.ic_refresh_white_36dp);
    }

    public void onPause() {
        Log.d(TAG, " : onPause-->stopRepeatingTask");
        stopRepeatingTask();
        super.onPause();
    }

    /*public void onBackPressed() {
        //super.onBackPressed();
        finish();
        startActivity(new Intent(ourInstance, BottomNavigation.class));
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }*/

    private void startRepeatingTask() {
        this.m_Runnable.run();
    }

    private void stopRepeatingTask() {
        this.mHandler.removeCallbacks(this.m_Runnable);
    }

    private void refreshMatchESPN(String matchId) {
        if (matchId != null && !matchId.equals("-1")) {
            Log.d(TAG, "onItemSelected :" + String.format(nwUtil.URL_DETAILSCORE_ESPN_FORMAT, matchId));
//            Utils.setAnimation(this.btnRefresh);
            new DisplayMatchesESPN().execute(String.format(nwUtil.URL_DETAILSCORE_ESPN_FORMAT, matchId));
        }
    }

    //renamed from: com.live_cric_scores.CricInfoDetailScoreFragment$2
    /*class C04492 implements View.OnClickListener {
        C04492() {
        }

        public void onClick(View v) {
            CricInfoDetailScoreFragment.this.onBackPressed();
        }
    }*/

    //renamed from: com.live_cric_scores.CricInfoDetailScoreFragment$3
    class C04503 implements Runnable {
        C04503() {
        }

        public void run() {
            try {
                CricInfoDetailScoreFragment.this.stopRepeatingTask();
                if (nwUtil.isConnected(CricInfoDetailScoreFragment.ourInstance.getActivity())) {
                    CricInfoDetailScoreFragment.this.refreshMatchESPN(getActivity().getIntent().getStringExtra("matchid"));
                } else {
                    //Toast.makeText(CricInfoDetailScoreFragment.this.getApplicationContext(), "You are not connected !", 0).show();
                }
                CricInfoDetailScoreFragment.this.mHandler.postDelayed(CricInfoDetailScoreFragment.this.m_Runnable, 3000);
            } catch (Throwable th) {
                CricInfoDetailScoreFragment.this.mHandler.postDelayed(CricInfoDetailScoreFragment.this.m_Runnable, 3000);
            }
        }
    }

    //renamed from: com.live_cric_scores.CricInfoDetailScoreFragment$4
    class C04514 implements View.OnClickListener {
        C04514() {
        }

        public void onClick(View v) {
            CricInfoDetailScoreFragment.this.refreshMatchESPN(getActivity().getIntent().getStringExtra("matchid"));
        }
    }

    public class DisplayMatchesESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(CricInfoDetailScoreFragment.TAG, "DisplayMatchesESPN - result :" + result);
            TableLayout ds_lay_tab_matches = CricInfoDetailScoreFragment.this.getView().findViewById(R.id.ds_lay_tab_matches);
            ds_lay_tab_matches.removeAllViews();
            if (result != null) {
                try {
                    cricinfodetailscore.onPostExecute(result, ds_lay_tab_matches, CricInfoDetailScoreFragment.this.host);
                    for (int i = 0; i < CricInfoDetailScoreFragment.this.host.getTabWidget().getChildCount(); i++) {
                        TextView tv = CricInfoDetailScoreFragment.this.host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                        tv.setTextColor(Utils.MATCH_TITLE_TEXT_COLOR);
                        tv.setTextSize(16.0f);
                        tv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), Utils.fonrString));
                        tv.setGravity(17);
                    }
//                    Utils.clearAnimation(CricInfoDetailScoreFragment.this.btnRefresh);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("com.live_cric_scores:" + getClass().toString(), e.getMessage(), e.getCause());
//                    Utils.clearAnimation(CricInfoDetailScoreFragment.this.btnRefresh);
                    return;
                }
            }
            tableUtil.addNoDataToTable(ds_lay_tab_matches);
//            Utils.clearAnimation(CricInfoDetailScoreFragment.this.btnRefresh);
        }
    }

    //renamed from: com.live_cric_scores.CricInfoDetailScoreFragment$1
    class C07111 implements SwipeRefreshLayout.OnRefreshListener {
        C07111() {
        }

        public void onRefresh() {
            CricInfoDetailScoreFragment.this.fetchTimelineAsync(0);
        }
    }

}
