package com.example.fox.moviespop.app;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fox on 2015/11/5.
 */
public class ImageArrayAdapter extends ArrayAdapter<MoviesInfo>{
    private int resource;
    Context context;
    //List<MoviesInfo> objects;

    public ImageArrayAdapter(Context context, int resource, List<MoviesInfo> moviesInfos) {

        super(context, 0, moviesInfos);

        Log.v("test debug", "testdebug 11111");
        this.resource = resource;
        this.context =context;
        //  this.objects = objects;
    }

    @Override
    public void add(MoviesInfo object) {
        super.add(object);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public MoviesInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String url = "http://image.tmdb.org/t/p/w185/";
        ImageView imageView = new ImageView(context);
        Log.v("1111111", "objectsdebug:   " + getItem(position).poster_path);
        Picasso.with(context)
                .load(url+getItem(position).poster_path)
                .into(imageView);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
        return imageView;
    }
}
