package com.gontones.demovo;

import android.content.Context;
import android.content.SharedPreferences;
public class FirstRun {

    private static String PACKAGE_NAME;
    private static String IS_FIRST_RUN_KEY;

    private static SharedPreferences getPrefs(Context context) {

        PACKAGE_NAME = context.getPackageName();
        IS_FIRST_RUN_KEY = PACKAGE_NAME+"-first-run";
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isFirstRun(Context context) {

        SharedPreferences prefs = FirstRun.getPrefs(context);
        boolean isFirstRun = prefs.getBoolean(IS_FIRST_RUN_KEY, true);
        if(isFirstRun) {
            prefs.edit().putBoolean(IS_FIRST_RUN_KEY, false).commit();
        }
        return isFirstRun;
    }

    public static void clear(Context context) {
        SharedPreferences prefs = FirstRun.getPrefs(context);
        prefs.edit().putBoolean(IS_FIRST_RUN_KEY, true).commit();
    }
}