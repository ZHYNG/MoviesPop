package com.example.fox.moviespop.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<String> urls=new ArrayList<>();
    private ArrayAdapter<MoviesInfo> mForecastAdapter;
    private MoviesInfo[] moviesInfo ={
            new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),
            new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),
            new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),
            new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),
            new MoviesInfo(),new MoviesInfo(),new MoviesInfo(),new MoviesInfo()};
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id== R.id.action_refresh){
            updateMoviesbyPopular();
            return true;
        }
        if(id== R.id.vote_average){
            updateMoviesByVote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<MoviesInfo> weekForecast = new ArrayList<MoviesInfo>(Arrays.asList(moviesInfo));
        //Log.v("TEST DEBUG","msg" + "11111");
        Context context = getActivity();
        mForecastAdapter = new ImageArrayAdapter(context,0, weekForecast);
        //Log.v("TEST DEBUG","msg" +"22222");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_forecast);

        gridView.setAdapter(mForecastAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             MoviesInfo forecast  = new MoviesInfo();
             forecast = mForecastAdapter.getItem(position);
                String foremessge = forecast.original_title;
               // Toast.makeText(getActivity(),foremsg,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),DetialActivity.class)
                        .putExtra(Intent.EXTRA_TEXT,foremessge);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateMoviesbyPopular(){
        FetchMoviesPopTask moviesPopTask = new FetchMoviesPopTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = prefs.getString(getString(R.string.action_refresh_key),
                getString(R.string.action_refresh_default));
        moviesPopTask.execute(sort);
    }
    public void updateMoviesByVote(){
        FetchMoviesPopTask moviesPopTask = new FetchMoviesPopTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = prefs.getString(getString(R.string.vote_average_key),
                getString(R.string.vote_average_default));
        moviesPopTask.execute(sort);
    }
    @Override
    public void onStart(){
        super.onStart();
        updateMoviesbyPopular();
    }
    public class FetchMoviesPopTask extends AsyncTask<String,Void,MoviesInfo[]>{
        private final String LOG_TAG = FetchMoviesPopTask.class.getSimpleName();

        private MoviesInfo[] getMoviesDataFromJson(String forecastJsonStr)
            throws JSONException {

            final String OWM_RESULT = "results";
            final String OWM_BACKGROUND_PATH = "backdrop_path";
            final String OWM_ORIGIN_TITLE = "original_title";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_ID = "id";
            final String OWM_OVERVIEW = "overview";
            //MoviesInfo[] moviesInfo = new MoviesInfo[numMovies];
            //Log.v(LOG_TAG,"forecast entry here ");
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(2));
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(3));
            JSONArray moviesArray = forecastJson.getJSONArray(OWM_RESULT);
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(4));
           // String[] resultStr = new String[numMovies];
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(moviesArray.length()));
            for (int i = 0; i < moviesArray.length(); i++) {
                String backdrop_path;
                String poster_path;
                String overview;
                String id;
                String original_title;
                JSONObject moviesForecast = moviesArray.getJSONObject(i);
                poster_path = moviesForecast.getString(OWM_POSTER_PATH);
                backdrop_path = moviesForecast.getString(OWM_BACKGROUND_PATH);
                id = moviesForecast.getString(OWM_ID);
                original_title =moviesForecast.getString(OWM_ORIGIN_TITLE);
                overview = moviesForecast.getString(OWM_OVERVIEW);
                //resultStr[i] = poster_path;
                moviesInfo[i].poster_path=poster_path;

                moviesInfo[i].backdrop_path=backdrop_path;
                moviesInfo[i].id=id;
                moviesInfo[i].overview=overview;
                moviesInfo[i].original_title=original_title;
                //Log.v(LOG_TAG,"forecast entry : "+backdrop_path);
            }


            return moviesInfo;
        }

        @Override
        protected MoviesInfo[] doInBackground(String... params) {
            if (params.length==0){
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;
            int numMovies = 20;
            try {
                final String FORECAST_BASE_URL= "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM = "sort_by";
                final String API_KEY = "api_key";
                Uri builtUri = null;
                builtUri =Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(API_KEY,BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();
                //URL url = new URL("http://api.themoviedb.org/3/discover/movie" +
                      //  "?sort_by=popularity.desc&api_key=f2b1422d34675e8f82847ae21c7e9c31");
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG,"BUILTURI  "+ builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream =urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null){
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }

                if(buffer.length()==0){
                    return null;
                }

                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG,"forecastTask"+forecastJsonStr);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                Log.v(LOG_TAG, "moviesarry " + forecastJsonStr);
                return getMoviesDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MoviesInfo[] moviesInfo) {
            if(moviesInfo != null){
               mForecastAdapter.clear();
            }
            for( MoviesInfo moviesForecast : moviesInfo){
               mForecastAdapter.add(moviesForecast);

            }
        }

    }
}
