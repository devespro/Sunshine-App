package pro.deves.sunshineapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import pro.deves.sunshineapp.data.WeatherContract;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ForecastAdapter mForecastAdapter;

    private static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private static final int LOADER_ID = 1;

    public static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESCRIPTION,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMPERATURE,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMPERATURE,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these must change.
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_LOCATION_SETTING = 5;
    public static final int COL_WEATHER_CONDITION_ID = 6;
    public static final int COL_COORD_LAT = 7;
    public static final int COL_COORD_LONG = 8;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final String locationSetting = Utility.getPrefferedLocation(getActivity());
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
               locationSetting, System.currentTimeMillis()
        );
        Cursor cursor = getActivity().getContentResolver().query(weatherLocationUri,
                FORECAST_COLUMNS,null,null,sortOrder);

        mForecastAdapter = new ForecastAdapter(getActivity(), cursor, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null){
                    String setting = Utility.getPrefferedLocation(getActivity());
                    Uri uri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(setting, cursor.getLong(COL_WEATHER_DATE));
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.setData(uri);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.transition.slide_in_right, android.R.anim.fade_out);
                }
            }
        });
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    void onLocationChanged(){
        updateWeather();
        getLoaderManager().restartLoader(LOADER_ID,null,this);
    }


    private void updateWeather(){
        Log.e(LOG_TAG, "updateWeather: CALLED" );
        String prefLocation = Utility.getPrefferedLocation(getActivity());
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        weatherTask.execute(prefLocation);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String locationSetting = Utility.getPrefferedLocation(getActivity());

        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting,
                System.currentTimeMillis()
        );

        return new CursorLoader(getActivity(), weatherForLocationUri, FORECAST_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(
                LOADER_ID,
                null,
                this
        );
        super.onActivityCreated(savedInstanceState);
    }
}