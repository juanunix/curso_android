package com.paola.weatherintentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WeatherIntentActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "com.paola.weatherintentactivity.extra.STRING";
    TextView myTextView;
    DownloadResultReceiver mDownloadResultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_intent);
        checkConnection();


        myTextView = (TextView) findViewById(R.id.textView2);
    }

    protected void checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {


            Intent msgIntent = new Intent(this, MyIntentService.class);
            msgIntent.putExtra(EXTRA_URL, "http://api.openweathermap.org/data/2.5/forecast/daily?q=Madrid&units=metric&cnt=7");
            startService(msgIntent);



        } else {
            Log.e(WeatherIntentActivity.class.getSimpleName(), "No hay red disponible. Trate de habilitar la red para poder conectarse a internet");
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter resultIntentFilter = new IntentFilter(MyIntentService.BROADCAST_ACTION);

        // Instantiates a new DownloadStateReceiver
        mDownloadResultReceiver =
                new DownloadResultReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mDownloadResultReceiver,
                resultIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadResultReceiver);
    }

    // Broadcast receiver for receiving results from the IntentService
    private class DownloadResultReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private DownloadResultReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {

            String [] responseString = intent.getStringArrayExtra(MyIntentService.EXTRA_ARRAY_STRING);
            String result= "";
            for(int i=0; i <responseString.length ;i++){
                result+=  responseString[i]+ " ºC\n";
            }
            myTextView.setText(result);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather_intent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
