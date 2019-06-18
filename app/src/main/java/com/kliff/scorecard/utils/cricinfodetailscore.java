package com.kliff.scorecard.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;

import com.kliff.scorecard.R;
import com.kliff.scorecard.fragment.CricInfoDetailScoreFragment;

import org.json.JSONArray;
import org.json.JSONObject;

public final class cricinfodetailscore {
    private static final String TAG = "cricinfodetailscore";

    private cricinfodetailscore() {
    }

    public static void onPostExecute(String result, TableLayout ds_lay_tab_matches, TabHost host) throws Exception {
        JSONObject summary = new JSONObject(result).optJSONObject("summary");
        tableUtil.addRowToTable(ds_lay_tab_matches, summary.getJSONObject("live").getString("status"));
        JSONArray award = summary.optJSONArray("award");
        if (award != null && award.length() > 0) {
            String mMOM = getPlayerName(summary.optJSONObject("player"), award.optJSONObject(0).optInt("player_id"));
            if (mMOM.equals("")) {
                mMOM = "[TBD]";
            }
            tableUtil.addRowToTable(ds_lay_tab_matches, "MOM : " + mMOM);
        }
        JSONArray innings = summary.optJSONArray("innings");
        if (innings == null) {
            Log.d(TAG, "onPostExecute - 1");
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
            TabSpec fakeSpec = host.newTabSpec("about").setIndicator("faketab").setContent(CricInfoDetailScoreFragment.getInstance().getResources().getIdentifier("faketab", "id", CricInfoDetailScoreFragment.getInstance().getActivity().getPackageName()));
            host.addTab(fakeSpec);
            Utils.tabSpecList.add(fakeSpec);
            host.getTabWidget().getChildTabViewAt(0).setVisibility(8);
            indexOfTab = 0;
            for (int inni = innings.length(); inni > 0; inni--) {
                JSONObject inning = innings.optJSONObject(inni - 1);
                if (inning != null && inning.getInt("batted") == 1) {
                    int indexOfInn = indexOfTab + 1;
                    String newTabIndex = String.valueOf(indexOfInn + 1);
                    int id = CricInfoDetailScoreFragment.getInstance().getResources().getIdentifier("tab" + indexOfInn, "id",CricInfoDetailScoreFragment.getInstance().getActivity().getPackageName());
                    TableLayout tl = CricInfoDetailScoreFragment.getInstance().getView().findViewById(id);
                    tl.removeAllViews();
                    tl.setVisibility(8);
                    String inn_number = String.valueOf(inni);
                    TabSpec spec = host.newTabSpec("INN " + newTabIndex);
                    spec.setIndicator(inn_number + "\n" + getTeamAbbreviation(summary.optJSONArray("team"), inning.getInt("batting_team_id"))).setContent(id);
                    Utils.tabSpecList.add(spec);
                    host.addTab(spec);
                    host.setCurrentTab(1);
                    indexOfTab++;
                    addInnning(tl, inni, inning, summary.optJSONArray("team"), summary.optJSONObject("player"));
                }
            }
            if (currentTableIndex == -1 && currentTableIndex == 0) {
                host.setCurrentTab(1);
            } else {
                host.setCurrentTab(currentTableIndex);
            }
        }
    }

    public static void addInnning(TableLayout ds_lay_tab_matches, int inn_number, JSONObject inning, JSONArray team, JSONObject player) throws Exception {
        if (inning != null) {
            if (inning.getInt("batted") == 1) {
                int j;
                Context con = ds_lay_tab_matches.getContext();
                TableLayout tableLayout = ds_lay_tab_matches;
                tableUtil.addTitleRowToTable(tableLayout, String.valueOf(inn_number), con.getString(R.string.space) + getTeamAbbreviation(team, inning.getInt("batting_team_id")) + " - " + inning.getString("runs") + "/" + inning.getString("wickets") + " (" + inning.getString("overs") + " ov)", "RR - " + inning.getString("run_rate"));
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                tableUtil.addBatsmanInfoToTable(ds_lay_tab_matches, con.getString(R.string.batsmen), con.getString(R.string.r), con.getString(R.string.b), con.getString(R.string._4s), con.getString(R.string._6s), con.getString(R.string.sr));
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                JSONArray batsmen = inning.optJSONArray("batting");
                for (j = 0; j < batsmen.length(); j++) {
                    JSONObject batsman = batsmen.getJSONObject(j);
                    if (batsman.getString("batted").equals("yes")) {
                        addESPNBatsmanToSC(ds_lay_tab_matches, j + 1, batsman, player);
                        tableUtil.addRowToTableWithColor(ds_lay_tab_matches, con.getString(R.string.space) + con.getString(R.string.space) + con.getString(R.string.space) + con.getString(R.string.space) + con.getString(R.string.space) + batsman.getString("dismissal_string"));
                    }
                }
                JSONObject extras = inning.optJSONObject("extras");
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                tableUtil.addExtrasToTable(ds_lay_tab_matches, inning.getString("extras"), inning.getString("byes"), inning.getString("legbyes"), inning.getString("wides"), inning.getString("noballs"), inning.getString("penalties"));
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                tableLayout = ds_lay_tab_matches;
                tableUtil.addTotalToTable(tableLayout, "Total", inning.getString("runs") + "/" + inning.getString("wickets") + " (" + inning.getString("overs") + " ov)", "RR - " + inning.getString("run_rate"));
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                tableUtil.addBowlerInfoToTable(ds_lay_tab_matches, con.getString(R.string.bowlers), con.getString(R.string.o), con.getString(R.string.m), con.getString(R.string.r), con.getString(R.string.w), con.getString(R.string.econ));
                tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                JSONArray bowlers = inning.optJSONArray("bowling");
                for (j = 0; j < bowlers.length(); j++) {
                    JSONObject bowler = bowlers.getJSONObject(j);
                    if (bowler.getString("bowled").equals("yes")) {
                        addESPNBowlerToSC(ds_lay_tab_matches, j + 1, bowler, player);
                    }
                }
                JSONArray fow = inning.optJSONArray("fow");
                if (fow != null) {
                    tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                    tableUtil.addFOWTitleToTable(ds_lay_tab_matches, "Fall of wickets");
                    tableUtil.addEmptyRowToTable(ds_lay_tab_matches);
                    for (j = 0; j < fow.length(); j++) {
                        JSONObject outBatsman = fow.getJSONObject(j);
                        if (!outBatsman.getString("fow_type_name").equals("end of innings")) {
                            addESPNWicketsToSC(ds_lay_tab_matches, j + 1, outBatsman, player);
                        }
                    }
                    return;
                }
                return;
            }
            return;
        }
        tableUtil.addNoDataToTable(ds_lay_tab_matches);
    }

    public static String getPlayerName(JSONObject player, int playerId) throws Exception {
        JSONObject playerIdObj = player.getJSONObject(String.valueOf(playerId));
        String playerName = playerIdObj.getString("scorecard_batting_name");
        JSONArray roleArr = playerIdObj.optJSONArray("role");
        if (roleArr != null) {
            for (int j = 0; j < roleArr.length(); j++) {
                String role = roleArr.getString(j);
                if (role.equals("captain")) {
                    playerName = playerName + "(c)";
                } else if (role.equals("keeper")) {
                    playerName = playerName + "(&#8224;)";
                }
            }
        }
        return playerName;
    }

    public static void addESPNBatsmanToSC(TableLayout lay_tab_match, int number, JSONObject batsman, JSONObject player) throws Exception {
        if (batsman != null) {
            tableUtil.addBatsmanInfoToTable(lay_tab_match, String.valueOf(number) + ". " + getPlayerName(player, batsman.getInt("player_id")), batsman.getString("runs"), batsman.getString("balls_faced"), batsman.getString("fours"), batsman.getString("sixes"), batsman.getString("strike_rate"));
        }
    }

    public static void addESPNBowlerToSC(TableLayout lay_tab_match, int number, JSONObject bowler, JSONObject player) throws Exception {
        if (bowler != null) {
            tableUtil.addBowlerInfoToTable(lay_tab_match, String.valueOf(number) + ". " + getPlayerName(player, bowler.getInt("player_id")), bowler.getString("overs"), bowler.getString("maidens"), bowler.getString("conceded"), bowler.getString("wickets"), bowler.getString("economy_rate"));
        }
    }

    public static void addESPNWicketsToSC(TableLayout lay_tab_match, int number, JSONObject outBatsman, JSONObject player) throws Exception {
        if (outBatsman != null) {
            tableUtil.addFOWToTable(lay_tab_match, outBatsman.getString("fow_runs") + "/" + String.valueOf(number), getPlayerName(player, outBatsman.getInt("out_player_id")), outBatsman.getString("fow_overs") + " Overs");
        }
    }

    public static String getTeamName(JSONArray team, int teamId) throws Exception {
        String teamName = "";
        for (int j = 0; j < team.length(); j++) {
            JSONObject teamObj = team.getJSONObject(j);
            if (teamObj.getInt("team_id") == teamId) {
                return teamObj.getString("team_name");
            }
        }
        return teamName;
    }

    public static String getTeamAbbreviation(JSONArray team, int teamId) throws Exception {
        String teamName = "";
        for (int j = 0; j < team.length(); j++) {
            JSONObject teamObj = team.getJSONObject(j);
            if (teamObj.getInt("team_id") == teamId) {
                return teamObj.getString("team_abbreviation");
            }
        }
        return teamName;
    }
}
