package com.example.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.NetworkUtil;
import com.example.popularmovies.utils.jsonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.ListItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context ctx = MainActivity.this;
    private List<Movie> movies;
    RecyclerView rv;
    MoviesListAdapter adapter;

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
