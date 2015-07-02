package com.paola.mylistviewapplication;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ListViewActivity extends AppCompatActivity {


    public static final String EXTRA_URL = "com.paola.weatherintentactivity.extra.STRING";
    DownloadResultReceiver mDownloadResultReceiver;
    ListView listView;
    ArrayAdapter arrayAdapter;
    AppCompatActivity contextActivity;
    Weather weather_data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = (ListView) findViewById(R.id.listView);
        checkConnection();
        contextActivity=this;

    }

    protected void checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {


            Intent msgIntent = new Intent(this, MyIntentService.class);
            msgIntent.putExtra(EXTRA_URL, "http://api.openweathermap.org/data/2.5/forecast/daily?q=Madrid&units=metric&cnt=14");
            startService(msgIntent);



        } else {
            Log.e(ListViewActivity.class.getSimpleName(), "No hay red disponible. Trate de habilitar la red para poder conectarse a internet");
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

            String [] tempStringArray = intent.getStringArrayExtra(MyIntentService.EXTRA_ARRAY_TEMP);
            String [] dateStringArray = intent.getStringArrayExtra(MyIntentService.EXTRA_ARRAY_DATE);

           //arrayAdapter = new ArrayAdapter(contextActivity, R.layout.list_item, R.id.textView, tempStringArray);//responseString) ;
            //listView.setAdapter(arrayAdapter);

            weather_data = new Weather[tempStringArray.length];
            for(int i = 0; i < weather_data.length; i++) {

                long unixSeconds = Long.parseLong(dateStringArray[i]);

                Date date = new Date(unixSeconds*1000L); // *1000 is to convert minutes to milliseconds
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM"); // the format of your date
                String dateString= sdf.format(date);

                weather_data[i]=  new Weather(R.drawable.ic_wb_sunny_black_24dp, dateString, tempStringArray[i]);

            }



            WeatherAdapter adapter = new WeatherAdapter(contextActivity,
                    R.layout.list_custom_item, weather_data);

            listView.setAdapter(adapter);

        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
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
