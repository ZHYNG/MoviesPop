package com.example.fox.moviespop.app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
    private final String baseUrl="http://www.jycoder.com/json/Image/";
    private ArrayAdapter<String> mForecastAdapter;
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
            FetchMoviesPopTask moviesPopTask = new FetchMoviesPopTask();
            moviesPopTask.execute("popularity.desc");
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        for(int i=1;i<=18;i++){
            urls.add(baseUrl+i+".jpg");
        }

        String[] url_path={
                "http://www.jycoder.com/json/Image/1.jpg",
                "http://www.jycoder.com/json/Image/1.jpg",
                "http://www.jycoder.com/json/Image/1.jpg",
                "http://www.jycoder.com/json/Image/1.jpg",
                "http://www.jycoder.com/json/Image/1.jpg"
        };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(url_path));
        //Log.v("TEST DEBUG","msg" + "11111");
        Context context = getActivity();
        mForecastAdapter = new TestArrayAdapter(context,0, weekForecast);
        //Log.v("TEST DEBUG","msg" +"22222");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_forecast);
        //ImageAdapter imageAdapter = new ImageAdapter(context);
        //gridView.setAdapter(imageAdapter);
        Log.v("test debug","testdebug " + mForecastAdapter.toString());
        gridView.setAdapter(mForecastAdapter);
        return rootView;
    }

    public class FetchMoviesPopTask extends AsyncTask<String,Void,String[]>{
        private final String LOG_TAG = FetchMoviesPopTask.class.getSimpleName();

        private String[] getMoviesDataFromJson(String forecastJsonStr,int numMovies)
            throws JSONException {

            final String OWM_RESULT = "results";
            final String OWM_BACKGROUND_PATH = "backdrop_path";
            final String OWM_ORIGIN_TITLE = "original_title";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_ID = "id";
            final String OWM_OVERVIEW = "overview";

            //Log.v(LOG_TAG,"forecast entry here ");
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(2));
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(3));
            JSONArray moviesArray = forecastJson.getJSONArray(OWM_RESULT);
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(4));
            String[] resultStr = new String[numMovies];
            //Log.v(LOG_TAG,"moviesarry.length :" + String.valueOf(moviesArray.length()));
            for(int i=0;i<moviesArray.length();i++){
                String backdrop_path;
                String poster_path;

                JSONObject moviesForecast = moviesArray.getJSONObject(i);
                poster_path = moviesForecast.getString(OWM_POSTER_PATH);
                resultStr[i]=poster_path;
                //Log.v(LOG_TAG,"forecast entry : "+backdrop_path);
            }

            for(String s:resultStr){
               // Log.v(LOG_TAG,"forecast entry : "+s);
            }
            return resultStr;
        }

        @Override
        protected String[] doInBackground(String... params) {
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
                Log.v(LOG_TAG,"moviesarry " + forecastJsonStr);
                return getMoviesDataFromJson(forecastJsonStr,numMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(result != null){
               mForecastAdapter.clear();
            }
            for( String moviesForecastStr : result){
               mForecastAdapter.add(moviesForecastStr);

            }
        }

    }

    public class TestArrayAdapter extends ArrayAdapter<String>{
        private int resource;
        Context context;
        List<String> objects;
        public TestArrayAdapter(Context context, int resource, List<String> objects) {

            super(context, 0, objects);

            Log.v("test debug", "testdebug 11111");
            this.resource = resource;
            this.context =context;
            this.objects = objects;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public String getItem(int position) {
            return super.getItem(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            String url = "http://image.tmdb.org/t/p/w185/";

            ImageView imageView = new ImageView(context);
            Log.v("1111111","objectsdebug:   "+objects.get(position).toString());
            Picasso.with(context)
                    .load(url+objects.get(position).toString())
                    .into(imageView);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,800));
            return imageView;
        }

    }
    public class ImageAdapter extends BaseAdapter{
        private Context context;
        public ImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            Picasso.with(context)
                   .load("http://www.jycoder.com/json/Image/1.jpg")
                    .into(imageView);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setLayoutParams(new GridView.LayoutParams(480,800));


            return imageView;
            //return null;
        }
    }
}
