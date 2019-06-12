package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import static com.example.popularmovies.MainActivity.MOVIE_DATA;

public class MovieActivity extends AppCompatActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();

    private static TextView mMoviePlot;
    private static ImageView mMovieImage;
    private static TextView mMovieReleaseDate;
    private static TextView mMovieRating;

    /**
     * On create method will get the data from the main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        // getting the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMovieImage = findViewById(R.id.iv_movie_image);
        mMoviePlot = findViewById(R.id.tv_movie_plot_text);
        mMovieReleaseDate = findViewById(R.id.tv_release_date_text);
        mMovieRating = findViewById(R.id.tv_user_rating_text);

        Intent intent = getIntent();

        Movie movie = intent.getParcelableExtra(MOVIE_DATA);

        // error handling maybe?
        Log.d(TAG, movie.toString());

        setTitle(movie.getTitle());
        Picasso.get().load(NetworkUtil.buildImagePath(movie.getPoster()).toString()).into(mMovieImage);
        mMoviePlot.setText(movie.getPlot());
        mMovieRating.setText(movie.getRating());
        mMovieReleaseDate.setText(movie.getRelease_date());

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
}
