package com.ledomatic.adk;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ledomatic.adk.widgets.VisualizerView;

public class LedomaticPhone extends BaseActivity implements OnClickListener {
	static final String TAG = "LedomaticPhone";
	
	private TextView text = null;
	private Button button = null;
	private VisualizerView mVisualizerView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			hideControls();
	}
	
	@Override
	protected void showControls() {
		super.showControls();

		text = (TextView) findViewById(R.id.text1);
		button = (Button) findViewById(R.id.button1);
		mVisualizerView = (VisualizerView) findViewById(R.id.ledomatic);
		mVisualizerView.setActivity(this);
		
		button.setText("Play sample");
	    text.setText("Capturing audio out...");
	   
	}
	
	@Override
	public void onPlayBtnClick(View v) {
		 if (mMediaPlayer.isPlaying()) {
             mMediaPlayer.pause();
             button.setText("Play sample");
             text.setText("Capturing audio out...");

         } else {
             mMediaPlayer.start();
             button.setText("Stop playing");
             text.setText("Playing audio...");

         }
	}

	public void onClick(View v) {

	}
}
