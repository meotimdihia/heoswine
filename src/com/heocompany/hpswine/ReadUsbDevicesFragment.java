package com.heocompany.hpswine;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.hardware.usb.UsbConstants;
=======
>>>>>>> 8b228747a94e3b41ed165053b0fc6b0e868e0097
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
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

<<<<<<< HEAD
	private byte[] bytes = {};
	private static int TIMEOUT = 2000;
	private boolean forceClaim = true;
=======
	protected static final String TAG = null;
	private static final String ACTION_USB_PERMISSION = "com.heocompany.hpswine.USB_PERMISSION";
	UsbManager mUsbManager;
	UsbDevice device = null;
>>>>>>> 8b228747a94e3b41ed165053b0fc6b0e868e0097
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.read_usb, container, false);
    }

    @Override
    public void onResume() {
    	super.onResume();

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        getActivity().registerReceiver(mUsbReceiver, filter);

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
<<<<<<< HEAD
	        
	        if (device != null) {    
			    manager.requestPermission(device, mPermissionIntent);
		    }
	        permission = manager.hasPermission(device);
=======
	        manager.requestPermission(device, mPermissionIntent);
>>>>>>> 8b228747a94e3b41ed165053b0fc6b0e868e0097
	        VID = Integer.toHexString(device.getVendorId()).toUpperCase();
	        PID = Integer.toHexString(device.getProductId()).toUpperCase();
		    break;
	    }
	    
	    
	    TextView descText = (TextView)getActivity().findViewById(R.id.description);
<<<<<<< HEAD
	    descText.setText(Html.fromHtml("	<strong>Device Number</strong>: " + devices.size() + "<br/>"
	    								+	"<strong>DeviceName</strong>: " + deviceName + "<br/>"
										+	"<strong>VID</strong>: " + VID + "<br/>"
										+	"<strong>PID</strong>: " + PID+ "<br/>"
										+	"<strong>Permission</strong>: " + permission ));
	    
	    UsbInterface intf = device.getInterface(0);
	    UsbEndpoint endpoint = intf.getEndpoint(0);
	    
	    UsbEndpoint epIN = null;
        UsbEndpoint epOUT = null;
        for (int i = 0; i < intf.getEndpointCount(); i++) {
            Log.e("LOG", "EP: "
                    + String.format("0x%02X", intf.getEndpoint(i)
                            .getAddress()));
            if (intf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
            	Log.e("LOG", "Bulk Endpoint");
                if (intf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
                    epIN = intf.getEndpoint(i);
                else
                    epOUT = intf.getEndpoint(i);
            } else {
            	Log.e("LOG", "Not Bulk");
            }
        }
        
	    UsbDeviceConnection connection = manager.openDevice(device); 
	    if (!connection.claimInterface(intf, forceClaim)) {
	    	Log.e("LOG", "Can not to clain interface");
	    }
	    Log.e("LOG", endpoint.toString());
   
	    
	    int data = connection.controlTransfer(0xC0, 0xFF, 0x370B, 0x0, bytes, bytes.length, TIMEOUT);

	    

		
	    connection.controlTransfer(0x41,0x0,0x1,0x0, bytes, 0, TIMEOUT);

		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT);

//		data = dev.read(0x81, endpoint.wMaxPacketSize, 0, 0x0);

		
        
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 00

		connection.controlTransfer(0x41,0x0B,0x01FD,0x0, bytes, 0, TIMEOUT); // Out

		connection.controlTransfer(0x41,0x12,0xF,0x0, bytes, 0, TIMEOUT); // Out

		connection.controlTransfer(0x41,0x0B,0x1FD,0x0, bytes, 0, TIMEOUT); // Out
		 
		connection.controlTransfer(0x41,0x0B,0x1FD,0x0, bytes, 0, TIMEOUT); // Out

		// Get temperature and wind speed 
		byte[] message = {80, 25, 00, 00};
        
		connection.controlTransfer(0x41,0x1E,0x1FD,0x0, message, message.length, 0); // Out 80 25 00 00
		 
		connection.controlTransfer(0x41,0x07,0x0202,0x0, bytes, 0, TIMEOUT); // Out
		 
		connection.controlTransfer(0x41,0x07,0x101,0x0, bytes, 0, TIMEOUT); // Out
		 
		connection.controlTransfer(0x41,0x03,0x800,0x0, bytes, 0, TIMEOUT); // Out
		
		byte[] message1 = {0x1A, 00, 00, 00, 0x11, 0x13};
		
		connection.controlTransfer(0x41,0x19,0x0,0x0, message1, message1.length, 0); // Out
		
		byte[] message2 = {0x1, 00, 00, 00, 0x4, 00, 00, 00, 0x01, 00, 00, 00, 0x01, 00, 00};
		connection.controlTransfer(0x41,0x13,0x0,0x0, message2, message2.length, 0); // Out
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0,bytes, 0, TIMEOUT); // In 03
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 03
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 03
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 03
		 
		connection.controlTransfer(0xC1,0x10,0x0,0x0, bytes, 0, TIMEOUT); // In 08, 00, ...
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 03
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 03
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 03
		 
		connection.controlTransfer(0xC1,0x8,0x0,0x0, bytes, 0, TIMEOUT); // In 03

//		dev.read(0x81, endpoint.wMaxPacketSize, 0, 1000) // 1 bytes 00
		 
//		dev.read(0x81, endpoint.wMaxPacketSize, 0, 1000) // 512 bytes = 0x200 
		 
		connection.controlTransfer(0x41,0x0B,0x01FD,0x0, bytes, 0, TIMEOUT); // Out
		 
		connection.controlTransfer(0xC1,0x10,0x0,0x0, bytes, 0, TIMEOUT); // In 04, 00, 00, 00, 00, 00, 00, 01, 00 ....
		
		
		byte[] buffer = new byte[endpoint.getMaxPacketSize()];
		while (true) {
		    byte[] buffer1 = new byte[1];
			data = connection.bulkTransfer(epIN, buffer1, 1, 100);
			Log.e("Log", Integer.toString(data));
			Log.e("Log", Arrays.toString(buffer1));			
			data = connection.bulkTransfer(epIN, buffer, endpoint.getMaxPacketSize(), 100);
			Log.e("Log", Arrays.toString(buffer));
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
    }
    
    private class ReadTemperatureDeviceTask extends AsyncTask<UsbDevice, Integer, Long> {

		@Override
		protected Long doInBackground(UsbDevice... params) {
			// TODO Auto-generated method stub
			return null;
		}
    	
  
    	
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
=======
	    descText.setText(Html.fromHtml("<strong>Device Number</strong>: " + devices.size() + "<br/>" +
	    								"<strong>DeviceName</strong>: " + deviceName + "<br/>" +
										"<strong>VID</strong>: " + VID + "<br/>" +
										"<strong>PID</strong>: " + PID+ "<br/>"));
	    

       
    }
    
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
>>>>>>> 8b228747a94e3b41ed165053b0fc6b0e868e0097
}