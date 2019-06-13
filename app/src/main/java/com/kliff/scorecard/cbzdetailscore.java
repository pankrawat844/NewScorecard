package com.kliff.scorecard;

import android.content.Context;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import com.google.android.gms.games.Games;
import org.json.JSONArray;
import org.json.JSONObject;

public final class cbzdetailscore {
    private static final String TAG = "com.live_cric_scores.cbzdetailscore";

    private cbzdetailscore() {
    }

    protected static void onPostExecute(String result, TableLayout ds_lay_tab_matches) throws Exception {
        JSONObject scorecard = new JSONObject(result);
        tableUtil.addRowToTable(ds_lay_tab_matches, scorecard.getJSONObject("header").getString("status"));
        String mMOM = scorecard.getJSONObject("header").getString("MOM");
        if (mMOM.equals("")) {
            mMOM = "[TBD]";
        }
        tableUtil.addRowToTable(ds_lay_tab_matches, "MOM : " + mMOM);
        JSONObject innings = scorecard.optJSONObject("Innings");
        if (innings == null) {
            tableUtil.addNoDataToTable(ds_lay_tab_matches);
        } else if (innings.length() == 0) {
            tableUtil.addNoDataToTable(ds_lay_tab_matches);
        } else {
            for (int inni = innings.length(); inni > 0; inni--) {
                addInnning(ds_lay_tab_matches, String.valueOf(inni), innings.optJSONObject(String.valueOf(inni)), scorecard.optJSONArray(Games.EXTRA_PLAYER_IDS));
            }
        }
    }

    protected static void onPostExecute(String result, TableLayout ds_lay_tab_matches, TabHost host) throws Exception {
        JSONObject jSONObject = new JSONObject(result);
        JSONObject header = jSONObject.getJSONObject("header");
        tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
        tableUtil.addRowToTable(ds_lay_tab_matches, header.getString("status") + " (" + header.getString("mchState") + ")");
        tableUtil.addRowToTable(ds_lay_tab_matches, header.getString("type") + " at " + header.getString("grnd") + ", " + header.getString("vcity") + ", " + header.getString("vcountry") + ", " + jSONObject.getString("srs"));
        String mMOM = jSONObject.getJSONObject("header").getString("MOM");
        if (!mMOM.equals("")) {
            tableUtil.addRowToTable(ds_lay_tab_matches, "MOM : " + mMOM);
        }
        JSONObject innings = jSONObject.optJSONObject("Innings");
        if (innings == null) {
            tableUtil.addNoDataToTable(ds_lay_tab_matches);
        } else if (innings.length() == 0) {
            tableUtil.addNoDataToTable(ds_lay_tab_matches);
        } else {
            int indexOfTab;
            int currentTableIndex = host.getCurrentTab();
            int tabSpecListsize = Utils.tabSpecList.size();
            host.setCurrentTab(0);
            for (indexOfTab = tabSpecListsize; indexOfTab > 0; indexOfTab--) {
                int tableIndexToBeDeleted = indexOfTab - 1;
                Utils.tabSpecList.remove(tableIndexToBeDeleted);
                host.getTabWidget().removeView(host.getTabWidget().getChildTabViewAt(tableIndexToBeDeleted));
            }
            TabSpec fakeSpec = host.newTabSpec("about").setIndicator("faketab").setContent(CricbuzzDetailScoreActivity.getInstance().getResources().getIdentifier("faketab", "id", CricbuzzDetailScoreActivity.getInstance().getPackageName()));
            host.addTab(fakeSpec);
            Utils.tabSpecList.add(fakeSpec);
            host.getTabWidget().getChildTabViewAt(0).setVisibility(8);
            indexOfTab = 0;
            for (int inni = innings.length(); inni > 0; inni--) {
                JSONObject inning = innings.optJSONObject(String.valueOf(inni));
                if (inning != null) {
                    String inn_number = String.valueOf(inni);
                    int indexOfInn = indexOfTab + 1;
                    String newTabIndex = String.valueOf(indexOfInn + 1);
                    int id = CricbuzzDetailScoreActivity.getInstance().getResources().getIdentifier("tab" + indexOfInn, "id", CricbuzzDetailScoreActivity.getInstance().getPackageName());
                    TableLayout tl = (TableLayout) CricbuzzDetailScoreActivity.getInstance().findViewById(id);
                    tl.removeAllViews();
                    tl.setVisibility(8);
                    TabSpec spec = host.newTabSpec("INN " + newTabIndex);
                    String innStr = inn_number + "\n" + inning.getString("battingteam");
                    spec.setContent(id);
                    spec.setIndicator(innStr);
                    Utils.tabSpecList.add(spec);
                    host.addTab(spec);
                    host.setCurrentTab(1);
                    indexOfTab++;
                    addInnning(tl, inn_number, inning, jSONObject.optJSONArray(Games.EXTRA_PLAYER_IDS));
                }
            }
            if (currentTableIndex == -1 && currentTableIndex == 0) {
                host.setCurrentTab(1);
            } else {
                host.setCurrentTab(currentTableIndex);
            }
        }
    }

    private static void addInnning(TableLayout ds_lay_tab_matches, String inn_number, JSONObject inning, JSONArray player) throws Exception {
        if (inning != null) {
            int j;
            Context con = ds_lay_tab_matches.getContext();
            TableLayout tableLayout = ds_lay_tab_matches;
            String str = inn_number;
            tableUtil.addTitleRowToTable(tableLayout, str, con.getString(R.string.space) + inning.getString("battingteam") + " - " + inning.getString("runs") + "/" + inning.getString("wickets") + " (" + inning.getString("overs") + " ov)", "RR - " + inning.getString("RR"));
            tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
            tableUtil.addBatsmanInfoToTable(ds_lay_tab_matches, con.getString(R.string.batsmen), con.getString(R.string.r), con.getString(R.string.b), con.getString(R.string._4s), con.getString(R.string._6s), con.getString(R.string.sr));
            tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
            JSONArray batsmen = inning.optJSONArray("batsmen");
            for (j = 0; j < batsmen.length(); j++) {
                JSONObject batsman = batsmen.getJSONObject(j);
                addCBZBatsmanToSC(ds_lay_tab_matches, j + 1, batsman, player);
                tableUtil.addRowToTableWithColor(ds_lay_tab_matches, con.getString(R.string.space) + con.getString(R.string.space) + con.getString(R.string.space) + con.getString(R.string.space) + con.getString(R.string.space) + batsman.getString("outdescription"));
            }
            JSONObject extras = inning.optJSONObject("extras");
            if (extras != null) {
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                tableUtil.addExtrasToTable(ds_lay_tab_matches, extras.getString("total"), extras.getString("byes"), extras.getString("legByes"), extras.getString("wideBalls"), extras.getString("noBalls"), extras.getString("penalty"));
            }
            tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
            tableLayout = ds_lay_tab_matches;
            tableUtil.addTotalToTable(tableLayout, "Total", inning.getString("runs") + "/" + inning.getString("wickets") + " (" + inning.getString("overs") + " ov)", "RR - " + inning.getString("RR"));
            tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
            tableUtil.addBowlerInfoToTable(ds_lay_tab_matches, con.getString(R.string.bowlers), con.getString(R.string.o), con.getString(R.string.m), con.getString(R.string.r), con.getString(R.string.w), con.getString(R.string.econ));
            tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
            JSONArray bowlers = inning.optJSONArray("bowlers");
            for (j = 0; j < bowlers.length(); j++) {
                tableLayout = ds_lay_tab_matches;
                addCBZBowlerToSC(tableLayout, j + 1, bowlers.getJSONObject(j), player);
            }
            JSONArray fow = inning.optJSONArray("fow");
            if (fow != null) {
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                tableUtil.addFOWTitleToTable(ds_lay_tab_matches, "Fall of wickets");
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                for (j = 0; j < fow.length(); j++) {
                    addCBZWicketsToSC(ds_lay_tab_matches, fow.getJSONObject(j), player);
                }
                return;
            }
            return;
        }
        tableUtil.addNoDataToTable(ds_lay_tab_matches);
    }

    private static String getPlayerName(JSONArray player, int playerId) throws Exception {
        String playerName = "";
        for (int j = 0; j < player.length(); j++) {
            JSONObject playerIdObj = player.getJSONObject(j);
            if (playerIdObj.getInt("id") == playerId) {
                playerName = playerIdObj.getString("name");
                String role = playerIdObj.getString("role");
                if (role.equals("")) {
                    return playerName;
                }
                return playerName + role.replace("wk", "&#8224;");
            }
        }
        return playerName;
    }

    private static void addCBZBatsmanToSC(TableLayout lay_tab_match, int number, JSONObject batsman, JSONArray player) throws Exception {
        if (batsman != null) {
            tableUtil.addBatsmanInfoToTable(lay_tab_match, String.valueOf(number) + ". " + getPlayerName(player, batsman.getInt("batsmanId")), batsman.getString("run"), batsman.getString("ball"), batsman.getString("four"), batsman.getString("six"), batsman.getString("sr"));
        }
    }

    private static void addCBZBowlerToSC(TableLayout lay_tab_match, int number, JSONObject bowler, JSONArray player) throws Exception {
        if (bowler != null) {
            tableUtil.addBowlerInfoToTable(lay_tab_match, String.valueOf(number) + ". " + getPlayerName(player, bowler.getInt("bowlerId")), bowler.getString("over"), bowler.getString("maiden"), bowler.getString("run"), bowler.getString("wicket"), bowler.getString("sr"));
        }
    }

    private static void addCBZWicketsToSC(TableLayout lay_tab_match, JSONObject outBatsman, JSONArray player) throws Exception {
        if (outBatsman != null) {
            tableUtil.addFOWToTable(lay_tab_match, outBatsman.getString("run") + "/" + outBatsman.getString("wicketnbr"), getPlayerName(player, outBatsman.getInt("outBatsmanId")), outBatsman.getString("overnbr") + " Overs");
        }
    }
}
