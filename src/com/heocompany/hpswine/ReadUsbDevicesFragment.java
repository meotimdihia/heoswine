package com.heocompany.hpswine;


import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
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

	    Toast t = Toast.makeText(getActivity(), "Number of devices: " + devices.size(), Toast.LENGTH_LONG);
	    t.setDuration(200);
	    t.show();
	    // Iterate over all devices
	    Iterator<String> it = devices.keySet().iterator();
	    String VID = "";
	    String PID = "";
	    String deviceName = "";
	    
	    while (it.hasNext()) 
	    {
	    	deviceName = it.next();
	        UsbDevice device = devices.get(deviceName);

	        VID = Integer.toHexString(device.getVendorId()).toUpperCase();
	        PID = Integer.toHexString(device.getProductId()).toUpperCase();
	        t = Toast.makeText(getActivity(), deviceName + " " +  VID + ":" + PID + " " + manager.hasPermission(device), Toast.LENGTH_LONG);
	        t.setDuration(200);
		    t.show();
		    break;
	    }
	    
	    TextView descText = (TextView)getActivity().findViewById(R.id.description);
	    descText.setText(Html.fromHtml("<strong>Device Number</strong>: " + devices.size() + "<br/>" +
	    								"<strong>DeviceName</strong>: " + deviceName + "<br/>" +
										"<strong>VID</strong>: " + VID + "<br/>" +
										"<strong>PID</strong>: " + PID+ "<br/>"));
    }
}