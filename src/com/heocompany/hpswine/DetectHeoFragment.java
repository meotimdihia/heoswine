package com.heocompany.hpswine;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
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
    	
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("highcore", 87);
        editor.commit();
        
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
                String idheovalue = idheo.getText().toString();
                String weightvalue = weight.getText().toString();
                Log.e("Log", idheovalue);
                Log.e("Log", weightvalue);
                if (idheovalue.equals("") || weight.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("You must fill inputs on this form!")
                           .setCancelable(false)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                    //do things
                               }
                           });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Log.e("DetectHeo", "Prepare to save data!");
                    // save to SQLite
                    HeoSQLite sqlite = new HeoSQLite(getActivity());
                    SQLiteDatabase db = sqlite.getWritableDatabase();
                    // db.rawQuery("INSERT INTO data_queue VALUES(?, ?)", new String[] {"http://google.com", "{id:" + id + ", weight:" + weight.getText() + "}"});
    
                    ContentValues content = new ContentValues();
                    content.put("url", "http://sw.hongphucjsc.com/api/update-profile/" + idheovalue); 
                    content.put("data", "{id:" + idheovalue
                            + ", weight:" + weightvalue + "}");
                    long result = db.insert("data_queue", null, content );
                    if (result != -1) {
                    	Toast.makeText(getActivity(), "Saved weight for heo ID: " + idheovalue, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        
    }
    
}