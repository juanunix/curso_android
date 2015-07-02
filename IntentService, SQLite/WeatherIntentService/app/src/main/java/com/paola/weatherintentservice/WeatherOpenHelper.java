package com.paola.weatherintentservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.paola.weatherintentservice.WeatherContract.WeatherEntry;
import com.paola.weatherintentservice.WeatherContract.LocationEntry;

public class WeatherOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "weather.db";

    private static final String LOCATION_TABLE_CREATE =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY, " +
                    LocationEntry.CITY_NAME + " TEXT NOT NULL, " +
                    LocationEntry.COUNTRY + " TEXT NOT NULL, " +
                    LocationEntry.CORD_LAT + " REAL NOT NULL, " +
                    LocationEntry.CORD_LONG + " REAL NOT NULL, " +

                    // To assure the table has just one city+country entry.
                    " UNIQUE (" + LocationEntry.CITY_NAME + ", " +
                    LocationEntry.COUNTRY + ") ON CONFLICT REPLACE);";

    private static final String WEATHER_TABLE_CREATE =
                "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                        WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                        WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                        WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                        WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +

                        // Set up the location column as a foreign key to location table.
                        " FOREIGN KEY (" + WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                        LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                        // To assure the application have just one weather entry per day
                        // per location, it's created a UNIQUE constraint with REPLACE strategy
                        " UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
                        WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

    WeatherOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOCATION_TABLE_CREATE);
        db.execSQL(WEATHER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}