package com.heocompany.hpswine;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import android.R.bool;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.loopj.android.http.*;

public class ReadUsbDevicesFragment extends Fragment implements OnClickListener {

	private byte[] bytes = {};
	private static int TIMEOUT = 2000;
	private boolean forceClaim = true;
	private static AsyncTask<Void, String, Void> readUsbTask;
	boolean needPermission = true;
	
	Long cts = System.currentTimeMillis()/1000;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.read_usb, container, false);
        ToggleButton b = (ToggleButton) v.findViewById(R.id.toggleRead);
        b.setOnClickListener(this);
        if (savedInstanceState != null) {
        	needPermission = false;
        }
        return v;
    }

    @Override
    public void onResume() {
    	super.onResume(); 

    	if (needPermission) {
	    	// get permission and read info temperature device
	    	getPermissionAndInfo();
    	}
    	if (checkPermission()) {
		    // run background read temperature info
	    	readUsbTask = new ReadTemperatureDeviceTask().execute();
    	}
    	// Turn on 
    	ToggleButton toggleRead = (ToggleButton) getActivity().findViewById(R.id.toggleRead);
    	toggleRead.setChecked(true);
    }

    public boolean checkPermission()
    {
    	UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
	    HashMap<String, UsbDevice> devices = manager.getDeviceList();
	    
	    Iterator<String> it = devices.keySet().iterator();
	    String deviceName = "";
	    UsbDevice device = null;
	    while (it.hasNext())
	    {
	    	deviceName = it.next();
	        device = devices.get(deviceName);
	    	if (device.getVendorId() == 0x10c4 && device.getProductId() == 0xea60) {
		        if (device != null) {
		        	if (manager.hasPermission(device)) {
		        		return true;
		        	}
			    }
	    	}
	    }
	    
	    return false;
    }
    
    private void getPermissionAndInfo()
    {
    	UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
	    // Get the list of attached devices
	    HashMap<String, UsbDevice> devices = manager.getDeviceList();
	    
	    // Iterate over all devices
	    Iterator<String> it = devices.keySet().iterator();
	    String VID = "";
	    String PID = "";
	    String deviceName = "";
	    UsbDevice device = null;
	    boolean permission = false;
	    boolean hasDevice = false;
	    while (it.hasNext())
	    {
	    	deviceName = it.next();
	        device = devices.get(deviceName);

	    	if (device.getVendorId() == 0x10c4 && device.getProductId() == 0xea60) {
	    		hasDevice = true;
		        
		        if (device != null) {
		        	if (!manager.hasPermission(device)) {
		        		PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
		        	    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		        	    getActivity().registerReceiver(this.mUsbReceiver, filter);
		        	    manager.requestPermission(device, mPermissionIntent);
		        	}
			    }
		        
		        permission = manager.hasPermission(device);
		        VID = Integer.toHexString(device.getVendorId()).toUpperCase();
		        PID = Integer.toHexString(device.getProductId()).toUpperCase();
			    break;
	    	}
	    }
	    
	    TextView descText = (TextView) getActivity().findViewById(R.id.description);
	    if (hasDevice) {
	    	descText.setText(Html.fromHtml("	<strong>Device Number</strong>: " + devices.size() + "<br/>"
	    								+	"<strong>DeviceName</strong>: " + deviceName + "<br/>"
										+	"<strong>VID</strong>: " + VID + "<br/>"
										+	"<strong>PID</strong>: " + PID+ "<br/>"
										+	"<strong>Permission</strong>: " + permission ));
	    } else {
	    	descText.setText(Html.fromHtml("<em>Can not find necessary device.</em>"));
	    }
    }
    
    private class ReadTemperatureDeviceTask extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			UsbDevice tDevice = null;
			UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
		    HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
		    Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

		    while (deviceIterator.hasNext()) {
		        UsbDevice device = deviceIterator.next();

		        if (device.getVendorId() == 0x10c4 && device.getProductId() == 0xea60) {
		        	tDevice = device;
		            break;
		        }
		    }
		    
		    if (tDevice == null || !usbManager.hasPermission(tDevice)) {
		    	return null;
		    }
		    

		    UsbInterface intf = tDevice.getInterface(0);
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
	        
		    UsbDeviceConnection connection = usbManager.openDevice(tDevice); 
		    if (!connection.claimInterface(intf, forceClaim)) {
		    	Log.e("LOG", "Can not to claim interface");
		    	System.exit(0);
		    }
		    Log.e("LOG", endpoint.toString());
	   
		    
		    connection.controlTransfer(0xC0, 0xFF, 0x370B, 0x0, new byte[1], 1, TIMEOUT);
			
		    connection.controlTransfer(0x41,0x0,0x1,0x0, new byte[0], 0, TIMEOUT);

			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[1], 1, TIMEOUT);

			connection.bulkTransfer(epIN, new byte[epIN.getMaxPacketSize()], epIN.getMaxPacketSize(), 100);	
	        
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[4], 4, TIMEOUT); // In 00
			
			connection.controlTransfer(0x41,0x0B,0x01FD,0x0, bytes, 0, TIMEOUT); // Out

			connection.controlTransfer(0x41,0x12,0xF,0x0, bytes, 0, TIMEOUT); // Out

			connection.controlTransfer(0x41,0x0B,0x1FD,0x0, bytes, 0, TIMEOUT); // Out
			 
			connection.controlTransfer(0x41,0x0B,0x1FD,0x0, bytes, 0, TIMEOUT); // Out

			// Get temperature and wind speed 
			byte[] message = {(byte) 0x80, 0x25, 00, 00};
			connection.controlTransfer(0x41,0x1E,0x1FD,0x0, message, message.length, 0); // Out 80 25 00 00
			 
			connection.controlTransfer(0x41,0x07,0x0202,0x0, bytes, 0, TIMEOUT); // Out
			 
			connection.controlTransfer(0x41,0x07,0x101,0x0, bytes, 0, TIMEOUT); // Out
			 
			connection.controlTransfer(0x41,0x03,0x800,0x0, bytes, 0, TIMEOUT); // Out
			
			byte[] message1 = {0x1A, 00, 00, 00, 0x11, 0x13};
			connection.controlTransfer(0x41,0x19,0x0,0x0, message1, message1.length, 0); // Out
			 
			byte[] message2 = {0x1, 00, 00, 00, 0x40, 00, 00, 00, 00, 0x01, 00, 00, 00, 0x01, 00, 00};
			connection.controlTransfer(0x41,0x13,0x0,0x0, message2, message2.length, 0); // Out
			 
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03
			 
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03
			 
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03
			 
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03
			 
			connection.controlTransfer(0xC1,0x10,0x0,0x0, new byte[14], 14, TIMEOUT); // In 08, 00, ...
			 
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03
			
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03
			 
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03
			 
			connection.controlTransfer(0xC1,0x8,0x0,0x0, new byte[2], 2, TIMEOUT); // In 03

			connection.bulkTransfer(epIN, new byte[epIN.getMaxPacketSize()], epIN.getMaxPacketSize(), 100);	 // 1 bytes 00
			 
			connection.bulkTransfer(epIN, new byte[epIN.getMaxPacketSize()], epIN.getMaxPacketSize(), 100);	 // 512 bytes = 0x200 			
			

			
			byte[] buffer = new byte[13];

        	// save to SQLite
			HeoSQLite sqlite = new HeoSQLite(getActivity());
			SQLiteDatabase db = sqlite.getWritableDatabase();
			
			while (true) { 
				try {
					if (isCancelled()) break;
					
					connection.controlTransfer(0x41,0x0B,0x01FD,0x0, bytes, 0, TIMEOUT); // Out
					 
					connection.controlTransfer(0xC1,0x10,0x0,0x0, new byte[epIN.getMaxPacketSize()], epIN.getMaxPacketSize(), TIMEOUT); // In 04, 00, 00, 00, 00, 00, 00, 01, 00 ....
						
					connection.bulkTransfer(epIN, buffer, 13, 300);
					
					char[] hexdata = bytesToHex(buffer);
	
					if (hexdata[0] == '0' && hexdata[1] == '0') {
						hexdata = Arrays.copyOfRange(hexdata, 2, hexdata.length);
					}
					
					// if this hexdata is shorten data then it is invalid
					if (new String(hexdata).length() == 26) {
					
						String rh = String.valueOf(
								(float) Integer.parseInt(new String(Arrays.copyOfRange(hexdata, 2, 6)), 16) / 10
						);
						
						// check if rh is valid number
						if (Float.parseFloat(rh) > 1000 || Float.parseFloat(rh) == 0) {
							throw new Exception("test");
						}
						int tempMode = Integer.parseInt(new String(Arrays.copyOfRange(hexdata, 0, 2)), 16); // 1 is C, 2 is F
						
						String temp = String.valueOf(
								(float) Integer.parseInt(new String(Arrays.copyOfRange(hexdata, 10, 14)), 16) / 10
						);
						
						int windSpeedDecimal = Integer.parseInt(new String(Arrays.copyOfRange(hexdata, 22, 24)), 16);
						int windSpeedMode = Integer.parseInt(new String(Arrays.copyOfRange(hexdata, 16, 18)), 16); // 1 is m/s, 2 km/h, 3 is mil/h, 4 is ft/m, 5 is ft/s, 6 is knots 
						
						
						String wind = String.valueOf(
								(float) Integer.parseInt(new String(Arrays.copyOfRange(hexdata, 18, 22)), 16) / Math.pow (10, (windSpeedDecimal - 1))
						);
			        	

						/*db.rawQuery("INSERT INTO data_queue VALUES(?, ?)", new String[] {"http://google.com", "{id:" + id + ", weight:" + weight.getText() + "}"});*/
						if ((System.currentTimeMillis()/1000 - cts) > 20) { 
							ContentValues content = new ContentValues();
							
							content.put("url", "http://sw.hongphucjsc.com/api/weather");
	 
							content.put("data","{id_site:" + 1
									+ ", humidity:"+ rh
									+ ", temperature:"+ temp
									+ ", wind_speed:"+ wind
									+ ", temp_mode:"+ tempMode
									+ ", wind_mode:"+ windSpeedMode
									+ "}");
							if (db.insert("data_queue", null, content ) != -1) {
								Log.e("Log", "Saved weather to queue");
							} else {
								Log.e("Log", "Can not Saved");
							}
							cts = System.currentTimeMillis()/1000;
						}
						publishProgress("RH: " + rh + " Wind Speed: " + wind + " Temperature: " + temp + " Temperature mode: " + tempMode + " WindSpeed mode: " + windSpeedMode);
						publishProgress(new String(hexdata));
						publishProgress("-----------------------------");
					}
				} catch (Exception e) {
					
				}

				try {
				    Thread.sleep(400);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				} 
			}

			return null;
		}
		
		final protected char[] hexArray = "0123456789ABCDEF".toCharArray();
		public char[] bytesToHex(byte[] bytes) {
		    char[] hexChars = new char[bytes.length * 2];
		    for ( int j = 0; j < bytes.length; j++ ) {
		        int v = bytes[j] & 0xFF;
		        hexChars[j * 2] = hexArray[v >>> 4];
		        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		    }
		    return hexChars;
		}

		public int[] bytesToInt(byte[] bytes) {
		    char[] hexChars = new char[bytes.length * 2];
		    int[] numbers =  new int[bytes.length];
		    for ( int j = 0; j < bytes.length; j++ ) {
		        int v = bytes[j] & 0xFF;
		        hexChars[j * 2] = hexArray[v >>> 4];
		        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//		        String(new char[]{hexChars[j * 2], hexChars[j * 2 + 1]});
		        numbers[j] = Integer.parseInt(new StringBuilder("").append(hexChars[j * 2]).append(hexChars[j * 2 + 1]).toString(), 16);
		    }
		    
		    return numbers;
		}
		
		@Override
		protected void onProgressUpdate(String... buffer) {
			super.onProgressUpdate(buffer);
			
			TableLayout tabledata = (TableLayout) getActivity().findViewById(R.id.measure_table);
			TableRow tr =  new TableRow(getActivity());
			TextView td = new TextView(getActivity());
			tr =  new TableRow(getActivity());
			td = new TextView(getActivity());
	        td.setText(Arrays.toString(buffer));
	        Log.e("Log", Arrays.toString(buffer));
			tr.addView(td);
			tabledata.addView(tr, 0);
		}

    }
    
    @Override
	public void onPause() {
        super.onPause();
        try {
        	getActivity().unregisterReceiver(mUsbReceiver);
        } catch(IllegalArgumentException e) {}
        
    }
    
    private static final String ACTION_USB_PERMISSION = "com.heocompany.hpswine.USB_PERMISSION";
    
	protected static final String TAG = null;
	
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();Log.e("log", "�");
	        if (ACTION_USB_PERMISSION.equals(action)) {
	            synchronized (this) {
	                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
	                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
	                    if(device != null){
	                    	//call method to set up device communication
	                    	// run background read temperature info
	                    	readUsbTask = new ReadTemperatureDeviceTask().execute();
	                   }
	                } 
	                else {
	                    Log.e(TAG, "permission denied for device " + device);
	                    Log.e("FD", "permission denied for device " + device);
	                }
	            }
	        }
	    }
	};

	@Override
	public void onClick(final View v) {
        switch (v.getId()) {
	        case R.id.toggleRead:
	        	boolean on = ((ToggleButton) v).isChecked();
	        	if (on == false) {
	        		readUsbTask.cancel(true);
	        	} else { 
	        		readUsbTask = new ReadTemperatureDeviceTask().execute();
	        	}
	            break;
        }
		
	}
	
}