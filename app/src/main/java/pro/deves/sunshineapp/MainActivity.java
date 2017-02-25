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

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String FORECASTFRAGMENT_TAG = "forecastFragment";
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
        mLocation = Utility.getPrefferedLocation(this);
    }

    @Override
    protected void onResume() {
        if (!mLocation.equals(Utility.getPrefferedLocation(this))){
            ForecastFragment forecastFragment = (ForecastFragment) getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            forecastFragment.onLocationChanged();
            mLocation = Utility.getPrefferedLocation(this);
        }
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
            Log.e(LOG_TAG, "onOptionsItemSelected: could not call "  + uri);
        }
    }
}