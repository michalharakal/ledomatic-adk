package com.ledomatic.adk;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ledomatic.adk.R;

public class BaseActivity extends LedomaticActivity {
	
	   private static BaseActivity instance;
	   
	
		public BaseActivity() {
			super();
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			instance = this;
			
		}
		
	    public static BaseActivity getInstance(){
	    	return instance;
		 }
	    
		public void onPlayBtnClick(View v) {
		}
		 

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			menu.add("Simulate");
			menu.add("Quit");
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (item.getTitle() == "Simulate") {
				showControls();
			} else if (item.getTitle() == "Quit") {
				finish();
				System.exit(0);
			}
			return true;
		}

		protected void enableControls(boolean enable) {
			if (enable) {
				showControls();
			} else {
				hideControls();
			}
		}

		protected void hideControls() {
			setContentView(R.layout.no_device);
		}

		protected void showControls() {
			setContentView(R.layout.main);

		}
	}