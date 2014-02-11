package com.heocompany.hpswine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.Toast;


public class FireMissilesDialogFragment extends DialogFragment{
	
	protected static final String TAG = "LogLogLog";

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "resume=" );
		 getDialog().setOnKeyListener(new OnKeyListener()
		 {
		    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
		    	Log.v(TAG, "111onKeyup=" );
		    	return true;
		  }
		});
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setOnKeyListener(this);
	}
	
	private void setOnKeyListener(
			FireMissilesDialogFragment fireMissilesDialogFragment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.detect_heo, null));
    
	    return builder.create();
	}


}