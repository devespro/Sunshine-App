package pro.deves.sunshineapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by deves on 23/02/17.
 */
public class Utility {
    public static String getPrefferedLocation(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }

    public static boolean isMetric(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_default))
                .equals(context.getString(R.string.pref_units_default));
    }

    public static String formatTemperature(double temperature, boolean isFahrenheit){
        double temp;
        if (!isFahrenheit){
            temp = 9 * temperature/5 + 32;
        } else {
            temp = temperature;
        }
        return String.format("%.0f", temp);
    }

    public static String formatDate(long dateInMillis){
        Date date = new Date(dateInMillis);
        return DateFormat.getDateInstance().format(date);
    }
}
