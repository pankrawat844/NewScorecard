package com.kliff.scorecard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TabHost.TabSpec;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.core.content.ContextCompat;

import com.kliff.scorecard.R;

public final class Utils {
    public static int ACTIVITY_HEADER_BG_COLOR = 0;
    public static int ACTIVITY_HEADER_TEXT_COLOR = 0;
    public static int FOW_BG_COLOR = 0;
    public static int FOW_TEXT_COLOR = 0;
    public static int MATCH_HEADER_BG_COLOR = 0;
    public static int MATCH_HEADER_TEXT_COLOR = 0;
    public static int MATCH_TITLE_BG_COLOR = 0;
    public static int MATCH_TITLE_TEXT_COLOR = 0;
    public static int NODATA_BG_COLOR = 0;
    public static int NODATA_TEXT_COLOR = 0;
    public static int NOUT_TEXT_COLOR = 0;
    public static int PLAYER_BG_COLOR = 0;
    public static int PLAYER_HEADER_BG_COLOR = 0;
    public static int PLAYER_HEADER_TEXT_COLOR = 0;
    public static int PLAYER_TEXT_COLOR = 0;
    public static int SETTING_TEXT_COLOR = 0;
    public static int SPINNER_BG_COLOR = 0;
    public static int SPINNER_TEXT_COLOR = 0;
    private static final String TAG = "com.live_cric_scores.Utils";
    private static final int THEME_AFGHANISTAN = 5;
    private static final int THEME_AUSTRALIA = 6;
    private static final int THEME_BANGLADESH = 7;
    private static final int THEME_BLACK = 4;
    private static final int THEME_ENGLAND = 8;
    public static final int THEME_GLOSSYBLUE = 19;
    public static final int THEME_GLOSSYDARK = 17;
    public static final int THEME_GLOSSYLIGHT = 18;
    private static final int THEME_GRAY = 0;
    private static final int THEME_INDIA = 9;
    private static final int THEME_INDIGO = 2;
    private static final int THEME_IRELAND = 10;
    private static final int THEME_LIGHTBLUE = 1;
    private static final int THEME_NEWZEALAND = 11;
    private static final int THEME_PAKISTAN = 12;
    private static final int THEME_SOUTHAFRICA = 13;
    private static final int THEME_SRILANKA = 14;
    private static final int THEME_WESTINDIES = 15;
    private static final int THEME_WHITE = 3;
    private static final int THEME_ZIMBABWE = 16;
    public static int TRANSPARENT_BG_COLOR = 0;
    public static Animation animation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
    public static String fonrString = "GeosansLight-Numbers.ttf";
    public static int fontCurrent = -1;
    public static Typeface fontFace = null;
    private static SharedPreferences sharedpreferences;
    public static ArrayList<TabSpec> tabSpecList = new ArrayList();
    public static int themeCurrent = -1;

    public static void setAnimation(View view) {
        Log.d("TAg", "setAnimation -start : view : " + view.getVisibility() + "focus : " + view.hasFocus());
        animation.setRepeatCount(-1);
        animation.setDuration(2000);
        view.startAnimation(animation);
    }

    public static void clearAnimation(View view) {
        view.clearAnimation();
        Log.d("TAG", "clearAnimation -start : view : " + view.getVisibility() + "focus : " + view.hasFocus());
    }

    public static void changeToTheme(Activity activity, int theme, boolean selfCall) {
        if (activity != null) {
            setSettingValue(activity.getApplicationContext());
            themeCurrent = theme;
            activity.finish();
            activity.overridePendingTransition(0, 0);
            Intent intent = new Intent(activity, activity.getClass());
            activity.startActivity(intent);
            if (selfCall) {
                activity.overridePendingTransition(0, 0);
            } else {
                activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        getStoredSettingValue(activity);
        switch (5) {
            case 1:
                activity.setTheme(R.style.LightBlueTheme);
                break;
            case 2:
                activity.setTheme(R.style.IndigoTheme);
                break;
            case 3:
                activity.setTheme(R.style.WhiteTheme);
                break;
            case 4:
                activity.setTheme(R.style.BlackTheme);
                break;
            case 5:
                activity.setTheme(R.style.Afghanistan);
                break;
            case 6:
                activity.setTheme(R.style.Australia);
                break;
            case 7:
                activity.setTheme(R.style.Bangladesh);
                break;
            case 8:
                activity.setTheme(R.style.England);
                break;
            case 9:
                activity.setTheme(R.style.India);
                break;
            case 10:
                activity.setTheme(R.style.Ireland);
                break;
            case 11:
                activity.setTheme(R.style.NewZealand);
                break;
            case 12:
                activity.setTheme(R.style.Pakistan);
                break;
            case 13:
                activity.setTheme(R.style.SouthAfrica);
                break;
            case 14:
                activity.setTheme(R.style.SriLanka);
                break;
            case 15:
                activity.setTheme(R.style.WestIndies);
                break;
            case 16:
                activity.setTheme(R.style.Zimbabwe);
                break;
            case 17:
            case 18:
            case 19:
                activity.setTheme(R.style.GlossyDark);
                break;
            default:
                activity.setTheme(R.style.GrayTheme);
                break;
        }
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.ACTIVITY_HEADER_TEXT_COLOR, typedValue, true);
        ACTIVITY_HEADER_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.ACTIVITY_HEADER_BG_COLOR, typedValue, true);
        ACTIVITY_HEADER_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.MATCH_TITLE_TEXT_COLOR, typedValue, true);
        MATCH_TITLE_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.MATCH_TITLE_BG_COLOR, typedValue, true);
        MATCH_TITLE_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.SPINNER_TEXT_COLOR, typedValue, true);
        SPINNER_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.SPINNER_BG_COLOR, typedValue, true);
        SPINNER_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.MATCH_HEADER_TEXT_COLOR, typedValue, true);
        MATCH_HEADER_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.MATCH_HEADER_BG_COLOR, typedValue, true);
        MATCH_HEADER_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.PLAYER_HEADER_TEXT_COLOR, typedValue, true);
        PLAYER_HEADER_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.PLAYER_HEADER_BG_COLOR, typedValue, true);
        PLAYER_HEADER_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.PLAYER_TEXT_COLOR, typedValue, true);
        PLAYER_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.PLAYER_BG_COLOR, typedValue, true);
        PLAYER_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.FOW_BG_COLOR, typedValue, true);
        FOW_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.FOW_TEXT_COLOR, typedValue, true);
        FOW_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.NOUT_TEXT_COLOR, typedValue, true);
        NOUT_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.TRANSPARENT_BG_COLOR, typedValue, true);
        TRANSPARENT_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.NODATA_BG_COLOR, typedValue, true);
        NODATA_BG_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.NODATA_TEXT_COLOR, typedValue, true);
        NODATA_TEXT_COLOR = typedValue.data;
        activity.getTheme().resolveAttribute(R.attr.SETTING_TEXT_COLOR, typedValue, true);
        SETTING_TEXT_COLOR = typedValue.data;
    }

    public static void changeBackground(Activity activity) {
        try {
            View content = activity.findViewById(R.id.relViewMiniScore).getRootView();
            Bitmap bm = null;
            switch (themeCurrent) {
                case 17:
                    bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.back1);
                    break;
                case 18:
                    bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.back2);
                    break;
                case 19:
                    bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.back3);
                    break;
            }
            if (bm != null) {
                setBackgroundBitmapDrawable(activity, content, BlurBuilder.blur(content.getContext(), bm));
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e.getCause());
        }
    }

    public static void setAdsOnTime(Context context) {
        Calendar rightNow = Calendar.getInstance();
        nwUtil.currentTimeSince1970 = (rightNow.getTimeInMillis() + ((long) (rightNow.get(15) + rightNow.get(16)))) / 1000;
        if (nwUtil.currentTimeSince1970 - nwUtil.AdsClickedTimeSince1970 > nwUtil.ADONTIMEOUT_SECONDSs) {
            nwUtil.addOn = true;
            setSettingValue(context);
        }
    }

    private static void setBackgroundBitmapDrawable(Activity activity, View view, Bitmap bitmap) {
        if (VERSION.SDK_INT >= 16) {
            view.setBackground(new BitmapDrawable(activity.getResources(), bitmap));
        } else {
            view.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
    }

    public static void setBackgroundDrawable(View view, int id) {
        if (VERSION.SDK_INT >= 16) {
            view.setBackground(ContextCompat.getDrawable(view.getContext(), id));
        } else {
            view.setBackgroundDrawable(view.getResources().getDrawable(id));
        }
    }

    public static void setSettingValue(Context context) {
        if (context != null) {
            sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Editor editor = sharedpreferences.edit();
            editor.putBoolean("AutoRefresh", nwUtil.auto_refresh);
            editor.putInt("RefreshRate", nwUtil.refresh_rate);
            editor.putInt("ThemeCurrent", themeCurrent);
            editor.putInt("FontCurrent", fontCurrent);
            editor.putBoolean("AddOn", nwUtil.addOn);
            editor.putLong("AdsClickedTime", nwUtil.AdsClickedTimeSince1970);
            editor.putString("selectedCBZNotiMatch", nwUtil.selectedCBZNotiMatch);
            editor.putInt("selectedCBZNotiMatchID", nwUtil.selectedCBZNotiMatchID);
            editor.putString("selectedCBZNotiMatchState", nwUtil.selectedCBZNotiMatchState);
            editor.putString("selectedCBZMatch", nwUtil.selectedCBZMatch);
            editor.putInt("selectedCBZMatchID", nwUtil.selectedCBZMatchID);
            editor.putString("selectedESPNMatch", nwUtil.selectedESPNMatch);
            editor.putString("selectedESPNMatchID", nwUtil.selectedESPNMatchID);
            Log.d("Utils", " : setValue value - " + nwUtil.selectedCBZNotiMatch);
            editor.apply();
        }
    }

    public static void getStoredSettingValue(Context context) {
        if (context != null) {
            sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
            nwUtil.auto_refresh = sharedpreferences.getBoolean("AutoRefresh", true);
            nwUtil.refresh_rate = sharedpreferences.getInt("RefreshRate", 5000);
            themeCurrent = sharedpreferences.getInt("ThemeCurrent", 0);
            fontCurrent = sharedpreferences.getInt("FontCurrent", 0);
            fonrString = getFontString(fontCurrent);
            fontFace = Typeface.createFromAsset(context.getAssets(), fonrString);
            nwUtil.addOn = sharedpreferences.getBoolean("AddOn", true);
            nwUtil.AdsClickedTimeSince1970 = sharedpreferences.getLong("AdsClickedTime", 0);
            nwUtil.selectedCBZNotiMatch = sharedpreferences.getString("selectedCBZNotiMatch", "None");
            nwUtil.selectedCBZNotiMatchID = sharedpreferences.getInt("selectedCBZNotiMatchID", 0);
            nwUtil.selectedCBZNotiMatchState = sharedpreferences.getString("selectedCBZNotiMatchState", "");
            nwUtil.selectedCBZMatch = sharedpreferences.getString("selectedCBZMatch", "None");
            nwUtil.selectedCBZMatchID = sharedpreferences.getInt("selectedCBZMatchID", 0);
            nwUtil.selectedESPNMatch = sharedpreferences.getString("selectedESPNMatch", "None");
            nwUtil.selectedESPNMatchID = sharedpreferences.getString("selectedESPNMatchID", "None");
            Log.d("TAG", " : getValue value - " + nwUtil.selectedCBZNotiMatch);
        }
    }

    public static String getFontString(int indexOfFont) {
        String fontStr = "GeosansLight-Numbers.ttf";
        switch (indexOfFont) {
            case 0:
                return "Roboto-Light.ttf";
            case 1:
                return "Roboto-Thin.ttf";
            case 2:
                return "angelina.ttf";
            case 3:
                return "arial_narrow_7.ttf";
            case 4:
                return "basic_sans_serif_7.ttf";
            case 5:
                return "sans_serif_plus_7.ttf";
            case 6:
                return "advanced_sans_serif_7.ttf";
            case 7:
                return "semi_rounded_sans_serif_7.ttf";
            case 8:
                return "DroidSerif-Bold.ttf";
            case 9:
                return "DroidSerif-BoldItalic.ttf";
            case 10:
                return "DroidSerif-Italic.ttf";
            case 11:
                return "DroidSerif-Regular.ttf";
            case 12:
                return "elegant_line_7.ttf";
            case 13:
                return "GeosansLight-Numbers.ttf";
            case 14:
                return "GeosansLight.ttf";
            case 15:
                return "Android_Insomnia_Regular.ttf";
            case 16:
                return "NeverSayNever.ttf";
            case 17:
                return "software_tester_7.ttf";
            case 18:
                return "steel_blade_7.ttf";
            case 19:
                return "zx_spectrum-7.ttf";
            case 20:
                return "zx_spectrum-7_bold.ttf";
            case 21:
                return "thin_pixel-7.ttf";
            default:
                return fontStr;
        }
    }

    public static boolean isMatchSelected4CBZNoti(Context con) {
        return (nwUtil.selectedCBZNotiMatch.equals(con.getString(R.string.none)) || nwUtil.selectedCBZNotiMatch.equals(con.getString(R.string.selectmatch))) ? false : true;
    }

    public static boolean isMatchSelected4CBZ(Context con) {
        return (nwUtil.selectedCBZMatch.equals(con.getString(R.string.none)) || nwUtil.selectedCBZMatch.contains(con.getString(R.string.selectmatch1))) ? false : true;
    }

    public static boolean isMatchSelected4ESPN(Context con) {
        return (nwUtil.selectedESPNMatch.equals(con.getString(R.string.none)) || nwUtil.selectedESPNMatch.contains(con.getString(R.string.selectmatch2))) ? false : true;
    }

    public static void setSelectedCBZNotiMatchToNone(Context con) {
        nwUtil.selectedCBZNotiMatch = con.getString(R.string.none);
        nwUtil.selectedCBZNotiMatchID = 0;
        setSettingValue(con);
    }

    public static int oversToBall(String overs) {
        String[] data = overs.split("\\.");
        int balls = 0;
        if (data.length == 2) {
            balls = Integer.parseInt(data[1]);
        }
        return (Integer.parseInt(data[0]) * 6) + balls;
    }

    public static void setViewEnabled(View matchButton, boolean enabled) {
        matchButton.setEnabled(enabled);
        matchButton.setClickable(enabled);
        float alpha = enabled ? 1.0f : 0.4f;
        if (VERSION.SDK_INT < 11) {
            AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            matchButton.startAnimation(animation);
            return;
        }
        matchButton.setAlpha(alpha);
    }
}
