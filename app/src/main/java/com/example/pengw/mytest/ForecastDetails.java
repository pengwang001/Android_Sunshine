package com.example.pengw.mytest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;


public class ForecastDetails extends Activity {

    private ShareActionProvider m_SharedActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_details);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forecast_details, menu);
        MenuItem menuItemforShare = menu.findItem(R.id.action_share_action_provider);
        m_SharedActionProvider = (ShareActionProvider) menuItemforShare.getActionProvider();

        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        String csShare = getIntent().getStringExtra(MainActivity.EXTRA_FORECAST_MESSAGE)+"#SunshineApp";
        intentShare.putExtra(Intent.EXTRA_TEXT,csShare);
        intentShare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        m_SharedActionProvider.setShareIntent(intentShare);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_forecast_details, container, false);
            TextView DetailsTextView = (TextView) rootView.findViewById(R.id.DetailedForecast);
            Intent DetailIntent = getActivity().getIntent();

            DetailsTextView.setText(DetailIntent.getStringExtra(MainActivity.EXTRA_FORECAST_MESSAGE));
            return rootView;
        }
    }
}
