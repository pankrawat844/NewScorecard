package com.kliff.scorecard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public final class nwUtil {
    public static final long ADONTIMEOUT_SECONDSs = 86400;
    public static long AdsClickedTimeSince1970 = 0;
    public static ArrayList<String> CBZMatchList = new ArrayList();
    public static String CBZMatchListAndMiniscoreJASONData = "";
    static final int ENUM_ACTION_OPEN_IN_MOBILE = 1;
    static final int ENUM_ACTION_UNFOLLOW = 2;
    static final int ENUM_MATCHLIST_ESPN = 2;
    static final int ENUM_MATCHLIST_MINISCORE_CBZ = 1;
    static final int ENUM_MINISCORE_ESPN_FORMAT = 3;
    public static ArrayList<String> ESPNMatchIDList = new ArrayList();
    public static ArrayList<String> ESPNMatchList = new ArrayList();
    public static String ESPNMatchListJASONData = "";
    public static String ESPNMiniscoreJASONData = "";
    static final int REFRESH_RATE_MAX = 300;
    static final int REFRESH_RATE_MIN = 5;
    static final int SECONDS_TO_MILLI = 1000;
    private static final String TAG = "es.nwUtil";
    public static final String URL_DETAILSCORE_CBZ_FORMAT = "http://mapps.cricbuzz.com/cbzandroid/3.0/match/%sscorecard.json";
    public static final String URL_DETAILSCORE_ESPN_FORMAT = "http://api.espncricinfo.com/4/match/%s/scores?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68";
    static final String URL_MATCHLIST_ESPN = "http://api.espncricinfo.com/4/site/mobile_home?&key=c3e20ac4-4ade-4624-8d96-e19beb44ec68";
    static final String URL_MATCHLIST_MINISCORE_CBZ = "http://mapps.cricbuzz.com/cbzandroid/2.0/livematches.json";
    static final String URL_MINISCORE_ESPN_FORMAT = "http://www.espncricinfo.com/ci/engine/match/%s.json?xhr=1";
    public static boolean addOn = true;
    public static boolean auto_refresh = true;
    public static long currentTimeSince1970 = 0;
    public static int refresh_rate;
    public static String selectedCBZMatch = "Select Match 1...";
    public static int selectedCBZMatchID = -1;
    public static String selectedCBZMatchdataPath = "";
    public static String selectedCBZNotiMatch = "Select Match...";
    public static int selectedCBZNotiMatchID = -1;
    public static String selectedCBZNotiMatchState = "";
    public static String selectedESPNMatch = "Select Match 2...";
    public static String selectedESPNMatchID = "-1";
    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String doInBackground(String... args) {
        String out = null;
        try {
            StringBuilder chaine = new StringBuilder("");
            HttpURLConnection connection = (HttpURLConnection) new URL(args[0]).openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while (true) {
                String line = rd.readLine();
                if (line == null) {
                    break;
                }
                chaine.append(line);
            }
            out = chaine.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e.getCause());
        }
        return out;
    }

    public static String loadMatchJason(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e.getCause());
        }
        return text.toString();
    }

    public static String loadMatchJason(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder text = new StringBuilder();
        while (true) {
            try {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                text.append(line);
                text.append('\n');
                br.close();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e.getCause());
            }

        }
        return text.toString();
    }
}
