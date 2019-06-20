package com.kliff.scorecard.fragment;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kliff.scorecard.R;
import com.kliff.scorecard.adapter.ScoreSummaryAdapter;
import com.kliff.scorecard.model.ScoreSummaryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentrayFragment extends Fragment {

    private static final String TAG = "CommentrayFragment";


    private RecyclerView recylerview;
    private ScoreSummaryAdapter summaryAdapter;
    private Handler handler;
    private String match_status;
    private String matchid;
    private String url;
    private List<ScoreSummaryModel> modelList;
    private ContentLoadingProgressBar loading;

    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "isCalling: ");
            if (match_status.equals("dormant")) {
                handler.postDelayed(m_Runnable, (long) 10000);
            } else {
                if (match_status.equals("current")) {
                    commentaryData(url, 0);
                    handler.postDelayed(m_Runnable, 10000);
                } else
                    commentaryData(url, 1);
            }
            Log.e(TAG, "run: " + url);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commentray, container, false);
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
        recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        loading.setVisibility(View.VISIBLE);
        url = "http://api.espncricinfo.com/3/match/" + matchid + "/commentary?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68&innings=1";
        modelList = new ArrayList<>();
        if (match_status.equals("dormant")) {
            loading.setVisibility(View.GONE);
        } else {
            if (match_status.equals("current"))
                commentaryData(url, 0);
            else
                commentaryData(url, 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(this.m_Runnable, 10000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(this.m_Runnable);
    }

    private void initView(View view) {
        recylerview = view.findViewById(R.id.recylerview);
        loading = view.findViewById(R.id.loading);
    }

    private void commentaryData(String url, final int val) {
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new
                Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        loading.setVisibility(View.GONE);
                        try {

                            modelList.clear();
//                            if (match_status.equals("current")) {
//                                for (int j = 0; j < response.length(); j++) {
//                                    ScoreSummaryModel model = new ScoreSummaryModel();
//                                    Log.e(TAG, "ruunig: ");
//                                    JSONObject scoreCardData = response.getJSONObject(j);
//                                    model.setBall_text("" + Html.fromHtml(scoreCardData.getString("ball_text")));
//                                    model.setCms_text("" + Html.fromHtml(scoreCardData.getString("cms_text")));
//                                    model.setFull_commentary("" + Html.fromHtml(scoreCardData.getString("cms_pre_text")));
//                                    modelList.add(model);
//                                }
//                            }
                                for (int j = response.length() - 1; j >= 0; j--) {
                                    ScoreSummaryModel model = new ScoreSummaryModel();
                                    Log.e(TAG, "ruunig: ");
                                    JSONObject scoreCardData = response.getJSONObject(j);
                                    model.setBall_text("" + Html.fromHtml(scoreCardData.getString("ball_text")));
                                    model.setCms_text("" + Html.fromHtml(scoreCardData.getString("cms_text")));
                                    model.setFull_commentary("" + Html.fromHtml(scoreCardData.getString("cms_pre_text")));
                                    modelList.add(model);
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
}
