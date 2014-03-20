package com.heocompany.hpswine;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.heocompany.hpswine.HeoSQLite;

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
	        case R.id.submit_info:
	        	EditText idheo = (EditText) getActivity().findViewById(R.id.heoid_text);
	        	EditText weight = (EditText) getActivity().findViewById(R.id.heoweight_text);
	        	
	        	// save to SQLite
				HeoSQLite sqlite = new HeoSQLite(getActivity());
				SQLiteDatabase db = sqlite.getWritableDatabase();
//				db.rawQuery("INSERT INTO data_queue VALUES(?, ?)", new String[] {"http://google.com", "{id:" + id + ", weight:" + weight.getText() + "}"});

				ContentValues content = new ContentValues();
				content.put("url", "http://google.com"); 
				content.put("data", "{id:" + idheo.getText()
						+ ", weight:" + weight.getText()+ "}");
				if (db.insert("data_queue", null, content ) != -1) {
					Toast.makeText(getActivity(), "Saved weight for " + idheo, 1);
				}
	            break;
	        }
		
	}
    
}