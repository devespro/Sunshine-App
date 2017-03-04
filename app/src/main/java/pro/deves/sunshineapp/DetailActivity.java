package pro.deves.sunshineapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by deves on 18/02/17.
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());
            DetailFragment df = new DetailFragment();
            df.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container, df)
                    .commit();

            }
    }
}
