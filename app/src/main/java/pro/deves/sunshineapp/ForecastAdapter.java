package pro.deves.sunshineapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import pro.deves.sunshineapp.data.WeatherContract;

/**
 * Created by deves on 23/02/17.
 */
public class ForecastAdapter extends CursorAdapter {
    private Context mContext;

    public ForecastAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view;
        textView.setText(convertCursorRowToUXFormat(cursor));
    }

    private String formatHighLows(double high, double low){
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    private String convertCursorRowToUXFormat(Cursor cursor){
        //get row indices for our cursor
        int max_tem_index = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE);
        int min_tem_index = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE);
        int date_index = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
        int short_descr_index = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESCRIPTION);

        String highAndLow = formatHighLows(
                cursor.getDouble(max_tem_index),
                cursor.getDouble(min_tem_index)
        );
        return Utility.formatDate(cursor.getLong(date_index)) + "-" + cursor.getString(short_descr_index) +
                "-" + highAndLow;
    }

}
