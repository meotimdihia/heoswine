package com.heocompany.hpswine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

public class DashboardActivity extends FragmentActivity
	implements MenuFragment.onMenuSelectedListener {

	@Override
	public void onMenuSelected(String fragmentName) {
		Fragment newFragment = null;
		if (fragmentName.equals("DetectHeo")) {
			 newFragment = new DetectHeoFragment();
		} else if (fragmentName.equals("ReadUsbDevices")) {
			 newFragment = new ReadUsbDevicesFragment();
		}
	
		
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();		
		
	}

    
	private static final String TAG = "LogLogLog";
	private boolean mTwoPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		EditText idHeoInput  = (EditText) findViewById(R.id.editText1);
//		idHeoInput.setText("this is a text");
//		EditText idheoinput = (EditText) findViewById(R.id.editText2);
//		mEmailView.setText(mEmail);
//		idheoinput.setEnabled(false);
		
		setContentView(R.layout.activity_dashboard);
		if (findViewById(R.id.fragment_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
//			((MenuFragment) getSupportFragmentManager().findFragmentById(
//					R.id.menu_fragment))).setActivateOnItemClick(true);
		}
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fragment_container, new DetectHeoFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
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
		final EditText idHeoInput  = (EditText) this.findViewById(R.id.heoid_text);
		
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
