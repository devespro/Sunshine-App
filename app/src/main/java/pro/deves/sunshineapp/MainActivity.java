package pro.deves.sunshineapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements ForecastFragment.Callback {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mLocation;
    private boolean mUnits;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocation = Utility.getPreferredLocation(this);
        mUnits = Utility.isMetric(this);
        if (findViewById(R.id.weather_detail_container) != null){
            mTwoPane = true;

            if (savedInstanceState == null) {
                Log.e(LOG_TAG, "onCreate: adding a detail fragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        ForecastFragment ff = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
        ff.setUseTodayLayout(!mTwoPane);
    }

    @Override
    protected void onResume() {
        String location = Utility.getPreferredLocation(this);
        if (!mLocation.equals(location)){
            ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            forecastFragment.onLocationChanged();
            mLocation = location;
        }

        if (!mUnits == (Utility.isMetric(this))){
            ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            forecastFragment.onUnitsChanged();
            mUnits = Utility.isMetric(this);
        }
        DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        if ( null != df ) {
            df.onLocationChanged(location);
            getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container, df).commit();
        }
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mTwoPane) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings : {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_show_map : {
                showLocationOnMap();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLocationOnMap() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        Uri uri = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location).build();
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else{
            Log.e(LOG_TAG, "onOptionsItemSelected: could not call " + uri);
        }
    }

    @Override
    public void onItemSelected(Uri dateUri) {
        Log.e(LOG_TAG, "onItemSelected: GOT URI " + dateUri);
        if (mTwoPane){
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, dateUri);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.setData(dateUri);
            startActivity(intent);
        }
    }
}