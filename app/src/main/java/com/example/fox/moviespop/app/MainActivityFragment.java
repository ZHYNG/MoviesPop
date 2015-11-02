package com.example.fox.moviespop.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
