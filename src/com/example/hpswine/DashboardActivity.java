package com.example.hpswine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class DashboardActivity extends Activity {

	private static final String TAG = "LogLogLog";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void detectIDheo(View v) {
		Log.v(TAG, "onKeyup=" );
		Toast.makeText(this, "ID: ", Toast.LENGTH_LONG).show();
		FireMissilesDialogFragment Fire = new FireMissilesDialogFragment ();
		Fire.show(getFragmentManager(), "Detect ID heo");
	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
	
	private static String codeid = "";
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
		Toast.makeText(this, "ID: ", Toast.LENGTH_LONG).show();
    	InputDevice input = event.getDevice();
    	Log.v(TAG, "onKeyup=" );
    	if (input != null) {
	    	String descriptor = input.getDescriptor();
	    	
//	    	if (descriptor.equals("0cd6a2f865b1f4e6ef3e296aeee58bc95f7574af")) {
	    		
		    	if (keyCode != 66) {
		    		char key = (char)event.getUnicodeChar();
		    		codeid += key;
		    	} else {
		    		Toast.makeText(this, "ID: " + codeid, Toast.LENGTH_LONG).show();
		    		codeid = "";
		    	}
//	    	}
    	}

    	return super.onKeyUp(keyCode, event);
    }
}
