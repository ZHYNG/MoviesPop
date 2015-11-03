package com.example.fox.moviespop.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
public class MainActivityFragment extends Fragment {

    private ArrayList<String> urls=new ArrayList<>();
    private final String baseUrl="http://www.jycoder.com/json/Image/";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        for(int i=1;i<=18;i++){
            urls.add(baseUrl+i+".jpg");
        }

        Context context = getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_forecast);
        ImageAdapter imageAdapter = new ImageAdapter(context);
        gridView.setAdapter(imageAdapter);

        return rootView;
    }

    public class FetchMoviesPopTask extends AsyncTask<Void,Void,Void>{
        private final String LOG_TAG = FetchMoviesPopTask.class.getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/discover/movie" +
                        "?sort_by=popularity.desc&api_key=f2b1422d34675e8f82847ae21c7e9c31");
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


            return null;
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
            //imageView.setLayoutParams(new GridView.LayoutParams(280, 280));


            return imageView;
        }
    }
}
