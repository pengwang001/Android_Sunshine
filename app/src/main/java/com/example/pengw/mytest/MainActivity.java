package com.example.pengw.mytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    public final static String EXTRA_FORECAST_MESSAGE = "com.example.pengw.mytest.DetailedForecast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
        String csLogTag = this.getClass().getSimpleName();

        Log.d(csLogTag,"OnCreate Called");
    }


    @Override
    protected void onDestroy() {
        String csLogTag = this.getClass().getSimpleName();

        Log.d(csLogTag,"OnDestroy Called");

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        String csLogTag = this.getClass().getSimpleName();

        Log.d(csLogTag,"on Pause Called");
        super.onPause();
    }

    @Override
    protected void onResume() {
        String csLogTag = this.getClass().getSimpleName();

        Log.d(csLogTag,"OnResume Called");
        super.onResume();
    }

    @Override
    protected void onStop() {
        String csLogTag = this.getClass().getSimpleName();

        Log.d(csLogTag,"OnStop Called");
        super.onStop();
    }

    @Override
    protected void onStart() {
        String csLogTag = this.getClass().getSimpleName();

        Log.d(csLogTag,"OnStart Called");
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(
                    new Intent(this, com.example.pengw.mytest.SettingsActivity.class));
            return true;
        }
        else if (id == R.id.action_debug_test)
        {
            String csLogTag = this.getClass().getSimpleName();
            Log.d(csLogTag,"Internal Storage: "+getFilesDir().toString());
            Log.d(csLogTag,"External Private Storage: "+getApplicationContext().getExternalFilesDir(null).toString());
            Log.d(csLogTag,"External public Storage: "+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());

        }
        return super.onOptionsItemSelected(item);
    }




}
