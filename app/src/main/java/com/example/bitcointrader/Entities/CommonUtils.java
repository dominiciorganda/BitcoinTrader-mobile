package com.example.bitcointrader.Entities;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonUtils {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPreferences;
    private static Context context;

    /**
     * Create SharedPreference and SharedPreferecne Editor for Context
     *
     * @param
     */
    private static void createSharedPreferenceEditor(Context c) {
        try {
//            if (context != null) {
//                mContext = getContext;
//            } else {
//                mContext = ApplicationStore.getContext();
//            }
            context = c;
            sharedPreferences = context.getSharedPreferences("Authentication", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Put String in SharedPreference Editor
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putPrefString(Context context, String key, String value) {
        try {
            createSharedPreferenceEditor(context);
            editor.putString(key, value);
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String getPrefString(Context context, String key) {
        try {
            createSharedPreferenceEditor(context);
            return sharedPreferences.getString(key, "not found");
        } catch (Exception e) {
            e.printStackTrace();
            return "not found";
        }
    }

    public static void logout(Context context) {
        try {
            createSharedPreferenceEditor(context);
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
