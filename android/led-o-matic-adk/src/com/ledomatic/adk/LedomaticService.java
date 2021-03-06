package com.ledomatic.adk;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.ledomatic.adk.widgets.VisualizerView;

public class LedomaticService extends Service implements Runnable {

	private static final String TAG = "LedomaticService";    

	private static final String ACTION_USB_PERMISSION = "com.ledomatic.adk.action.USB_PERMISSION";

	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;

	private BaseActivity mLedomaticActivity;



	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;

	private Visualizer mVisualizer;

	private VisualizerView mVisualizerView;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		mLedomaticActivity = BaseActivity.getInstance();
		mLedomaticActivity.hideControls();
		showToast("onStartCommand");
		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}

		return 1;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		closeAccessory();
	}

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			showToast("Receive broadcast msg: "+action);
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};

	private void openAccessory(UsbAccessory accessory) {
		Log.d("openAccessory", "openAccessory");
		showToast("Open accessory: "+accessory);
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "LedMatrix");
			thread.start();
			Log.d(TAG, "accessory opened");
			showToast("accessory opened");
			mLedomaticActivity.showControls();
		} else {
			mLedomaticActivity.hideControls();
			Log.d(TAG, "accessory open fail");
			showToast("accessory open fail");
		}
	}

	private void closeAccessory() {
		mLedomaticActivity.hideControls();

		showToast("Close accessory");
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}

	@Override
	public void onCreate() {

		Log.d(TAG, "onCreate");

		showToast("Starting service");

		mVisualizerView = VisualizerView.getInstance();
		mVisualizer = new Visualizer(0);

		mVisualizer.setCaptureSize(512);

		mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {

			public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
			}

			public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
					int samplingRate) {
				//Log.d(TAG, "Captured " + bytes.length);
				updateMatrix(bytes);
			}

		}, Visualizer.getMaxCaptureRate(), false, true);

		mVisualizer.setEnabled(true);

		mUsbManager = UsbManager.getInstance(this);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);

		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
		registerReceiver(mUsbReceiver, filter);
	}

	public void showToast(String msg){
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	private byte[] mBytes;

	private void updateMatrix(byte[] bytes){
		mBytes = bytes;


		mVisualizerView = VisualizerView.getInstance();

		ArrayList<Double> mClearDouble = new ArrayList<Double>();

		// calc magnitude from data
		// see here abouta raw data description http://developer.android.com/reference/android/media/audiofx/Visualizer.html#getFft%28byte%5B%5D%29
		for (int i = 2; i <= 66; i = i + 2) {
			mClearDouble.add((double) 10
					* Math.log((mBytes[i] * mBytes[i]) + (mBytes[i + 1] * mBytes[i + 1])));
		}

		byte[] frame;
		frame = new byte[9];
		Arrays.fill(frame, (byte) 0);

		frame[0] = (byte)Math.ceil(mClearDouble.get(2));

		frame[4] =(byte)Math.ceil(mClearDouble.get(8));

		frame[8] = (byte)Math.ceil(mClearDouble.get(12));




		if (mVisualizerView != null) {
			int[] colors = new int[3];
			colors[0] = Color.rgb(frame[0], frame[1], frame[2]);
			colors[1] = Color.rgb(frame[3], frame[4], frame[5]);
			colors[2] = Color.rgb(frame[6], frame[7], frame[8]);

			if (mVisualizerView.isShown()){
				mVisualizerView.updateVisualizer(colors);
			}
		}
		sendCommand(frame);
	}

	public void sendCommand(byte[] buffer) {
		if (mOutputStream != null) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		closeAccessory();
		super.onDestroy();
	}

}

