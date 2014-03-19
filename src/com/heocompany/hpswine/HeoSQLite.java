package com.heocompany.hpswine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class HeoSQLite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    
    private static final String DICTIONARY_TABLE_CREATE =
                "CREATE TABLE data_queue (url TEXT, data TEXT);";

    HeoSQLite(Context context) {
        super(context, "heoswine", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.e("logloglog", "dsjljfsldfdlls"); 
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}