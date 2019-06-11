package com.example.popularmovies.utils;

import android.util.Log;

import com.example.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class jsonUtil {

    private static final String TAG = jsonUtil.class.getSimpleName();

    public static final String ID = "id";
    public static final String POSTER = "poster_path";
    public static final String TITLE = "title";
    public static final String PLOT = "overview";
    public static final String RATING = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "results";

    /**
     * Converting the result of the API call to a list of movies
     *
     * @param jsonString string from the API
     * @return list of the movies or null
     *
     * @exception JSONException this is logged and the value will be null
     */
    public static List<Movie> parseMovies(String jsonString){
        Log.v(TAG, jsonString);
        List<Movie> movies = new ArrayList<>();

        if(jsonString == null || jsonString.equals("")) return null;

        try {
            JSONObject json = new JSONObject(jsonString);

            if(json == null) return null;

            JSONArray jsonArray = json.optJSONArray(RESULTS);

            for ( int i =0; i < jsonArray.length(); i++) {
                Movie movie = parseMovie(jsonArray.optJSONObject(i));

                if(movie != null) movies.add(movie);
            }

        } catch (JSONException e) {
            Log.v(TAG, "Unable to parse json array");
            e.printStackTrace();
        }


        return movies;
    }

    /**
     * Parsing a json object and creating a movie object.
     *
     * Only the id field is int and others are string since we don't need to manipulate them.
     * I might update it if needed.
     *
     * @param jsonObject
     * @return newly created movie object
     */
    public static Movie parseMovie(JSONObject jsonObject) {

        int id = jsonObject.optInt(ID);
        String poster = jsonObject.optString(POSTER);
        String title = jsonObject.optString(TITLE);
        String plot = jsonObject.optString(PLOT);
        String rating = jsonObject.optString(RATING);
        String release_date = jsonObject.optString(RELEASE_DATE);

        return new Movie(id, poster, title, plot, rating, release_date);
    }

}
