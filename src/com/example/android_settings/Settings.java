package com.example.android_settings;

import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;



public class Settings extends PreferenceActivity
 {
    private static final String TAG = "AvmSettings";

    @Override
    public void onBuildHeaders(List<Header> headers) {
        // create preference headers
        loadHeadersFromResource(R.layout.settings_headers, headers);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Log.d(TAG, " danny onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }



 }