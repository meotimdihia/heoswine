package com.heocompany.hpswine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DetectHeoFragment extends Fragment implements OnClickListener {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    	
    	View v = inflater.inflate(R.layout.detect_heo, container, false);
        Button b = (Button) v.findViewById(R.id.submit_info);
        b.setOnClickListener(this);
        
        return v;
    } 

	@Override
	public void onClick(final View v) {
        switch (v.getId()) {
	        case R.id.toggleRead:
//	        	boolean on = ((ToggleButton) v).isChecked();
//	        	if (on == false) {
//	        		readUsbTask.cancel(true);
//	        	} else {
//	        		readUsbTask = new ReadTemperatureDeviceTask().execute();
//	        	}
//	            break;
	        }
		
	}
    
//    HeoSQLite sqlite = new HeoSQLite(this);
//    SQLiteDatabase db = sqlite.getWritableDatabase();
//    db.rawQuery("INSERT INTO data_queue VALUES(?, ?)", new String[] {"http://google.com", "{data:1}"});
//    ContentValues content = new ContentValues();
//    content.put("url", "ldfsjsd");
//    content.put("data", "ldfsjsd");
//    db.insert("data_queue", null, content );
    
    
}