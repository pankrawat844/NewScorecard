package com.kliff.scorecard.utils;

import android.os.Build.VERSION;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.kliff.scorecard.R;

import org.json.JSONArray;
import org.json.JSONObject;

public final class tableUtil {
    private static final int FONT_HUGE = 18;
    private static final int FONT_LARGE = 16;
    private static final int FONT_NORMAL = 14;
    private static final int FONT_SMALL = 12;
    private static final String TAG = "com.live_cric_scores.tableUtil";
    private static final int TEXT_DISTANCE_LEFT_RIGHT = 15;
    private static final int TEXT_DISTANCE_TOP_BOTTOM = 10;
    private static final int WIDGET_DISTANCE_LEFT_RIGHT = 15;
    private static final int WIDGET_DISTANCE_TOP_BOTTOM = 1;

    private tableUtil() {
    }

    public static Spanned fromHtml(String source) {
        return Html.fromHtml(source);
    }

    private static void addTextToTableRow(TableRow row, String text, float weight) {
        addTextToTableRow(row, text, weight, Utils.MATCH_HEADER_TEXT_COLOR, Utils.MATCH_HEADER_BG_COLOR, 14);
    }

    private static void addTextToTableRow(TableRow row, String text, float weight, int txtColor, int bgColor, int fontSize) {
        TextView tv = new TextView(row.getContext());
        LayoutParams lpbatsmen = new LayoutParams(0, -2);
        lpbatsmen.weight = weight;
        lpbatsmen.gravity = 16;
        tv.setLayoutParams(lpbatsmen);
        if (text.equals("")) {
            tv.setPadding(0, 0, 0, 0);
            tv.setIncludeFontPadding(false);
            if (VERSION.SDK_INT >= 11) {
                tv.setAlpha(0.0f);
            }
        } else {
            tv.setTypeface(Utils.fontFace);
            tv.setPadding(15, 10, 15, 10);
            tv.setText(Html.fromHtml(text));
            tv.setTextSize(2, (float) fontSize);
            tv.setTextColor(txtColor);
        }
        row.setBackgroundColor(bgColor);
        row.addView(tv);
    }

    public static void addBowlerInfoToTable(TableLayout lay_tab, String name, String info1, String info2, String info3, String info4, String info5) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        if (name.equals(lay_tab.getContext().getString(R.string.bowlers))) {
            addTextToTableRow(row, name, 0.44f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info1, 0.14f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info2, 0.08f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info3, 0.11f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info4, 0.08f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info5, 0.15f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
        } else {
            addTextToTableRow(row, name, 0.43f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info1, 0.14f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info2, 0.09f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info3, 0.12f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info4, 0.07f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info5, 0.15f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        }
        lay_tab.addView(row, lprow);
    }

    public static void addBatsmanInfoToTable(TableLayout lay_tab, String name, String info1, String info2, String info3, String info4, String info5) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        if (name.equals(lay_tab.getContext().getString(R.string.batsmen))) {
            addTextToTableRow(row, name, 0.35f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info1, 0.13f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info2, 0.13f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info3, 0.1f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info4, 0.1f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
            addTextToTableRow(row, info5, 0.19f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
        } else {
            addTextToTableRow(row, "<b>" + name + "</b>", 0.35f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, "<b>" + info1 + "</b>", 0.13f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info2, 0.13f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info3, 0.1f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info4, 0.1f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
            addTextToTableRow(row, info5, 0.19f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        }
        lay_tab.addView(row, lprow);
    }

    public static void addFOWTitleToTable(TableLayout lay_tab, String name) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        addTextToTableRow(row, name, 1.0f, Utils.PLAYER_HEADER_TEXT_COLOR, Utils.PLAYER_HEADER_BG_COLOR, 14);
        lay_tab.addView(row, lprow);
    }

    public static void addFOWToTable(TableLayout lay_tab, String follOfWicket, String name, String overs) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        addTextToTableRow(row, follOfWicket, 0.2f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, "<b>" + name + "</b>", 0.5f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, overs, 0.3f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        lay_tab.addView(row, lprow);
    }

    public static void addExtrasToTable(TableLayout lay_tab, String extras, String byes, String legbyes, String wides, String noballs, String penalties) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        addTextToTableRow(row, "<b>Extras</b>", 0.27f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, "<b>" + extras + "</b>", 0.13f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, "b" + byes, 0.1f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, "lb" + legbyes, 0.13f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, "w" + wides, 0.15f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, "n" + noballs, 0.12f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        addTextToTableRow(row, "p" + penalties, 0.1f, Utils.PLAYER_TEXT_COLOR, Utils.PLAYER_BG_COLOR, 14);
        lay_tab.addView(row, lprow);
    }

    public static void addTotalToTable(TableLayout lay_tab, String teanname, String Score, String runrate) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        addTextToTableRow(row, "<b>" + teanname + "</b>", 0.2f, Utils.PLAYER_TEXT_COLOR, Utils.MATCH_HEADER_BG_COLOR, 20);
        addTextToTableRow(row, "<b>" + Score + "</b>", 0.55f, Utils.PLAYER_TEXT_COLOR, Utils.MATCH_HEADER_BG_COLOR, 20);
        addTextToTableRow(row, runrate, 0.25f, Utils.PLAYER_TEXT_COLOR, Utils.MATCH_HEADER_BG_COLOR, 15);
        lay_tab.addView(row, lprow);
    }

    public static void addRowToTable(TableLayout lay_tab, String text) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        if (text.equals("")) {
            lprow.setMargins(0, 0, 0, 0);
        } else {
            lprow.setMargins(1, 1, 1, 1);
        }
        row.setLayoutParams(lprow);
        addTextToTableRow(row, text, 1.0f);
        lay_tab.addView(row, lprow);
    }

    public static void addRowToTableWithColor(TableLayout lay_tab, String text) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        if (text.trim().contains("not out") || text.trim().contains("batting")) {
            addTextToTableRow(row, "<b>" + text + "</b>", 1.0f, Utils.NOUT_TEXT_COLOR, Utils.FOW_BG_COLOR, 12);
        } else {
            addTextToTableRow(row, text, 1.0f, Utils.FOW_TEXT_COLOR, Utils.FOW_BG_COLOR, 12);
        }
        lay_tab.addView(row, lprow);
    }

    public static void addTitleRowToTable(TableLayout lay_tab, String number, String score, String runrate) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.gravity = 17;
        lprow.setMargins(1, 1, 1, 1);
        row.setLayoutParams(lprow);
        addTextToTableRow(row, "<b>" + number + "</b>", 0.1f, Utils.PLAYER_TEXT_COLOR, Utils.MATCH_HEADER_BG_COLOR, 20);
        addTextToTableRow(row,"<b>"+ score+"<b>", 0.6f, Utils.PLAYER_TEXT_COLOR, Utils.MATCH_HEADER_BG_COLOR, 20);
        addTextToTableRow(row, runrate, 0.3f, Utils.PLAYER_TEXT_COLOR, Utils.MATCH_HEADER_BG_COLOR, 15);
        lay_tab.addView(row, lprow);
    }

    public static void addEmptyRowToTable(TableLayout lay_tab) {
        View line = new View(lay_tab.getContext());
        line.setLayoutParams(new LayoutParams(-1, 1));
        line.setBackgroundColor(Utils.ACTIVITY_HEADER_TEXT_COLOR);
        lay_tab.addView(line);
    }

    public static void addNoDataToTable(TableLayout lay_tab) {
        TableRow row = new TableRow(lay_tab.getContext());
        TableLayout.LayoutParams lprow = new TableLayout.LayoutParams(-1, -2);
        lprow.setMargins(15, 1, 15, 1);
        row.setLayoutParams(lprow);
        addTextToTableRow(row, "<b>Sorry.<br>No data available.<br>Try again after some time.</b>", 1.0f, Utils.NODATA_TEXT_COLOR, Utils.NODATA_BG_COLOR, 18);
        lay_tab.addView(row, lprow);
    }

    public static String getBatsmanCBZ(JSONObject batsman, boolean striker) throws Exception {
        String retString = "";
        if (batsman == null) {
            return retString;
        }
        String stikerSr = "-";
        if (batsman.optDouble("balls", 0.0d) != 0.0d) {
            stikerSr = String.format("%.2f", new Object[]{Double.valueOf((batsman.optDouble("runs") / batsman.optDouble("balls")) * 100.0d)});
        }
        return batsman.getString("fullName") + (striker ? "*" : "") + ":" + batsman.getString("runs") + "(" + batsman.getString("balls") + "b)-" + batsman.getString("fours") + "(4s)-" + batsman.getString("sixes") + "(6s)-" + stikerSr + "(sr)";
    }

    public static void FillMiniScoreToTableLayoutESPN(String result, TableLayout tl) {
        if (result != null) {
            try {
                String score;
                JSONObject jSONObject = new JSONObject(result);
                tl.removeAllViews();
                addEmptyRowToTable(tl);
                jSONObject = jSONObject;
                addRowToTable(tl, jSONObject.getJSONObject("live").getString("status"));
                SparseArray<String> teamInnings = new SparseArray();
                JSONArray jsnArrayInnings = jSONObject.getJSONArray("innings");
                for (int j = 0; j < jsnArrayInnings.length(); j++) {
                    JSONObject jsobj = jsnArrayInnings.getJSONObject(j);
                    int teamid = jsobj.getInt(tl.getResources().getString(R.string.battingteamid));
                    score = jsobj.getString(tl.getResources().getString(R.string.runs)) + "/" + jsobj.getString(tl.getResources().getString(R.string.wickets)) + " (" + jsobj.getString(tl.getResources().getString(R.string.overs)) + " ov)";
                    if (jsobj.getInt("live_current") == 1) {
                        score = "<b>" + score + "<b>";
                    }
                    String firstScore = (String) teamInnings.get(teamid);
                    if (firstScore == null) {
                        teamInnings.put(teamid, score);
                    } else {
                        teamInnings.put(teamid, firstScore + " & " + score);
                    }
                }
                for (int i = 0; i < teamInnings.size(); i++) {
                    int key = teamInnings.keyAt(i);
                    String teamName = cricinfodetailscore.getTeamName(jSONObject.getJSONArray("team"), key);
                    score = (String) teamInnings.get(key);
                    TableLayout tableLayout = tl;
                    addRowToTable(tableLayout, String.format("%s%s%s", new Object[]{teamName, tl.getResources().getString(R.string.space), score}));
                }
                addEmptyRowToTable(tl);
                addBatsmanInfoToTable(tl, tl.getResources().getString(R.string.batsmen), tl.getResources().getString(R.string.r), tl.getResources().getString(R.string.b), tl.getResources().getString(R.string._4s), tl.getResources().getString(R.string._6s), tl.getResources().getString(R.string.sr));
                addEmptyRowToTable(tl);
                jSONObject = jSONObject;
                commanjson.addBatsmanESPN(tl, jSONObject.getJSONObject("live").getJSONArray("batting").optJSONObject(0), jSONObject);
                jSONObject = jSONObject;
                commanjson.addBatsmanESPN(tl, jSONObject.getJSONObject("live").getJSONArray("batting").optJSONObject(1), jSONObject);
                addEmptyRowToTable(tl);
                addBowlerInfoToTable(tl, tl.getResources().getString(R.string.bowlers), tl.getResources().getString(R.string.o), tl.getResources().getString(R.string.m), tl.getResources().getString(R.string.r), tl.getResources().getString(R.string.w), tl.getResources().getString(R.string.econ));
                addEmptyRowToTable(tl);
                jSONObject = jSONObject;
                commanjson.addBowlerESPN(tl, jSONObject.getJSONObject("live").getJSONArray("bowling").optJSONObject(0), jSONObject);
                jSONObject = jSONObject;
                commanjson.addBowlerESPN(tl, jSONObject.getJSONObject("live").getJSONArray("bowling").optJSONObject(1), jSONObject);
                addEmptyRowToTable(tl);
                addRowToTable(tl, commanjson.getRecentOvers(jSONObject));
                addEmptyRowToTable(tl);
            } catch (Exception e) {
                Log.e("com.live_cric_scores.tableUtil: FillMiniScoreToTableLayoutESPN :", e.getMessage(), e.getCause());
            }
        }
    }
}
