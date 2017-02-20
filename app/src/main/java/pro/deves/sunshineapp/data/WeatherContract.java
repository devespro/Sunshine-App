package pro.deves.sunshineapp.data;

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class WeatherContract {

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate){
        Time time = new Time();
        time.setToNow();
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";

        public static final String COLUMN_LOCATION_SETTING = "location_setting";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
    }

    public static final class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_LOCATION_KEY  = "location_id";
        public static final String COLUMN_DATE  = "date";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_MIN_TEMPERATURE = "min";
        public static final String COLUMN_MAX_TEMPERATURE = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_DEGREES = "degrees";
    }
}
