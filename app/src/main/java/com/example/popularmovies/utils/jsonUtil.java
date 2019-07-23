package com.example.popularmovies.utils;

import android.util.Log;

import com.example.popularmovies.models.Movie;
import com.example.popularmovies.models.MovieReview;
import com.example.popularmovies.models.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class jsonUtil {

    private static final String TAG = jsonUtil.class.getSimpleName();

    private static final String ID = "id";
    private static final String POSTER = "poster_path";
    private static final String TITLE = "title";
    private static final String PLOT = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String RESULTS = "results";

    // trailers
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";
    private static final String TRAILER_TYPE = "type";

    // review
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";

    /**
     * Converting the result of the API call to a list of movies
     *
     * @param jsonString string from the API
     * @return list of the movies or null
     *
     * @exception JSONException this is logged and the value will be null
     */
    public static List<Movie> parseMovies(String jsonString){
        Log.d(TAG, "jsonString: "+jsonString);
        List<Movie> movies = new ArrayList<>();

        if(jsonString == null || jsonString.equals("")) return null;

        try {
            JSONObject json = new JSONObject(jsonString);

            if(json == null) return null;

            JSONArray jsonArray = json.optJSONArray(RESULTS);

            for ( int i =0; i < jsonArray.length(); i++) {
                Movie movie = createMovieObject(jsonArray.optJSONObject(i));

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
     * @param jsonObject json movie
     * @return newly created movie object
     */
    public static Movie createMovieObject(JSONObject jsonObject) {

        int id = jsonObject.optInt(ID);
        String poster = jsonObject.optString(POSTER);
        String title = jsonObject.optString(TITLE);
        String plot = jsonObject.optString(PLOT);
        String rating = jsonObject.optString(RATING);
        String release_date = jsonObject.optString(RELEASE_DATE);

        return new Movie(id, poster, title, plot, rating, release_date);
    }

    public static List<MovieTrailer> pareseTrailers(String jsonString){
        Log.d(TAG, "jsonString: "+jsonString);
        List<MovieTrailer> trailers = new ArrayList<>();

        if(jsonString == null || jsonString.equals("")) return null;

        try {
            JSONObject json = new JSONObject(jsonString);

            if(json == null) return null;

            JSONArray jsonArray = json.optJSONArray(RESULTS);

            for ( int i =0; i < jsonArray.length(); i++) {
                MovieTrailer trailer = createTrailerObject(jsonArray.optJSONObject(i));

                if(trailer != null) trailers.add(trailer);
            }

        } catch (JSONException e) {
            Log.v(TAG, "Unable to parse json array");
            e.printStackTrace();
        }


        return trailers;
    }

    private static MovieTrailer createTrailerObject(JSONObject jsonObject){
        int id = jsonObject.optInt(ID);
        String name = jsonObject.optString(TRAILER_NAME);
        String key = jsonObject.optString(TRAILER_KEY);
        String type = jsonObject.optString(TRAILER_TYPE);

        return  new MovieTrailer(id, name, key, type);
    }

    public static List<MovieReview> pareseReviews(String jsonString){
        Log.d(TAG, "jsonString: "+jsonString);
        List<MovieReview> reviews = new ArrayList<>();

        if(jsonString == null || jsonString.equals("")) return null;

        try {
            JSONObject json = new JSONObject(jsonString);

            if(json == null) return null;

            JSONArray jsonArray = json.optJSONArray(RESULTS);

            for ( int i =0; i < jsonArray.length(); i++) {
                MovieReview review = createReviewObject(jsonArray.optJSONObject(i));

                if(review != null) reviews.add(review);
            }

        } catch (JSONException e) {
            Log.v(TAG, "Unable to parse json array");
            e.printStackTrace();
        }


        return reviews;
    }

    private static MovieReview createReviewObject(JSONObject jsonObject){
        String author = jsonObject.optString(REVIEW_AUTHOR);
        String content = jsonObject.optString(REVIEW_CONTENT);

        return  new MovieReview(author, content);
    }
}
