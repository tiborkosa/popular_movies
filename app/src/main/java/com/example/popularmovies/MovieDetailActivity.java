package com.example.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.popularmovies.adapters.MovieTrailerAdapter;
import com.example.popularmovies.db.MyRatedMoviesDB;
import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.MovieTrailer;
import com.example.popularmovies.utils.NetworkUtil;
import com.example.popularmovies.utils.jsonUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.popularmovies.MainActivity.MOVIE_DATA;
import static com.example.popularmovies.MovieReviewActivity.MOVIE_ID;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailerAdapter.ListItemClickeListener{

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static final String RATING_TEN = "/10";
    private static final String YOUTUBE_ADDRESS = "http://www.youtube.com/watch?v=";
    private static final String YOUTUBE_PROVIDER = "vnd.youtube:";
    private static final String TRAILERS = "trailers";

    private final Context ctx = MovieDetailActivity.this;
    private static List<MovieTrailer> trailers;

    private Spinner mSpinner;
    private TextView mError;
    private RecyclerView rv;
    private MovieTrailerAdapter adapter;

    private ImageButton mRatingButton;

    private Movie movie;
    private MyRatedMoviesDB db;

    private static final String ACTIVITY_TITLE = "MovieDetail";

    /**
     * Saving instance state
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRAILERS, (ArrayList<? extends Parcelable >) trailers);
        outState.putString(MOVIE_ID, String.valueOf(movie.getId()));
        Log.d(TAG, "Saving trailer instance.");
        super.onSaveInstanceState(outState);
    }

    /**
     * On create method will get the data from the main activity
     * Also will check if the trailers are saved in the state otherwise get it from the web
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(ACTIVITY_TITLE);

        // getting the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = MyRatedMoviesDB.getInstance(getApplication());

        TextView mMovieTitle = findViewById(R.id.movie_title);
        ImageView mMovieImage = findViewById(R.id.iv_movie_image);
        TextView mMoviePlot = findViewById(R.id.tv_movie_plot_text);
        TextView mMovieReleaseDate = findViewById(R.id.tv_release_date_text);
        TextView mMovieRating = findViewById(R.id.tv_user_rating_text);
        mRatingButton = findViewById(R.id.ib_rate_movie);
        mSpinner = findViewById(R.id.sp_trailer);
        mError = findViewById(R.id.tv_trailer_error);
        rv = findViewById(R.id.rv_trailer);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(MOVIE_DATA);

        mMovieTitle.setText(movie.getTitle());
        Picasso.get().load(NetworkUtil.buildImagePath(movie.getPoster()).toString()).into(mMovieImage);
        mMoviePlot.setText(movie.getPlot());
        mMovieRating.setText(movie.getRating() + RATING_TEN);
        mMovieReleaseDate.setText(movie.getRelease_date());

        if( (savedInstanceState == null || !savedInstanceState.containsKey(TRAILERS)) ||
                (savedInstanceState.containsKey(MOVIE_ID) && !savedInstanceState.getString(MOVIE_ID).equals(String.valueOf(movie.getId())))){
            new TrailerDataTask()
                    .execute(
                            NetworkUtil
                                    .buildUrl(Integer.toString(movie.getId()), NetworkUtil.TRAILER_PATH, ctx));
            Log.d(TAG, "Getting trailers from the web!");

            // checking if movie is favorite and setting the flag and star color
            AppExecutors.getInstance().diskIO().execute(() -> {
                Movie ratedMovie = db.myRatedMoviesDao().findMovie(movie.getId());
                if(ratedMovie != null) {
                    movie.setRated(true);
                }
            });
        } else {
            trailers = savedInstanceState.getParcelableArrayList(TRAILERS);
            adapter = new MovieTrailerAdapter(trailers, MovieDetailActivity.this );

            final LinearLayoutManager layoutManager = new  LinearLayoutManager(ctx);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);

            Log.d(TAG, "Loading trailers from the state!");
        }

        // update the favorite star button color
        checkRated();

        /**
         * Rating button listener
         * This will either save or delete the current movie from our DB
         */
        mRatingButton.setOnClickListener(view -> {
            if(movie.getRated()){
                //delete
                AppExecutors.getInstance().diskIO().execute(() -> {
                    db.myRatedMoviesDao().deleteMovie(movie);
                    mRatingButton.setColorFilter(getResources().getColor(R.color.colorPrimary));
                    movie.setRated(false);
                });
                Log.d(TAG, "onClick favorite deleted");
            } else {
                // insert new
                AppExecutors.getInstance().diskIO().execute(() -> {
                    db.myRatedMoviesDao().insertMovie(movie);
                    // updating the star color using UI thread to avoid crash
                    // NOTE: how do you test this? it did not crash while running emulator
                    runOnUiThread( () -> mRatingButton.setColorFilter(getResources().getColor(R.color.favorite_button)));

                    movie.setRated(true);
                });
                Log.d(TAG, "onClick favorite saved");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRated();
    }

    /**
     * checking if movie is in our local DB and update the rating button color accordingly
     */
    private void checkRated() {
        if(movie.getRated()) {
            Log.d(TAG, "get rated executed!");
            mRatingButton.setColorFilter(getResources().getColor(R.color.favorite_button));
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Trailer list item click listener
     * This will either play the trailer in the native YouTube app or the browser
     * @param clickedItemIndex
     */
    @Override
    public void onListeItemClick(int clickedItemIndex) {
        String key = trailers.get(clickedItemIndex).getKey();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PROVIDER + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_ADDRESS + key));
        try {
            ctx.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            ctx.startActivity(webIntent);
        }
    }

    /**
     * Lunching the ReviewActivity to read the reviews of the movie
     * @param view
     */
    public void readReviewClicked(View view){
        if(view.getId() == R.id.btn_read_reviews){

            Intent intent = new Intent(ctx, MovieReviewActivity.class);
            intent.putExtra(MOVIE_ID, String.valueOf(movie.getId()));
            startActivity(intent);
        }
    }

    /**
     * TrailerDataTask is used to reach out the web for the list of trailers
     */
    public class TrailerDataTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String resp = null;
            try {
                Log.e(TAG, url.toString());
                resp = NetworkUtil.getResponseFromHttpUrls(url);
            } catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "Unable to load trailers!");
            }
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onStartLoad();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s == null || s.equals("")){
                onError();
            }else {
                onSuccess();
                trailers = jsonUtil.pareseTrailers(s);
                adapter = new MovieTrailerAdapter(trailers, MovieDetailActivity.this );
                final LinearLayoutManager layoutManager = new  LinearLayoutManager(ctx);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }
        }

        /**
         * Show spinner on load start
         */
        private void onStartLoad(){
            mSpinner.setVisibility(View.VISIBLE);
        }

        /**
         * Show error message on error from the server
         */
        private void onError(){
            mSpinner.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            mError.setVisibility(View.VISIBLE);
        }

        /**
         * Show the RecycleView and hide the error and spinner
         */
        private void onSuccess(){
            rv.setVisibility(View.VISIBLE);
            mError.setVisibility(View.GONE);
            mSpinner.setVisibility(View.GONE);
        }
    }


}
