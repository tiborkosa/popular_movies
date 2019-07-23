package com.example.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.popularmovies.models.Movie;

/**
 * Movie database creation using Room
 */
@Database(entities = { Movie.class}, version = 1, exportSchema = false)
public abstract class MyRatedMoviesDB extends RoomDatabase {

    private final static String TAG = MyRatedMoviesDB.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "myRatedMovies";
    private static MyRatedMoviesDB sInstance;

    /**
     * Get single instance of the database
     * @param context of the application
     * @return the single instance that was created
     */
    public static MyRatedMoviesDB getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(TAG,"Creating database instance.");

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MyRatedMoviesDB.class, MyRatedMoviesDB.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    /**
     * Abstract method of the database DAO
     * @return
     */
    public abstract MyRatedMoviesDao myRatedMoviesDao();
}
