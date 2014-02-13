package com.heocompany.hpswine;


import java.util.HashMap;
import java.util.Iterator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReadUsbDevicesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.read_usb, container, false);
    }

    @Override
    public void onResume() {
    	super.onResume();

    	
	    UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
	    // Get the list of attached devices
	    HashMap<String, UsbDevice> devices = manager.getDeviceList();


	    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
	    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
	    getActivity().registerReceiver(mUsbReceiver, filter);
	    
	    // Iterate over all devices
	    Iterator<String> it = devices.keySet().iterator();
	    String VID = "";
	    String PID = "";
	    String deviceName = "";
	    UsbDevice device = null;
	    boolean permission = false;
	    
	    while (it.hasNext()) 
	    {
	    	deviceName = it.next();
	        device = devices.get(deviceName);
	        
	        if (device != null) {    
			    manager.requestPermission(device, mPermissionIntent);
		    }
	        permission = manager.hasPermission(device);
	        VID = Integer.toHexString(device.getVendorId()).toUpperCase();
	        PID = Integer.toHexString(device.getProductId()).toUpperCase();
		    break;
	    }
	    
	    
		    TextView descText = (TextView)getActivity().findViewById(R.id.description);
		    descText.setText(Html.fromHtml("	<strong>Device Number</strong>: " + devices.size() + "<br/>"
		    								+	"<strong>DeviceName</strong>: " + deviceName + "<br/>"
											+	"<strong>VID</strong>: " + VID + "<br/>"
											+	"<strong>PID</strong>: " + PID+ "<br/>"
											+	"<strong>Permission</strong>: " + permission ));
	    
    }
    
    private static final String ACTION_USB_PERMISSION =
    	    "com.heocompany.hpswine.USB_PERMISSION";
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