package com.example.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.adapters.MoviesListAdapter;
import com.example.popularmovies.db.MyRatedMoviesDB;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.NetworkUtil;
import com.example.popularmovies.utils.jsonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.ListItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String MOVIE_DATA = "movie_data";

    private final Context ctx = MainActivity.this;
    private static List<Movie> movies;

    // optimization to not get data from server if already loaded
    private static char sortedBy = 'P';

    private RecyclerView rv;
    private MoviesListAdapter adapter;
    private ProgressBar mLoader;
    private TextView mError;
    private TextView mNoFavorites;

    //private MyRatedMoviesDB db;
    /**
     * Creating the main activity
     * We will load the saved state if exists or load the new data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyRatedMoviesDB db = MyRatedMoviesDB.getInstance(getApplication());

        rv = findViewById(R.id.rv_main);
        mLoader = findViewById(R.id.pb_main);
        mError = findViewById(R.id.tv_error_main);
        mNoFavorites = findViewById(R.id.tv_no_favorites);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            new MovieDataTask().execute(NetworkUtil.buildUrl( NetworkUtil.POPULAR_PATH, ctx));
            Log.d(TAG, "Getting the data from web");
        }
        else {
            movies = savedInstanceState.getParcelableArrayList("movies");
            adapter = new MoviesListAdapter(movies, MainActivity.this);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns()));
            Log.d(TAG, "Loaded data from state");
        }

    }

    /**
     * Inflating the Main menu
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    /**
     * Menu item clicked handler for retrieving different movie list
     * @param item menu item clicked
     * @return true or super optionItemSelected(item)
     */
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
                    new MovieDataTask().execute(NetworkUtil.buildUrl( NetworkUtil.POPULAR_PATH, ctx));
                    setTitle("Popular Movies");
                } else {
                    Toast.makeText(ctx, "List is already sorted by popularity!", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.sort_rating:
                if(sortedBy != 'R' || movies == null) {
                    sortedBy = 'R';
                    new MovieDataTask().execute(NetworkUtil.buildUrl( NetworkUtil.TOP_RATED_PATH, ctx));
                    setTitle("Rated Movies");
                }else {
                    Toast.makeText(ctx, "List is already sorted by rating!", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.menu_favorites:
                sortedBy = 'F';
                setTitle("My Favorite Movies");
                MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
                viewModel.getMovies().observe(this, (fMovies) -> {
                        if(sortedBy == 'F'){
                            Log.d(TAG, "Updating list of movies from LiveData in ViewModel.");
                            movies = fMovies;
                            adapter = new MoviesListAdapter(fMovies, MainActivity.this);
                            rv.setAdapter(adapter);
                            rv.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns()));
                            if(movies.size() > 0){
                                mNoFavorites.setVisibility(View.GONE);
                            } else {
                                // show no favorites
                                mNoFavorites.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                );

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Saving the movie list state
     * @param outState state bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) movies);
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    /**
     * Implementation of the MovieListAdapter onListItemClick interface
     * This will navigate to another page where we show movie details
     * @param clickedItemIndex index of the item that was clicked
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(ctx, MovieDetailActivity.class);
        intent.putExtra(MOVIE_DATA, movies.get(clickedItemIndex));
        startActivity(intent);
    }

    /**
     * Asynchronous task to pull data from the server
     */
    public class MovieDataTask extends AsyncTask<URL, Void, String>{

        /**
         * Async background task
         * @param urls from where we want to get the data from
         * @return the json string
         */
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

        /**
         * Before we reach out to the server show loading bar
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onStartLoad();
        }

        /**
         * After the server response either create a layout or show error
         * @param s json string from server
         */
        @Override
        protected void onPostExecute(String s) {
            if(s == null || s.equals("")){
                onError();
            } else {
                onSuccess();
                movies = jsonUtil.parseMovies(s);
                adapter = new MoviesListAdapter(movies, MainActivity.this);
                rv.setAdapter(adapter);

                rv.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns()));
            }
        }
    }

    /**
     * Determine if its landscape or portrait mode
     * If landscape show three columns if portrait show two
     * @return number of columns
     */
    private int numberOfColumns(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 3;
        return 2;
    }

    /**
     *  In case of error from the server
     *  show error message and hide progressbar
     */
    private void onError(){
        mLoader.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.VISIBLE);
    }

    /**
     * After successfully returned the data from server
     * set progressbar and error invisible
     */
    private void onSuccess(){
        mLoader.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.INVISIBLE);
    }

    /**
     * Before reaching out to the server show loading bar but not the error
     */
    private void onStartLoad(){
        mLoader.setVisibility(View.VISIBLE);
        mError.setVisibility(View.INVISIBLE);
    }

}
