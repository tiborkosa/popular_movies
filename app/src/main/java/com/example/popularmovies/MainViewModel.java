package com.example.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.popularmovies.db.MyRatedMoviesDB;
import com.example.popularmovies.models.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final LiveData<List<Movie>> movies;
    private static final String TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(@NonNull Application application) {
        super(application);
        MyRatedMoviesDB db = MyRatedMoviesDB.getInstance(this.getApplication());
        movies = db.myRatedMoviesDao().loadAllMovies();
        Log.d(TAG, "Retrieving movies from the db.");
        Log.d(TAG, movies.toString());
    }

    public LiveData<List<Movie>> getMovies(){
        return movies;
    }
}
