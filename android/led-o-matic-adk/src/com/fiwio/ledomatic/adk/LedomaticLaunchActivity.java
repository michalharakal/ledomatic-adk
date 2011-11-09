package com.fiwio.ledomatic.adk;

import com.google.android.DemoKit.DemoKitPhone;
import com.google.android.DemoKit.DemoKitTablet;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

public class LedomaticLaunchActivity extends Activity {
	static final String TAG = "LedomaticLaunchActivity";

	static Intent createIntent(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int maxExtent = Math.max(display.getWidth(), display.getHeight());

		Intent intent;
		if (maxExtent > 1200) {
			Log.i(TAG, "starting tablet ui");
			intent = new Intent(activity, LedomaticTablet.class);
		} else {
			Log.i(TAG, "starting phone ui");
			intent = new Intent(activity, LedomaticPhone.class);
		}
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = createIntent(this);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, "unable to start DemoKit activity", e);
		}
		finish();
	}
}