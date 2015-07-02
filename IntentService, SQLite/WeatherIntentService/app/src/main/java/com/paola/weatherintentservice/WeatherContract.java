 /*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paola.weatherintentservice;

import android.provider.BaseColumns;



 /**
 * Defines table and column names for the weather database.
 */
public class WeatherContract {

     // To prevent someone from accidentally instantiating the contract class,
     // give it an empty constructor.
     public WeatherContract() {}

    /*
        Inner class that defines the table contents of the location table
     */
    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String CITY_NAME = "city_name";
        public static final String COUNTRY = "country";
        public static final String CORD_LAT = "cord_lat";
        public static final String CORD_LONG = "cord_long";

    }

    /* Inner class that defines the table contents of the weather table */
    public static final class WeatherEntry implements BaseColumns {

        public static final String TABLE_NAME = "weather";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";

        // Description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_DESC = "desc";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        // Degrees are meteorological degrees (e.g, 0 is north, 180 is south).  Stored as floats.
        public static final String COLUMN_DEGREES = "degrees";
    }
}