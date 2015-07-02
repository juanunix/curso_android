package com.paola.weatherintentservice;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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

import com.paola.weatherintentservice.WeatherContract.WeatherEntry;
import com.paola.weatherintentservice.WeatherContract.LocationEntry;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 *
 * helper methods.
 */
public class MyIntentService extends IntentService {

    public static final String BROADCAST_ACTION = "com.paola.weatherintentservice.action.BROADCAST";

    public static final String EXTRA_ARRAY_STRING = "com.paola.weatherintentservice.extra.STRING_RESULT";

    private WeatherOpenHelper  dbHelper;
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            String urlString = intent.getStringExtra(WeatherIntentActivity.EXTRA_URL);

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

            String[] stringArrayResult= getTempFromJSON(downloadResult);
            writeToDB(stringArrayResult);
            String city_name = readCityFromDB();


            /*
     * Creates a new Intent containing a Uri object
     * BROADCAST_ACTION is a custom Intent action
     */
           Intent localIntent =
                    new Intent(BROADCAST_ACTION)
                            // Puts the status into the Intent
                            .putExtra(EXTRA_ARRAY_STRING, stringArrayResult);
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        }
    }

    private void writeToDB( String [] string){


         dbHelper = new WeatherOpenHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(WeatherContract.LocationEntry.CITY_NAME, "Madrid");
        values.put(WeatherContract.LocationEntry.COUNTRY, "ES");
        values.put(WeatherContract.LocationEntry.CORD_LAT, 40.4165);
        values.put(WeatherContract.LocationEntry.CORD_LONG, -3.70256);


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                LocationEntry.TABLE_NAME,
                null,
                values);


    }


    private String readCityFromDB(){

        dbHelper = new WeatherOpenHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.
        String[] projection = {
                WeatherContract.LocationEntry._ID,
                WeatherContract.LocationEntry.CITY_NAME,
        };


        Cursor c = db.query(
                WeatherContract.LocationEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        c.moveToFirst();
        return c.getString(1);

    }

    private String[] getTempFromJSON(String jsonString){
        final String LIST = "list";
        final String TEMPERATURE ="temp";
        final String DAY="day";

        String [] result = new String[7];

        try {
            String description;
            String temperature;

            JSONObject json = new JSONObject(jsonString);

            JSONArray sevenDaysArray = json.getJSONArray(LIST);

            for(int i = 0; i < sevenDaysArray.length(); i++) {

                String day;

                // Get the JSON object representing the day
                JSONObject tempObject = sevenDaysArray.getJSONObject(i).getJSONObject(TEMPERATURE);


                day = tempObject.getString(DAY);
                result[i]=day;
            }


        }catch(JSONException e){
            Log.e(MyIntentService.class.getSimpleName(), "Error", e);
        }
        return  result;

    }
}
