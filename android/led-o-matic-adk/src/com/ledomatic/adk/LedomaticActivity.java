package com.ledomatic.adk;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ledomatic.adk.R;

public class LedomaticActivity extends Activity {
	
	private static final String TAG = "LedomaticActivity";
	protected MediaPlayer mMediaPlayer;
    
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer = MediaPlayer.create(this, R.raw.gio2011);
        Log.d(TAG, "MediaPlayer audio session ID: " + mMediaPlayer.getAudioSessionId());
        mMediaPlayer.setLooping(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                // mVisualizer.setEnabled(false);
            }
        });

        Log.d("Service", "before");
        startService(new Intent(this, LedomaticService.class));
        Log.d("Service", "after");
    }
    
    
    public void showToast(String msg){
    	Context context = getApplicationContext();
    	CharSequence text = msg;
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }

        static final String HEXES = "0123456789ABCDEF";

    public static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
   
    @Override
    protected void onPause() {
        super.onPause();
      
        if (isFinishing() && mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    
    public void run() {
    	
    }
}

class VisualizerView extends View {
    private byte[] mBytes;
    // private float[] mPoints;
    private Rect mRect = new Rect();
    
    private static VisualizerView instance;
    
    protected LedomaticActivity mActivity;

    private Paint mForePaint = new Paint();

    public VisualizerView(LedomaticActivity activity) {
        super(activity);
        mActivity = activity;
        instance = this;
        init();
    }
    
    public static VisualizerView getInstance(){
    	return instance;
    }

    private void init() {
        mBytes = null;

        mForePaint.setStrokeWidth(1f);
        mForePaint.setAntiAlias(true);
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBytes == null) {
            return;
        }

 
        mRect.set(0, 0, getWidth(), getHeight());

        ArrayList<Double> mClearDouble = new ArrayList<Double>();

        for (int i = 2; i <= 66; i = i + 2) {

            mClearDouble.add((double) 10
                    * Math.log((mBytes[i] * mBytes[i]) + (mBytes[i + 1] * mBytes[i + 1])));
        }
        
        byte[] frame;
        frame = new byte[128];
        Arrays.fill(frame, (byte) 0);

        for (int i = 0; i < mClearDouble.size() - 1; i++) {

            int piece_h = (int) Math.ceil(mRect.height() / 16);

            int num_pieces = (int) Math.ceil(mClearDouble.get(i) / piece_h);
            
            int color = 0;

            //todo: change direction from 0 to 15
            for (int j = 16; j >= 1; j--) {
            //for (int j = 0; j <= 15; j++) {
                
                if (j <= num_pieces){

                    if (j >= 15) {
                        mForePaint.setColor(Color.rgb(255, 0, 0));
                        color = 2;
                    } else if (j >= 12) {
                        mForePaint.setColor(Color.rgb(255, 128, 0));
                        color = 3;
                    } else {
                        mForePaint.setColor(Color.rgb(0, 255, 128));
                        color = 1;
                    }
    
                    canvas.drawRect(
                            mRect.width() * i / (mClearDouble.size() - 1),
                            mRect.height() - (int) ((j - 1) * piece_h) - 1,
                            (mRect.width() * i / (mClearDouble.size() - 1)) + 10,
                            mRect.height() - (int) (j * piece_h),
                            mForePaint);
                }else{
                    color = 0;
                }
                
            }
        }
    }
}

