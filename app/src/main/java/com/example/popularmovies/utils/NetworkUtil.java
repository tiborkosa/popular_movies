package com.example.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String API_KEY_STRING = "api_key";
    private static final String[] IMAGE_SIZES = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
    public static final String POPULAR = "movie/popular";
    public static final String TOP_RATED = "movie/top_rated";


    /**
     * This method is used to build the url for getting the main data
     * @param path  from where to get the data
     * @param ctx   you are calling from (used to get the api key from the properties file)
     * @return URL
     *
     * @exception IOException that is logged @link getAPIKey
     */
    public static URL buildUrl(String path, Context ctx) {
        Uri uri = getUri(path, ctx);

        return getURL(uri);
    }

    /**
     * Building the image path to retrieve image
     * @param imageName
     * @return URL
     */
    public static URL buildImagePath(String imageName) {

        Uri uri = getImageUri(imageName, -1);

        return getURL(uri);
    }

    /**
     * Building the image path specifying the image size we want to retrieve
     * @param imageName
     * @param index
     * @return URL
     */
    public static URL buildImagePath(String imageName, int index) {
        Uri uri = getImageUri(imageName, index);

        return getURL(uri);
    }

    /**
     * Building the Uri to get json data
     * @param path
     * @param ctx
     * @return Uri
     *
     * @exception IOException from @link getAPIKey that could return an empty string
     */
    private static Uri getUri(String path, Context ctx) {
        return Uri
                .parse(BASE_URL)
                .buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(API_KEY_STRING, getAPIKey(ctx))
                .build();
    }

    /**
     * Getting the API key from the property file
     *
     * @param ctx
     * @return string api key or empty string
     *
     * @exception IOException is logged and return empty string
     */
    private static String getAPIKey(Context ctx) {
        String apiKey = "";
        try {
            apiKey = Util.getProperty("API_KEY", ctx);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not get API key!");
        }
        return apiKey;
    }

    /**
     * Create a Uri for the image
     * @param path name of the image
     * @param index of the image size
     *              if index is out of bounce we get the normal image size of w185
     * @return Uri
     */
    private static Uri getImageUri(String path, int index) {
        String imageSize = IMAGE_SIZES[2];
        if (index > -1 && index < IMAGE_SIZES.length) {
            imageSize = IMAGE_SIZES[index];
        }
        return Uri
                .parse(BASE_IMAGE_URL)
                .buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(path)
                .build();
    }

    /**
     * creating a URL from the Uri
     * @param uri
     * @return URL or null in case of exception
     * @exception MalformedURLException
     */
    private static URL getURL(Uri uri) {
        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Could not parse uri!");
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Reaching out to the web to get the data
     * @param url from where we want to get the data from
     * @return json string
     * @throws IOException
     */
    public static String getResponseFromHttpUrls(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = connection.getInputStream();

            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");

            boolean hasInput = sc.hasNext();

            if (hasInput) {
                return sc.next();
            } else {
                Log.e(TAG, "No data returned from url!");
                return null;
            }

        } finally {
            connection.disconnect();
        }
    }
}
