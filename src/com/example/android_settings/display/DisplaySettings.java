
package com.example.android_settings.display;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.android_settings.R;
import com.example.android_settings.SettingsPreferenceFragment;

public class DisplaySettings extends SettingsPreferenceFragment {
    private static String TAG = "DisplaySettings";
    private SeekBar mBrightnessSeekBar;
    private CheckBox mAutoBrightnessCheckBox;
    private ContentResolver mContentResolver;

    private boolean mAutomaticMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (((PreferenceActivity) getActivity()).onIsMultiPane()) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
            getActivity().getActionBar().setHomeButtonEnabled(false);
        } else {
            setHasOptionsMenu(true);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            getActivity().getActionBar().setHomeButtonEnabled(true);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!((PreferenceActivity) getActivity()).onIsMultiPane()) {
                    getActivity().onBackPressed();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContentResolver = getActivity().getContentResolver();

        View view = inflater.inflate(R.layout.display_settings, container, false);

        mBrightnessSeekBar = (SeekBar) view.findViewById(R.id.seekbar_brightness);

        initBrightnessControls();

        mAutoBrightnessCheckBox = (CheckBox) view
                .findViewById(R.id.check_box_brightness_automatic_mode);
        initAutoBrightnessControl();

        return view;
    }

    private void initBrightnessControls()
    {
        try
        {
            int screen_brightness = getBrightness();

            mBrightnessSeekBar.setMax(255);
            mBrightnessSeekBar.setProgress(screen_brightness);
            mBrightnessSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progresValue,
                        boolean fromUser) {
                    progress = progresValue;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Do something here,
                    // if you want to do anything at the start of
                    // touching the seekbar
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if(mAutomaticMode == true)
                        mAutoBrightnessCheckBox.setChecked(false);
                    setBrightness(progress);

                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int getBrightness() {
        int curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    mContentResolver,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        return curBrightnessValue;
    }

    private boolean setBrightness(int brightness) {

        android.provider.Settings.System.putInt(mContentResolver,
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                brightness);

        return true;
    }

    private void initAutoBrightnessControl() {
        mAutomaticMode = getBrightnessMode(0)
                == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

        mBrightnessSeekBar.setEnabled(!mAutomaticMode);

        mAutoBrightnessCheckBox.setChecked(mAutomaticMode);

        mAutoBrightnessCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBrightnessSeekBar.setEnabled(!isChecked);
                setBrightnessMode(isChecked ? android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                        : android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            }
        });
    }

    private int getBrightnessMode(int defaultValue) {
        int brightnessMode = defaultValue;
        try {
            brightnessMode = android.provider.Settings.System.getInt(mContentResolver,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (SettingNotFoundException snfe) {
            Log.d(TAG, "SettingNotFoundException: " + snfe.toString());
        }
        return brightnessMode;
    }

    private void setBrightnessMode(int mode) {
        mAutomaticMode = mode == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        android.provider.Settings.System.putInt(mContentResolver,
                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
    }
}
