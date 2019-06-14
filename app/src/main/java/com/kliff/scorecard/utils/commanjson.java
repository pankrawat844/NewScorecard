package com.kliff.scorecard.utils;

import android.widget.TableLayout;

import com.kliff.scorecard.utils.tableUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public final class commanjson {
    public static final int ADD_ID = 310460;
    public static final String ADD_KEY = "1463912867287087788";
    private static final String TAG = "com.live_cric_scores.tableUtil";

    private commanjson() {
    }

    public static String getPlayerName(JSONObject jsonObj, int playerId, boolean isBatsman) throws Exception {
        String playerName = "-";
        JSONArray jsnArrayTeam = jsonObj.getJSONArray("team");
        for (int j = 0; j < jsnArrayTeam.length(); j++) {
            JSONArray players = jsnArrayTeam.getJSONObject(j).optJSONArray("player");
            if (players != null) {
                boolean foundPlayer = false;
                int k = 0;
                while (k < players.length()) {
                    if (players.getJSONObject(k).getInt("player_id") == playerId) {
                        if (isBatsman) {
                            playerName = String.format("%s(%s)", new Object[]{players.getJSONObject(k).getString("card_long"), players.getJSONObject(k).getString("batting_style")});
                        } else {
                            playerName = String.format("%s(%s)", new Object[]{players.getJSONObject(k).getString("card_long"), players.getJSONObject(k).getString("bowling_style")});
                        }
                        foundPlayer = true;
                        if (foundPlayer) {
                            break;
                        }
                    } else {
                        k++;
                    }
                }
                if (foundPlayer) {
                    break;
                }
            }
        }
        return playerName;
    }

    public static String getRecentOvers(JSONObject jsonObj) throws Exception {
        JSONArray overArray = jsonObj.getJSONObject("live").getJSONArray("recent_overs");
        String ball = "";
        for (int i = 0; i < overArray.length(); i++) {
            JSONArray overs = overArray.getJSONArray(i);
            for (int j = 0; j < overs.length(); j++) {
                String extra = overs.getJSONObject(j).getString("extras");
                if (!(extra == null || extra.trim().equals(""))) {
                    extra = "(" + extra + ")";
                }
                ball = ball + overs.getJSONObject(j).getString("ball") + extra;
                if (j != overs.length() - 1) {
                    ball = ball + " ";
                }
            }
            if (i != overArray.length() - 1) {
                ball = ball + "|";
            }
        }
        return String.format("%s%s", new Object[]{"Recent overs : ", tableUtil.fromHtml(ball)});
    }

    public static void addBatsmanCBZ(TableLayout lay_tab_match, JSONObject batsman, boolean striker) throws Exception {
        if (batsman != null) {
            String stikerSr = "-";
            if (batsman.optDouble("balls", 0.0d) != 0.0d) {
                stikerSr = String.format("%.2f", new Object[]{Double.valueOf((batsman.optDouble("runs") / batsman.optDouble("balls")) * 100.0d)});
            }
            tableUtil.addBatsmanInfoToTable(lay_tab_match, batsman.getString("fullName") + (striker ? "*" : ""), batsman.getString("runs"), batsman.getString("balls"), batsman.getString("fours"), batsman.getString("sixes"), stikerSr);
        }
    }

    public static void addBowlerCBZ(TableLayout lay_tab_match, JSONObject bowler, boolean striker) throws Exception {
        if (bowler != null) {
            String eCon = "-";
            if (bowler.optDouble("overs", 0.0d) != 0.0d) {
                double oversValue = bowler.optDouble("overs");
                double fractionalPart = oversValue % 1.0d;
                Object[] objArr = new Object[1];
                objArr[0] = Double.valueOf(bowler.optDouble("runs") / ((oversValue - fractionalPart) + (1.666d * fractionalPart)));
                eCon = String.format("%.2f", objArr);
            }
            tableUtil.addBowlerInfoToTable(lay_tab_match, bowler.getString("fullName") + (striker ? "*" : ""), bowler.getString("overs"), bowler.getString("maidens"), bowler.getString("runs"), bowler.getString("wicket"), eCon);
        }
    }

    public static void addBatsmanESPN(TableLayout lay_tab_match, JSONObject batsman, JSONObject reply) throws Exception {
        if (batsman != null) {
            String strikerStr = "";
            if (batsman.optInt("live_current") == 1) {
                strikerStr = "*";
            }
            tableUtil.addBatsmanInfoToTable(lay_tab_match, getPlayerName(reply, batsman.getInt("player_id"), true) + strikerStr, batsman.getString("runs"), batsman.getString("balls_faced"), batsman.getString("fours"), batsman.getString("sixes"), batsman.getString("strike_rate"));
        }
    }

    public static void addBowlerESPN(TableLayout lay_tab_match, JSONObject bowler, JSONObject reply) throws Exception {
        if (bowler != null) {
            String strikerStr = "";
            if (bowler.optInt("live_current") == 1) {
                strikerStr = "*";
            }
            tableUtil.addBowlerInfoToTable(lay_tab_match, getPlayerName(reply, bowler.getInt("player_id"), false) + strikerStr, bowler.getString("overs"), bowler.getString("maidens"), bowler.getString("conceded"), bowler.getString("wickets"), bowler.getString("economy_rate"));
        }
    }
}
