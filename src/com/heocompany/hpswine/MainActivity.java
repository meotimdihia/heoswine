package com.heocompany.hpswine;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        setContentView(R.layout.activity_main);
        
//        try {
//          
//          URL url = new URL("http://www.android.com/");
//          HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//     
//              
//              InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//   
//
//        } catch (Exception e) {
//          e.printStackTrace();
//      }
//        Log.e("Log", Integer.toString(response));
        startLoginForm();  
//        startDashboard();
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true; 
    }
    
    public void startLoginForm()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    
    public void startDashboard()
    {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }   
    
}
