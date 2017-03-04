package pro.deves.sunshineapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by deves on 23/02/17.
 */
public class ForecastAdapter extends CursorAdapter {
    private Context mContext;
    private boolean mUseTodayLayout = true;
    //these values could not be bigger then the output of getItemViewType()
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE = 1;
    private static final String LOG_TAG = ForecastAdapter.class.getSimpleName();

    public ForecastAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) && mUseTodayLayout ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId;
        if (viewType == VIEW_TYPE_TODAY){
            layoutId = R.layout.list_item_forecast_today;
        } else {
            layoutId = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    public void setUseTodayLayout(boolean mUseTodayLayout) {
        this.mUseTodayLayout = mUseTodayLayout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
                break;
            }
            case VIEW_TYPE_FUTURE: {
                viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));
            }
        }
        String date = Utility.getFriendlyDayString(context, cursor.getLong(ForecastFragment.COL_WEATHER_DATE));
        viewHolder.dateView.setText(date);

        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.descriptionView.setText(description);

        boolean isMetric = Utility.isMetric(mContext);
        double maxTemperature = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);

        viewHolder.highTempView.setText(Utility.formatTemperature(mContext, maxTemperature, isMetric));

        double minTemperature = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);

        viewHolder.lowTempView.setText(Utility.formatTemperature(mContext, minTemperature, isMetric));
    }

    /**
     * Cache of the children views for a forecast list item
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }

    }

}
