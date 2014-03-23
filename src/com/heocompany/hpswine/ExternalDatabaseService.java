package com.heocompany.hpswine;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExternalDatabaseService extends IntentService {


	public ExternalDatabaseService() {
		super("ExternalDatabaseService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while(true) {
			synchronized (this) {
				try {
					HeoSQLite sqlite = new HeoSQLite(this);
					SQLiteDatabase db = sqlite.getWritableDatabase();
					Cursor cursor = db.rawQuery("SELECT * FROM data_queue", null);
					Log.e("LOGLOG", cursor.getString(cursor.getColumnIndex("data")));
				} catch (Exception e) { 
					Log.e("LogLog", e.getMessage());
				}
				try {
				    Thread.sleep(400);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
		}
	}

}