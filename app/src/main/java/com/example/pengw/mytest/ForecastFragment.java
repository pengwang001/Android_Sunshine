package com.example.pengw.mytest;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */

public class ForecastFragment extends Fragment {
    private final String ParentLogTag = ForecastFragment.class.getSimpleName();

    private static ArrayAdapter<String> mForecastAdapter;
    public ForecastFragment() {
        // setHasOptionsMenu


    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}.
     * <p/>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(android.os.Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LogTag = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... queryinfo) {

            try{
                Context CurContext = getActivity();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CurContext);
                String csUnitType = pref.getString(getString(R.string.Pref_Metrics_Key),"metric");
                return WeatherDataParser.getWeatherDataFromJson(RetrieveForecastinJason(queryinfo),7, csUnitType);
            }
            catch (org.json.JSONException e) {
                Log.i(LogTag,"JSON exception! "+e.toString());
                return null;
            }

        }


        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            //Log.i(LogTag,"weather forecast xml retrieved!");
            /*for (String sTmp : s) {
                Log.i(LogTag, "Weather Entry: " + sTmp + "\n");
            }*/

            //Clear the data array to refill
            mForecastAdapter.clear();
            for (String sTmp : s) {
                mForecastAdapter.add(sTmp);
            }
        }

        /*
                 *Retrieve Forecast for 7 days from open weather map
                 * first para: city
                 * second para: unit type
                 */
        private String RetrieveForecastinJason(String []QueryParam) {


            String postcode =  QueryParam[0];

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                //construct the URI first

                //example URI: http://api.openweathermap.org/data/2.5/forecast/daily?q=k2w0c2&mode=json&units=metric&cnt=7
                //              http:/api.openweathermap.org/data/2.5/forecast/daily?q=k2w0c2&mode=json&units=metric&cnt=7
                Uri OpenWeatherMapURL = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily");

                String urlstr = OpenWeatherMapURL.buildUpon().appendQueryParameter("q",postcode)
                        .appendQueryParameter("mode","json")
                        .appendQueryParameter("units","metric")
                        .appendQueryParameter("cnt","7").toString();


                //Log.v(ParentLogTag, "URL:"+urlstr);

                //http://api.openweathermap.org/data/2.5/forecast/daily?q=k2w0c2&mode=json&units=metric&cnt=7
                //http://api.openweathermap.org/data/2.5/forecast/daily?q=k2w0c2&mode=json&units=metric&cnt=7
                URL url = new URL(urlstr);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LogTag, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LogTag, "Error closing stream", e);
                    }
                }
            }
            return forecastJsonStr;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList listWeather = new ArrayList();
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_forecast,
                        R.id.list_item_forecast_textview,
                        listWeather);
        ListView WeatherListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        WeatherListView.setAdapter(mForecastAdapter);

        //refresh the view
        SharedPreferences LocationPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String csLocationForForecast = LocationPref.getString(getActivity().getString(R.string.Pref_Location_Key),"");
        new FetchWeatherTask().execute(csLocationForForecast);

        WeatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent WeatherDetailsIntent = new Intent(getActivity(),ForecastDetails.class);
                WeatherDetailsIntent.putExtra(MainActivity.EXTRA_FORECAST_MESSAGE,mForecastAdapter.getItem(position).toString());
                startActivity(WeatherDetailsIntent);


            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==R.id.action_refresh)
        {
           /*if (BuildConfig.DEBUG) {
               Log.i(ParentLogTag, "User selects Refresh!");
           }*/
            SharedPreferences LocationPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String csLocationForForecast = LocationPref.getString(getActivity().getString(R.string.Pref_Location_Key),"");

            new FetchWeatherTask().execute(csLocationForForecast);
        }
        else if (item.getItemId()==R.id.action_showlocation)
        {
            SharedPreferences ForecastPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            String csLocation = ForecastPref.getString(getString(R.string.Pref_Location_Key),"Ottawa");

            Intent intentforMap = new Intent(Intent.ACTION_VIEW);
            String csGeoBase = "geo:0,0";
            //Uri geoURI = Uri.parse(csGeoBase).buildUpon().appendQueryParameter("q",csLocation).;


            intentforMap.setAction(Intent.ACTION_VIEW);
            intentforMap.setData(Uri.parse(csGeoBase).buildUpon().appendQueryParameter("q",csLocation).build());
            if (intentforMap.resolveActivity(getActivity().getPackageManager())!=null)
            {
                startActivity(intentforMap);
            }
        }
        return super.onOptionsItemSelected(item);
    }
 }

