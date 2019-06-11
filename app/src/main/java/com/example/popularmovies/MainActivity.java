package com.example.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.NetworkUtil;
import com.example.popularmovies.utils.jsonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.ListItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private final Context ctx = MainActivity.this;
    private static List<Movie> movies;

    // optimization to not get data from server if already loaded
    private static char sortedBy = 'P';
    RecyclerView rv;
    MoviesListAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
            How to check if async task already running and cancel it?
            In case of multiple menu clicks
         */
        switch (item.getItemId()){
            case R.id.sort_popular:
                if(sortedBy != 'P' || movies == null) {
                    sortedBy = 'P';
                    new MovieDataTask().execute(NetworkUtil.buildUrl( NetworkUtil.POPULAR, ctx));
                } else {
                    Toast.makeText(ctx, "List is already sorted by popularity!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.sort_rating:
                if(sortedBy != 'R' || movies == null) {
                    sortedBy = 'R';
                    new MovieDataTask().execute(NetworkUtil.buildUrl( NetworkUtil.TOP_RATED, ctx));
                }else {
                    Toast.makeText(ctx, "List is already sorted by rating!", Toast.LENGTH_LONG).show();
                }
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv_main);
        new MovieDataTask().execute(NetworkUtil.buildUrl( NetworkUtil.POPULAR, ctx));

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(ctx, clickedItemIndex +" 23", Toast.LENGTH_LONG).show();
        //TODO needs to be implemented
    }

    public class MovieDataTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String resp = null;
            try {
                resp = NetworkUtil.getResponseFromHttpUrls(url);
            } catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "Unable to get data!");
            }
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO show loading bar
        }

        @Override
        protected void onPostExecute(String s) {
           // super.onPostExecute(s);
            // TODO hide loading bar
            // TODO populate data

            movies = jsonUtil.parseMovies(s);
            adapter = new MoviesListAdapter(movies, MainActivity.this);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        }
    }

}
