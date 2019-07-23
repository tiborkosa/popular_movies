package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.popularmovies.adapters.MovieReviewAdapter;
import com.example.popularmovies.models.MovieReview;
import com.example.popularmovies.utils.NetworkUtil;
import com.example.popularmovies.utils.jsonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieReviewActivity extends AppCompatActivity {

    private final static String TAG = MovieReviewActivity.class.getSimpleName();
    private static final String MOVIE_REVIEWS = "reviews";
    public static final String MOVIE_ID = "movieId";
    private static final String MOVIE_REVIEW = "Movie Review";

    private static List<MovieReview> reviews;
    private final Context ctx = MovieReviewActivity.this;
    private RecyclerView rv;
    private MovieReviewAdapter adapter;
    private Spinner mSpinner;
    private TextView mError;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);

        setTitle(MOVIE_REVIEW);
        mSpinner = findViewById(R.id.sp_reviews);
        mError = findViewById(R.id.tv_error_review);
        rv = findViewById(R.id.rv_movie_review);

        // getting the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        movieId = intent.getStringExtra(MOVIE_ID);

        if( (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_REVIEWS) ) ||
                (savedInstanceState.containsKey(MOVIE_REVIEWS) &&
                        savedInstanceState.containsKey(MOVIE_ID) &&
                        !movieId.equals(savedInstanceState.getString(MOVIE_ID)))) {
            new ReviewDataTask().execute(NetworkUtil.buildUrl(movieId, NetworkUtil.REVIEW_PATH, ctx));
            Log.d(TAG, "Getting the data from web [ReadReview]");
        }
        else {
            reviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS);
            adapter = new MovieReviewAdapter(reviews);
            final LinearLayoutManager layoutManager = new  LinearLayoutManager(ctx);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);
            Log.d(TAG, "Loaded data from state [ReadReview]");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Saving the movie list state
     * @param outState state bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_REVIEWS, (ArrayList<? extends Parcelable>) reviews);
        outState.putString(MOVIE_ID, movieId);
        Log.d(TAG, "onSaveInstanceState [Reviews]");
        super.onSaveInstanceState(outState);
    }
    public class ReviewDataTask extends AsyncTask<URL, Void, String>{
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
                reviews = jsonUtil.pareseReviews(s);
                adapter = new MovieReviewAdapter(reviews );
                final LinearLayoutManager layoutManager = new  LinearLayoutManager(ctx);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String resp = null;
            try {
                Log.e(TAG, url.toString());
                resp = NetworkUtil.getResponseFromHttpUrls(url);
            } catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, "Unable to load reviews!");
            }
            return resp;
        }

        private void onStartLoad(){
            mSpinner.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }

        private void onError(){
            mSpinner.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            mError.setVisibility(View.VISIBLE);
        }

        private void onSuccess(){
            rv.setVisibility(View.VISIBLE);
            mError.setVisibility(View.GONE);
            mSpinner.setVisibility(View.GONE);
        }
    }
}
