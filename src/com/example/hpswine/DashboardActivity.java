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
import android.widget.EditText;
import android.widget.Toast;

public class DashboardActivity extends Activity {

	private static final String TAG = "LogLogLog";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		EditText idHeoInput  = (EditText) findViewById(R.id.editText1);
//		idHeoInput.setText("this is a text");
//		EditText idheoinput = (EditText) findViewById(R.id.editText2);
//		mEmailView.setText(mEmail);
//		idheoinput.setEnabled(false);
		
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

//	@Override
//    public boolean onKeyUp(int keyCode, KeyEvent event)
//    {
//		final EditText idHeoInput  = (EditText) this.findViewById(R.id.editText1);
//		
//		Log.e(TAG, "Key" + keyCode);
//    	InputDevice input = event.getDevice();
//    	if (input != null) {
//	    	String descriptor = input.getDescriptor();
//	    	
//	    	if (descriptor.equals("0cd6a2f865b1f4e6ef3e296aeee58bc95f7574af")) {
//	    		
//		    	if (keyCode != 66) {
//		    		char key = (char)event.getUnicodeChar();
//		    		codeid += key;
//		    	} else {
//		    		
//		    		idHeoInput.setText(codeid);
//		    		Log.e(TAG, "Enter" );
//		    		codeid = "";
//		    	}
//		    	
//	    	}
//    	}
//    	return super.onKeyUp(keyCode, event);
//    }
	

	public boolean dispatchKeyEvent(KeyEvent event) {
		final EditText idHeoInput  = (EditText) this.findViewById(R.id.editText1);
		
		int keyCode = event.getKeyCode();
		char key = (char)event.getUnicodeChar();
		String skey = String.valueOf(keyCode);
		
    	InputDevice input = event.getDevice();
    	if (input != null) {
	    	String descriptor = input.getDescriptor();
	    	if (descriptor.equals("0cd6a2f865b1f4e6ef3e296aeee58bc95f7574af")) {
	    		if (event.getAction() == KeyEvent.ACTION_UP){
			    	if (keyCode != 66) {
	//		    		idHeoInput.setText(key);
			    		codeid += Character.toString(key);
			    		
			    	} else {
			    		idHeoInput.setText(codeid );
			    		Toast.makeText(this, "ID: " + codeid, Toast.LENGTH_LONG).show();
	//		    		idHeoInput.append("1");
	//		    		idHeoInput.append(codeid);
	//		    		final EditText idHeoInput  = (EditText) this.findViewById(R.id.editText1);
	//		    		idHeoInput.setText(codeid);
			    		Log.e(TAG, "Enter" );
			    		codeid = "";
			    	}
			    	
		    	}
	    		return true;
	    	}
    	}
    	return super.dispatchKeyEvent(event);
	}
}
