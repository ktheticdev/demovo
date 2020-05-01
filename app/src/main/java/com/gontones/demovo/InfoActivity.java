package com.gontones.demovo;


import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class InfoActivity extends Activity {
    static SharedPreferences prefs;
    static int style;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            style = Integer.parseInt(prefs.getString("style", "1"));
            if (style == 1) {
                setTheme(R.style.AppTheme);
            } else if (style == 2) {
                setTheme(R.style.AppTheme_Light);
            } else if (style == 3) {
                setTheme(R.style.Sys);
            } else if (style == 4) {
                setTheme(R.style.Sys_Light);
            } else if (style == 5) {
                setTheme(R.style.Holo);
            } else if (style == 6) {
                setTheme(R.style.Holo_Light);
            } else if (style == 7) {
                setTheme(R.style.Necro);
            } else if (style == 8) {
                setTheme(R.style.Necro_Light);
            }
        } catch (Exception themed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setTheme(R.style.Holo);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setTheme(R.style.AppTheme);
            } else {
                setTheme(R.style.Necro);
            }
        }
        ActionBar actionBar = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar = getActionBar();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity1);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
