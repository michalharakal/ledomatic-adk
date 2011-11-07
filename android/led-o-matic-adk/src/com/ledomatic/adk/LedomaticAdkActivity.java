package com.ledomatic.adk;


import org.hermit.android.core.MainActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class LedomaticAdkActivity extends MainActivity {
	   // ******************************************************************** //
    // Activity Lifecycle.
    // ******************************************************************** //

    /**
     * Called when the activity is starting.  This is where most
     * initialisation should go: calling setContentView(int) to inflate
     * the activity's UI, etc.
     * 
     * You can call finish() from within this function, in which case
     * onDestroy() will be immediately called without any of the rest of
     * the activity lifecycle executing.
     * 
     * Derived classes must call through to the super class's implementation
     * of this method.  If they do not, an exception will be thrown.
     * 
     * @param   icicle          If the activity is being re-initialised
     *                          after previously being shut down then this
     *                          Bundle contains the data it most recently
     *                          supplied in onSaveInstanceState(Bundle).
     *                          Note: Otherwise it is null.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // We don't want a title bar or status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
      
        // We want the audio controls to control our sound volume.
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get our power manager for wake locks.
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        
        // Create the application GUI.
        audioInstrument = new InstrumentPanel(this);
        setContentView(audioInstrument);
        
        // Restore our preferences.
        updatePreferences();
    }
    

    /**
     * Called after {@link #onCreate} or {@link #onStop} when the current
     * activity is now being displayed to the user.  It will
     * be followed by {@link #onRestart}.
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        
        super.onStart();
        audioInstrument.onStart();
    }


    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(),
     * for your activity to start interacting with the user.  This is a good
     * place to begin animations, open exclusive-access devices (such as the
     * camera), etc.
     * 
     * Derived classes must call through to the super class's implementation
     * of this method.  If they do not, an exception will be thrown.
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");

        super.onResume();
        
        
        // Take the wake lock if we want it.
        if (wakeLock != null && !wakeLock.isHeld())
            wakeLock.acquire();

        audioInstrument.onResume();

        // Just start straight away.
        audioInstrument.surfaceStart();
    }


    /**
     * Called to retrieve per-instance state from an activity before being
     * killed so that the state can be restored in onCreate(Bundle) or
     * onRestoreInstanceState(Bundle) (the Bundle populated by this method
     * will be passed to both).
     * 
     * @param   outState        A Bundle in which to place any state
     *                          information you wish to save.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    
    /**
     * Called as part of the activity lifecycle when an activity is going
     * into the background, but has not (yet) been killed.  The counterpart
     * to onResume(). 
     */
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        
        super.onPause();
        
        audioInstrument.onPause();

        // Let go the wake lock if we have it.
        if (wakeLock != null && wakeLock.isHeld())
            wakeLock.release();
    }


    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onStart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();

        audioInstrument.onStop();
    }


    // ******************************************************************** //
    // Preferences Handling.
    // ******************************************************************** //

    /**
     * Read our application preferences and configure ourself appropriately.
     */
    private void updatePreferences() {
        SharedPreferences prefs =
                        PreferenceManager.getDefaultSharedPreferences(this);

        // Get the desired sample rate.
        int sampleRate = 8000;
        try {
            String srate = prefs.getString("sampleRate", null);
            sampleRate = Integer.valueOf(srate);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad sampleRate");
        }
        if (sampleRate < 8000)
            sampleRate = 8000;
        Log.i(TAG, "Prefs: sampleRate " + sampleRate);
        audioInstrument.setSampleRate(sampleRate);
        
        // Get the desired block size.
        int blockSize = 256;
        try {
            String bsize = prefs.getString("blockSize", null);
            blockSize = Integer.valueOf(bsize);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad blockSize");
        }
        Log.i(TAG, "Prefs: blockSize " + blockSize);
        audioInstrument.setBlockSize(blockSize);
        
        // Get the desired window function.
        org.hermit.dsp.Window.Function windowFunc =
                            org.hermit.dsp.Window.Function.BLACKMAN_HARRIS;
        try {
            String func = prefs.getString("windowFunc", null);
            windowFunc = org.hermit.dsp.Window.Function.valueOf(func);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad windowFunc");
        }
        Log.i(TAG, "Prefs: windowFunc " + windowFunc);
        audioInstrument.setWindowFunc(windowFunc);
        
        // Get the desired decimation.
        int decimateRate = 2;
        try {
            String drate = prefs.getString("decimateRate", null);
            decimateRate = Integer.valueOf(drate);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad decimateRate");
        }
        Log.i(TAG, "Prefs: decimateRate " + decimateRate);
        audioInstrument.setDecimation(decimateRate);
        
        // Get the desired histogram smoothing window.
        int averageLen = 4;
        try {
            String alen = prefs.getString("averageLen", null);
            averageLen = Integer.valueOf(alen);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad averageLen");
        }
        Log.i(TAG, "Prefs: averageLen " + averageLen);
        audioInstrument.setAverageLen(averageLen);

        try {
 //           String func = prefs.getString("instruments", null);
    //        if (func != null)
   //         	visibleInstruments = InstrumentPanel.Instruments.valueOf(func);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad instruments");
        }
        
        // Get the desired orientation.
        int orientMode = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        try {
            String omode = prefs.getString("orientationMode", null);
            orientMode = Integer.valueOf(omode);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad orientationMode");
        }
        Log.i(TAG, "Prefs: orientationMode " + orientMode);
        setRequestedOrientation(orientMode);

        boolean keepAwake = false;
        try {
            keepAwake = prefs.getBoolean("keepAwake", false);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad keepAwake");
        }
        if (keepAwake) {
            Log.i(TAG, "Prefs: keepAwake true: take the wake lock");
            if (wakeLock == null)
                wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
            if (!wakeLock.isHeld())
                wakeLock.acquire();
        } else {
            Log.i(TAG, "Prefs: keepAwake false: release the wake lock");
            if (wakeLock != null && wakeLock.isHeld())
                wakeLock.release();
            wakeLock = null;
        }

        boolean debugStats = false;
        try {
            debugStats = prefs.getBoolean("debugStats", false);
        } catch (Exception e) {
            Log.e(TAG, "Pref: bad debugStats");
        }
        Log.i(TAG, "Prefs: debugStats " + debugStats);
        audioInstrument.setShowStats(debugStats);
    }
    

    // ******************************************************************** //
    // Class Data.
    // ******************************************************************** //

    // Debugging tag.
    private static final String TAG = "Audalyzer";
   
    
    // ******************************************************************** //
    // Private Data.
    // ******************************************************************** //
    
    // Our power manager.
    private PowerManager powerManager = null;

    // The surface manager for the view.
    private InstrumentPanel audioInstrument = null;
    
    // Wake lock used to keep the screen alive.  Null if we aren't going
    // to take a lock; non-null indicates that the lock should be taken
    // while we're actually running.
    private PowerManager.WakeLock wakeLock = null;

    
}