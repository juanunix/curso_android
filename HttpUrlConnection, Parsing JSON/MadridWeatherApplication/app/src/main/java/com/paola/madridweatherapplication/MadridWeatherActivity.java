package com.paola.madridweatherapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.TextView;

public class MadridWeatherActivity extends AppCompatActivity {

    private TextView textViewDesc;

    private TextView textViewTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_madrid_weather);
        textViewDesc =(TextView) findViewById(R.id.textView);
        textViewTemp =(TextView) findViewById(R.id.textView2);
        checkConnection();
    }


    protected void checkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadTask().execute("http://api.openweathermap.org/data/2.5/weather?q=Madrid&units=metric");
        } else {
            Log.e(MadridWeatherActivity.class.getSimpleName(), "No hay red disponible. Trate de habilitar la red para poder conectarse a internet");
        }


    }

    private String[] getTempFromJSON(String jsonString){
        final String WEATHER = "weather";
        final String DESCRIPTION = "description";
        final String MAIN = "main";
        final String TEMPERATURE = "temp";

       String [] result = new String[2];

        try {
            String description;
            String temperature;

            JSONObject json = new JSONObject(jsonString);
            JSONArray weatherArray = json.getJSONArray(WEATHER);


            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            description = weatherObject.getString(DESCRIPTION);

            JSONObject mainObject = json.getJSONObject(MAIN);
            temperature=mainObject.getString(TEMPERATURE);

            result [0]= description;
            result[1]= temperature;

        }catch(JSONException e){
            Log.e(MadridWeatherActivity.class.getSimpleName(), "Error", e);
        }
        return  result;

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader= null;
            String downloadResult=null;

            try {

                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();


                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    StringBuffer buffer = new StringBuffer();

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                    }

                    if (buffer.length() != 0) {
                        downloadResult = buffer.toString();
                    }
                }
            }catch(Exception e){
                Log.e(MadridWeatherActivity.class.getSimpleName(), "Error ", e);
            }
            finally {
                if(urlConnection !=null) urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(MadridWeatherActivity.class.getSimpleName(), "Error closing stream", e);
                    }
                }
            }
            return downloadResult;
        }


        @Override
        protected void onPostExecute(String s) {
            if(s!=null) {
                Log.i(MadridWeatherActivity.class.getSimpleName(),s);
                String [] weather =   getTempFromJSON(s);
                textViewDesc.setText(weather[0]);
                textViewTemp.setText(weather[1] + " °C");

            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_madrid_weather, menu);
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
