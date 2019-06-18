package com.kliff.scorecard.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.kliff.scorecard.utils.ScoreSummaryUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchSummaryFragment extends Fragment {

    private RequestQueue queue;
    private String url;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ScoreSummaryUtil.init(view);
        queue = Volley.newRequestQueue(getActivity());
        url = "http://api.espncricinfo.com/4/match/1144505/scores?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68";

        scoreData(url);
    }

    private void scoreData(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new
                Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("commentary");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
