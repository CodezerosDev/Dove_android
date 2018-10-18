package com.darpysolutions.dove.NetUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.Map;

/**
 * Created by Narendra Keda on 4/28/2016.
 */
public class PrefUtilities {
    public static String PREFERENCE_KEY = "AuthPref";

    // / Get Your String Value by Giving Key
    public static String getString(Context ctx, String Key) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        return prefs.getString(Key, "");
    }

    // / Save Your String Value by Giving Key and its corrosponding Value
    public static void saveString(Context ctx, String Key, String Value) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    // / Get Your Int Value by Giving Key
    public static int getInt(Context ctx, String Key) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        return prefs.getInt(Key, 0);
    }

    // / Save Your Int Value by Giving Key and its corrosponding Value
    public static void saveInt(Context ctx, String Key, int Value) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Key, Value);
        editor.apply();
    }

    public static void saveFloat(Context ctx, String Key, float Value) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(Key, Value);
        editor.apply();
    }

    // / Get Your Float Value by Giving Key
    public static float getFloat(Context ctx, String Key) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        return prefs.getFloat(Key, 0);
    }

    // / Get Your boolean Value by Giving Key
    public static boolean getBoolean(Context ctx, String Key) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(Key, false);
    }

    public static void saveBoolean(Context ctx, String Key, boolean Value) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Key, Value);
        editor.apply();
    }

    public static void PrintPreference(Context cntx) {
        SharedPreferences pref = cntx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        Map<String, ?> keys = pref.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.e("PREFERENCES", entry.getKey() + ": "
                    + entry.getValue().toString());
        }
    }


    public static void clearPref(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        pref.edit().clear().commit();
    }
}
