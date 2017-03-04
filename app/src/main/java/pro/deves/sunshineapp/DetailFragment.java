package pro.deves.sunshineapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pro.deves.sunshineapp.data.WeatherContract;

/**
 * Created by deves on 18/02/17.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;
    private static final String SHARE_STRING = "#sunshine";
    private static final int DETAIL_LOADER_ID = 0;
    public static final String DETAIL_URI = "detailUri";
    private Uri mUri;


    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESCRIPTION,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            // This works because the WeatherProvider returns location data joined with
            // weather data, even though they're stored in two different tables.
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    private static final int ID_INDEX = 0;
    private static final int DATE_INDEX= 1;
    private static final int DESC_INDEX = 2;
    private static final int MAX_INDEX = 3;
    private static final int MIN_INDEX = 4;
    private static final int HUMIDITY_INDEX = 5;
    private static final int WIND_INDEX = 6;
    private static final int DEGREES_INDEX = 7;
    private static final int PRESSURE_INDEX = 8;
    private static final int COLUMN_WEATHER_ID_INDEX = 9;
    private static final int LOCATION_SETTING_INDEX = 10;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null){
            mUri = bundle.getParcelable(DETAIL_URI);
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_view_icon);
        mDateView = (TextView) rootView.findViewById(R.id.detail_view_date);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_view_day);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_view_description);
        mHighTempView = (TextView) rootView.findViewById(R.id.detail_view_high);
        mLowTempView = (TextView) rootView.findViewById(R.id.detail_view_low);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_view_humidity);
        mWindView = (TextView) rootView.findViewById(R.id.detail_view_wind);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_view_pressure);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.share, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareIntent());
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        } else {
            Log.e(LOG_TAG, "mShareActionProvider is null");
        }
    }

    private Intent createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, SHARE_STRING);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.w(LOG_TAG, "In onCreateLoader: ");
        if (mUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()){
            return;
        }

        int imageResourceId = Utility.getArtResourceForWeatherCondition(data.getInt(COLUMN_WEATHER_ID_INDEX));

        boolean isMetric = Utility.isMetric(getActivity());

        long date = data.getLong(DATE_INDEX);
        String day = Utility.getDayName(getActivity(), date);
        String dateOfMonth = Utility.getFormattedMonthDay(date);
        String weatherDescription = data.getString(DESC_INDEX);

        String high = Utility.formatTemperature(getActivity(), data.getLong(MAX_INDEX), isMetric);
        String low = Utility.formatTemperature(getActivity(), data.getLong(MIN_INDEX), isMetric);

        String humidity = getActivity().getString(R.string.format_humidity, data.getDouble(HUMIDITY_INDEX));
        String wind = Utility.getFormattedWind(getActivity(), data.getFloat(WIND_INDEX), data.getFloat(DEGREES_INDEX));
        String pressure = getActivity().getString(R.string.format_pressure, data.getDouble(PRESSURE_INDEX));

        mIconView.setImageResource(imageResourceId);
        mFriendlyDateView.setText(day);
        mDateView.setText(dateOfMonth);
        mHighTempView.setText(high);
        mLowTempView.setText(low);
        mDescriptionView.setText(weatherDescription);
        mHumidityView.setText(humidity);
        mWindView.setText(wind);
        mPressureView.setText(pressure);

        if (mShareActionProvider != null){
            setShareIntent(createShareIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void onLocationChanged(String location){
        Uri uri = mUri;
        if (uri != null){
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(location, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER_ID, null, this);
        }
    }
}
