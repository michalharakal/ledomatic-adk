package com.ledomatic.adk.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public final class VisualizerView extends View {
	
	private int[] mColors;
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
		mColors = null;

		mForePaint.setStrokeWidth(1f);
		mForePaint.setAntiAlias(true);
	}

	public void updateVisualizer(int[] colors) {
		mColors = colors;
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

		if (mColors == null) {
			return;
		}

		mRect.set(0, 0, getWidth(), getHeight());
			for (int i = 0; i < 3; i++) {
			mForePaint.setColor(mColors[i]);
			canvas.drawRect(
					i * (mRect.width() / 3),
					0,
					i * (mRect.width() / 3) + (mRect.width() / 4),
					mRect.height(),
					mForePaint);
		}
	}
}
