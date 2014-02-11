package com.heocompany.hpswine;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.utility.HttpRequest;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        try {
        	
	        URL url = new URL("http://www.android.com/");
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	   
	        	
	            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
	 

        } catch (Exception e) {
        	e.printStackTrace();
		}
//        Log.e("Log", Integer.toString(response));
        startLoginForm();  
    } 


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true; 
    }
    
    public void startLoginForm()
    {
    	Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
    }
    
    public void startDashboard()
    {
    	Intent intent = new Intent(this, DashboardActivity.class);
		startActivity(intent);
    }   
    
    
    private static final String ACTION_USB_PERMISSION =
    	    "com.android.example.USB_PERMISSION";
	protected static final String TAG = null;
	
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (ACTION_USB_PERMISSION.equals(action)) {
	            synchronized (this) {
	                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

	                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
	                    if(device != null){
	                      //call method to set up device communication
	                   }
	                } 
	                else {
	                    Log.d(TAG, "permission denied for device " + device);
	                }
	            }
	        }
	    }
	};
}
