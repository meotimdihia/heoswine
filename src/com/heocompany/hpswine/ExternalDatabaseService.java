package com.heocompany.hpswine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ExternalDatabaseService extends IntentService {

	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public ExternalDatabaseService() {
		super("ExternalDatabaseService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		while(true) {
				try { 
					HeoSQLite sqlite = new HeoSQLite(this);
					final SQLiteDatabase db = sqlite.getWritableDatabase();
					String query = String
				            .format("SELECT rowid, * FROM data_queue");
					Cursor cursor = db.rawQuery(query , null);
					if (!cursor.moveToFirst()) {
						throw new Exception("Wait for new data");
					}
					
					do {
						final String rowid = cursor.getString(cursor.getColumnIndex("rowid"));
						String data = cursor.getString(cursor.getColumnIndex("data"));
						String url = cursor.getString(cursor.getColumnIndex("url"));
						
						Log.e("Logdata", rowid + "|" + url + "|" + data);
						try {
							JSONObject json = new JSONObject(data);
							Iterator<String> iter = json.keys();

	/*						RequestParams postdata = new RequestParams();
							while (iter.hasNext()) {
						        String key = iter.next();
					            Object value = json.get(key);
					            postdata.put(key, value);
						    }*/
							
							HttpClient client = new DefaultHttpClient();
							HttpPost post = new HttpPost(url);
							
							List<NameValuePair> formparams = new ArrayList<NameValuePair>();
							
							RequestParams postdata = new RequestParams();
							while (iter.hasNext()) {
						        String key = iter.next();
					            Object value = json.get(key);
	//				            postdata.put(key, value);
					            formparams.add(new BasicNameValuePair(key, value.toString()));
						    }
	
							UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
							HttpPost httppost = new HttpPost(url);
							httppost.setEntity(entity);
							
							HttpResponse response = client.execute(httppost);
							
							if (response.getStatusLine().getStatusCode() == 200) {
								Log.e("Log", "Sent data successful");
								db.delete("data_queue", "rowid=?", new String[] {rowid});
							} else {
								BufferedReader rd = new BufferedReader(
								        new InputStreamReader(response.getEntity().getContent()));
							 
								StringBuffer result = new StringBuffer();
								String line = "";
								while ((line = rd.readLine()) != null) {
									result.append(line);
								}
								Log.e("Log", result.toString());
								
							}
						} catch(Exception e) {
							db.delete("data_queue", "rowid=?", new String[] {rowid});
						}
					} while(cursor.moveToNext());
					

				} catch (Exception e) { 
					Log.e("Exception", e.getMessage());
					
				}
				try {
				    Thread.sleep(1000);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
		}
	}

}