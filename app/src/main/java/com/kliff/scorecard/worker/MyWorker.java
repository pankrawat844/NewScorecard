package com.kliff.scorecard.worker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kliff.scorecard.utils.nwUtil;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    private String result;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        }

    @NonNull
    @Override
    public Result doWork() {
//        refreshMatchESPN(getInputData().getString("matchid"));
        Log.e("called","worker called");
        Data data= new Data.Builder().putString("result",refreshMatchESPN(getInputData().getString("matchid"))).build();
        return Result.success(data);
    }


    private String refreshMatchESPN(String matchId) {

        if (matchId != null && !matchId.equals("-1")) {
            Log.d("Worker", "onItemSelected1 :" + String.format(nwUtil.URL_DETAILSCORE_ESPN_FORMAT, new Object[]{matchId.trim()}));

            if (new DisplayMatchesESPN().execute(new String[]{String.format(nwUtil.URL_DETAILSCORE_ESPN_FORMAT, new Object[]{matchId.trim()})}) != null) {
//                this.ds_tvStatus.setText(tableUtil.fromHtml(getString(R.string.SENT_REQUEST)));
//            } else {
            }
        }

        StringRequest stringRequest= new StringRequest(Request.Method.GET, "http://api.espncricinfo.com/4/match/" + matchId + "/scores?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result="";
            }
        });
        return result;
    }

    public class DisplayMatchesESPN extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            return nwUtil.doInBackground(args);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Data data = new Data.Builder()
                    .putString("result", result)
                    .build();


            Log.d("Worker", "DisplayMatchesESPN - result1 :" + result);

        }
    }


}


