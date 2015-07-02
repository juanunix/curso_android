package com.paola.mylistviewapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>

 * helper methods.
 */
public class MyIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String BROADCAST_ACTION = "com.paola.weatherintentservice.action.BROADCAST";

    public static final String EXTRA_ARRAY_TEMP = "com.paola.weatherintentservice.extra.STRING_TEMP";
    public static final String EXTRA_ARRAY_DATE = "com.paola.weatherintentservice.extra.STRING_DATE";



    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            String urlString = intent.getStringExtra(ListViewActivity.EXTRA_URL);

            HttpURLConnection urlConnection = null;
            BufferedReader reader= null;
            String downloadResult=null;

            try {

                URL url = new URL(urlString);
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
                Log.e(MyIntentService.class.getSimpleName(), "Error ", e);
            }
            finally {
                if(urlConnection !=null) urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(MyIntentService.class.getSimpleName(), "Error closing stream", e);
                    }
                }
            }

            String[][] result= getTempFromJSON(downloadResult);


            /*
     * Creates a new Intent containing a Uri object
     * BROADCAST_ACTION is a custom Intent action
     */
            Intent localIntent = new Intent(BROADCAST_ACTION);
            localIntent.putExtra(EXTRA_ARRAY_TEMP, result[0]);
            localIntent.putExtra(EXTRA_ARRAY_DATE, result[1]);

            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        }
    }

    private String[][] getTempFromJSON(String jsonString){
        final String LIST = "list";
        final String TEMPERATURE ="temp";
        final String DAY="day";
        final String DATE="dt";

        String [] [] result = null;

        try {
            String description;
            String temperature;

            JSONObject json = new JSONObject(jsonString);

            JSONArray daysArray = json.getJSONArray(LIST);
            result= new String[daysArray.length()][daysArray.length()];

            for(int i = 0; i < daysArray.length(); i++) {

                String day;
                String unix_date;
                // Get the JSON object representing the date
                unix_date = daysArray.getJSONObject(i).getString(DATE);

                // Get the JSON object representing the temp
                JSONObject tempObject = daysArray.getJSONObject(i).getJSONObject(TEMPERATURE);


                day = tempObject.getString(DAY);
                result[0] [i]=day + " ºC";
                result[1][i]= unix_date;
            }


        }catch(JSONException e){
            Log.e(MyIntentService.class.getSimpleName(), "Error", e);
        }
        return  result;

    }


}
