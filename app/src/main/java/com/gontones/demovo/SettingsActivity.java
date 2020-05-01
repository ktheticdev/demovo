package com.gontones.demovo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
    static SharedPreferences prefs;
    static int style;
    static Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        style = Integer.parseInt(prefs.getString("style", "1"));
        try {
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
        super.onCreate(savedInstanceState);
        ActionBar actionBar = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar = getActionBar();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        addPreferencesFromResource(R.xml.root_preferences);
        if(MainActivity.first) {
            MainActivity.first = false;
            finish();
        }
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ListPreference myPref = (ListPreference) findPreference("style");
            if (myPref != null)
                myPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        new AlertDialog.Builder(SettingsActivity.this)
                                .setTitle(R.string.themechanged)
                                .setMessage(R.string.thememessage)
                                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(intent);
                                    }
                                })
                                .setCancelable(false)
                                .create().show();
                        return true;
                    }
                });
        }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}