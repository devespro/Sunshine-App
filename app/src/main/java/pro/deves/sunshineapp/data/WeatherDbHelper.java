package pro.deves.sunshineapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static pro.deves.sunshineapp.data.WeatherContract.*;

/**
 * Created by deves on 19/02/17.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "weather.db";

    public WeatherDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + " (" +
              WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
              WeatherEntry.COLUMN_LOCATION_KEY + " INTEGER NOT NULL, " +
              WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
              WeatherEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
              WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +
              WeatherEntry.COLUMN_MIN_TEMPERATURE + " REAL NOT NULL, " +
              WeatherEntry.COLUMN_MAX_TEMPERATURE + " REAL NOT NULL, " +
              WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
              WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
              WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
              WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +
              " FOREIGN KEY (" + WeatherEntry.COLUMN_LOCATION_KEY + ") REFERENCES " +
              LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +
              " UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
              WeatherEntry.COLUMN_LOCATION_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LocationEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_LOCATION_SETTING + " TEXT UNIQUE NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG + " REAL NOT NULL);";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);
        db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Note that this only fires if you change the version number for your database.
        db.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
