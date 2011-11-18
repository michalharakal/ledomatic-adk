package com.ledomatic.adk.widgets;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public final class VisualizerView extends View {
	
	private byte[] mBytes;
	// private float[] mPoints;
	private Rect mRect = new Rect();

	private static VisualizerView instance;

	protected Activity mActivity;

	private Paint mForePaint = new Paint();

	public VisualizerView(Activity activity) {
		super(activity);
		mActivity = activity;
		instance = this;
		init();
	}
	
	public void setActivity(Activity activity) {
		mActivity = activity;
		instance = this;
	}
	
	public VisualizerView(Context context) {
		super(context);
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
			
		setMeasuredDimension(widthSize, heightSize);
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

	
			for (int j = 16; j >= 1; j--) {
				if (j <= num_pieces){
					if (j >= 15) {
						mForePaint.setColor(Color.rgb(255, 0, 0));
					} else if (j >= 12) {
						mForePaint.setColor(Color.rgb(255, 128, 0));
					} else {
						mForePaint.setColor(Color.rgb(0, 255, 128));
					}

					canvas.drawRect(
							mRect.width() * i / (mClearDouble.size() - 1),
							mRect.height() - (int) ((j - 1) * piece_h) - 1,
							(mRect.width() * i / (mClearDouble.size() - 1)) + 10,
							mRect.height() - (int) (j * piece_h),
							mForePaint);
				}else{
				}

			}
		}
	}
}
