package com.example.fox.moviespop.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetialActivityFragment extends Fragment {

    private  static final String LOG_TAG = DetialActivityFragment.class.getSimpleName();
    private  static final String FORECAST_SHARE_HASHTAG = "#MoviesAPP";
    private String mForecaststr;
    public DetialActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider
                =(ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider!=null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
        else{
            Log.d(LOG_TAG,"share Action provider is null");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootview = inflater.inflate(R.layout.fragment_detial, container, false);
        if(intent!=null&&intent.hasExtra(Intent.EXTRA_TEXT)){
             mForecaststr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootview.findViewById(R.id.origin_title_text))
                    .setText(mForecaststr);


        }
        return rootview;
    }

    private  Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,mForecaststr+FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
}
