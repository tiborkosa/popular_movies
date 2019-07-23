package com.example.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.popularmovies.models.Movie;

import java.util.List;

/**
 * The movie database DAO interface using Room
 */
@Dao
public interface MyRatedMoviesDao {

    /**
     * Query all movies
     * @return all movies using LiveData
     */
    @Query("SELECT * FROM movies ORDER BY id")
    LiveData<List<Movie>> loadAllMovies();

    /**
     * Insert new movie
     * @param movie to be inserted
     */
    @Insert
    void insertMovie(Movie movie);

    /**
     * Update existing moview
     * @param movie updated version
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    /**
     * Delete movie
     * @param movie to be deleted
     */
    @Delete
    void deleteMovie(Movie movie);

    /**
     * Query single movie by id
     * @param id movie id
     * @return return movie or null
     */
    @Query("SELECT * FROM movies WHERE id = :id")
    Movie findMovie(int id);

}
