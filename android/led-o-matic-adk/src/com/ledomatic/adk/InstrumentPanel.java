
/**
 * Audalyzer: an audio analyzer for Android.
 * <br>Copyright 2009-2010 Ian Cameron Smith
 *
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation (see COPYING).
 * 
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */


package com.ledomatic.adk;

import org.hermit.android.instruments.AudioAnalyser;
import org.hermit.android.instruments.InstrumentSurface;
import org.hermit.android.instruments.SpectrumGauge;
import org.hermit.dsp.Window;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;


/**
 * The main audio analyser view.  This class relies on the parent SurfaceRunner
 * class to do the bulk of the animation control.
 */
public class InstrumentPanel
extends InstrumentSurface
implements GestureDetector.OnGestureListener,
GestureDetector.OnDoubleTapListener
{


	// ******************************************************************** //
	// Constructor.
	// ******************************************************************** //

	/**
	 * Create a WindMeter instance.
	 * 
	 * @param	app			The application context we're running in.
	 */
	public InstrumentPanel(Activity app) {
		super(app, SURFACE_DYNAMIC);

		audioAnalyser = new AudioAnalyser(this);

		addInstrument(audioAnalyser);

		// On-screen debug stats display.
		statsCreate(new String[] { "µs FFT", "Skip/s" });

		//Gesture detection
		gesturedetector = new GestureDetector(this);
		gesturedetector.setOnDoubleTapListener(this);
		
		loadInstruments();
	}


	// ******************************************************************** //
	// Configuration.
	// ******************************************************************** //

	/**
	 * Set the sample rate for this instrument.
	 * 
	 * @param   rate        The desired rate, in samples/sec.
	 */
	public void setSampleRate(int rate) {
		audioAnalyser.setSampleRate(rate);
	}


	/**
	 * Set the input block size for this instrument.
	 * 
	 * @param   size        The desired block size, in samples.
	 */
	public void setBlockSize(int size) {
		audioAnalyser.setBlockSize(size);
	}


	/**
	 * Set the spectrum analyser windowing function for this instrument.
	 * 
	 * @param   func        The desired windowing function.
	 *                      Window.Function.BLACKMAN_HARRIS is a good option.
	 *                      Window.Function.RECTANGULAR turns off windowing.
	 */
	public void setWindowFunc(Window.Function func) {
		audioAnalyser.setWindowFunc(func);
	}


	/**
	 * Set the decimation rate for this instrument.
	 * 
	 * @param   rate        The desired decimation.  Only 1 in rate blocks
	 *                      will actually be processed.
	 */
	public void setDecimation(int rate) {
		audioAnalyser.setDecimation(rate);
	}


	/**
	 * Set the histogram averaging window for this instrument.
	 * 
	 * @param   rate        The averaging interval.  1 means no averaging.
	 */
	public void setAverageLen(int rate) {
		audioAnalyser.setAverageLen(rate);
	}


	/**
	 * Enable or disable stats display.
	 * 
	 * @param   enable        True to display performance stats.
	 */
	public void setShowStats(boolean enable) {
		setDebugPerf(enable);
	}


	/**
	 * Load instruments
	 * 
	 * @param   InstrumentPanel.Intruments        Choose which ones to display.
	 */
	private void loadInstruments() {  	
		Log.i(TAG, "Load instruments");

		//Stop surface update
		onPause();

		//Clear surface events
		clearGauges();

		//Clear analyse events
		audioAnalyser.resetGauge();

		spectrumGauge = audioAnalyser.getSpectrumGauge(this);	
		addGauge(spectrumGauge);
		//Load current layout in Gauges if they're already define 
		if ((currentWidth>0)&&(currentHeight>0))
			refreshLayout();

		//Restart
		onResume();    	

		Log.i(TAG, "End instruments loading");    	
	}


	// ******************************************************************** //
	// Layout Processing.
	// ******************************************************************** //

	/**
	 * Lay out the display for a given screen size.
	 * 
	 * @param   width       The new width of the surface.
	 * @param   height      The new height of the surface.
	 */
	@Override
	protected void layout(int width, int height) {
		//Save current layout
		currentWidth=width;
		currentHeight=height;
		refreshLayout();
	}


	/**
	 * Lay out the display for the current screen size.
	 */
	protected void refreshLayout() {   	
		// Make up some layout parameters.
		int min = Math.min(currentWidth, currentHeight);
		int gutter = min / (min > 400 ? 15 : 20);

		// Calculate the layout based on the screen configuration.
		if (currentWidth > currentHeight)
			layoutLandscape(currentWidth, currentHeight, gutter);
		else
			layoutPortrait(currentWidth, currentHeight, gutter);

		if (spectrumGauge!=null)
			spectrumGauge.setGeometry(specRect);
	}


	/**
	 * Lay out the display for a given screen size.
	 * 
	 * @param   width       The new width of the surface.
	 * @param   height      The new height of the surface.
	 * @param   gutter      Spacing to leave between items.
	 */
	private void layoutLandscape(int width, int height, int gutter) {      
		int x = gutter;
		int y = gutter;

		//Init
		specRect = new Rect(0,0,0,0);

		if (spectrumGauge!=null) { 
			specRect = new Rect(x, y, width - gutter, height - gutter);
		}
	}


	/**
	 * Lay out the display for a given screen size.
	 * 
	 * @param   width       The new width of the surface.
	 * @param   height      The new height of the surface.
	 * @param   gutter      Spacing to leave between items.
	 */
	private void layoutPortrait(int width, int height, int gutter) {
		int x = gutter;
		int y = gutter;

		specRect = new Rect(x, y, width - gutter, height - gutter);
	}


	// ******************************************************************** //
	// Input Handling.
	// ******************************************************************** //

	/**
	 * Handle key input.
	 * 
	 * @param	keyCode		The key code.
	 * @param	event		The KeyEvent object that defines the
	 * 						button action.
	 * @return				True if the event was handled, false otherwise.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}


	/**
	 * Handle touchscreen input.
	 * 
	 * @param	event		The MotionEvent object that defines the action.
	 * @return				True if the event was handled, false otherwise.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gesturedetector.onTouchEvent(event);
	}


	@Override
	public boolean onDown(MotionEvent e) {
		//True for propagation to onFling Event
		return true;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
	}


	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}


	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}


	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}


	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}


	// ******************************************************************** //
	// Save and Restore.
	// ******************************************************************** //

	/**
	 * Save the state of the panel in the provided Bundle.
	 * 
	 * @param   icicle      The Bundle in which we should save our state.
	 */
	protected void saveState(Bundle icicle) {
		//      gameTable.saveState(icicle);
	}


	/**
	 * Restore the panel's state from the given Bundle.
	 * 
	 * @param   icicle      The Bundle containing the saved state.
	 */
	protected void restoreState(Bundle icicle) {
		//      gameTable.pause();
		//      gameTable.restoreState(icicle);
	}


	// ******************************************************************** //
	// Class Data.
	// ******************************************************************** //

	// Debugging tag.
	private static final String TAG = "Audalyzer";


	// ******************************************************************** //
	// Private Data.
	// ******************************************************************** //

	//Gesture detection
	private GestureDetector gesturedetector = null;

	//Current layout
	private int currentWidth=0;
	private int currentHeight=0;

	// Our audio input device.
	private final AudioAnalyser audioAnalyser;

	// The gauges associated with this instrument.
	private SpectrumGauge spectrumGauge = null;

	// Bounding rectangles for the waveform, spectrum, sonagram, and VU meter displays.
	private Rect specRect = null;


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

}

