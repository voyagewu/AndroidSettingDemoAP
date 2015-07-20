
package com.example.android_settings.sound;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.android_settings.R;

public class SoundSettings extends PreferenceFragment {

    private static String TAG = "SoundSettings";
    private SeekBar mSoundSeekbar;
    private AudioManager mAudioManager = null;

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
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        View view = inflater.inflate(R.layout.sound_settings, container, false);
        mSoundSeekbar = (SeekBar) view.findViewById(R.id.seekbar_sound);
        initSoundControl();

        return view;
    }

    private void initSoundControl() {
        try
        {

            mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

            mSoundSeekbar.setMax(mAudioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            mSoundSeekbar.setProgress(mAudioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            Log.d(TAG, "danny: current volume is " + mAudioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            Log.d(TAG, "danny: MAX volume is " + mAudioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));

            mSoundSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    Log.d(TAG, "danny: current volume is " + progress);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
