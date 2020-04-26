package com.quwaysim.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String MOVIE_API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private final static String PARAM_QUERY = "api_key";
    //    private final static String PARAM_QUERY = "api_key";
    private final static String API_KEY = "";
    private static String TAG = "NetworkUtils";

    public static URL buildUrl(String sortType) {
        Uri builtUri = Uri.parse(MOVIE_API_BASE_URL).buildUpon().appendEncodedPath(sortType).appendQueryParameter(PARAM_QUERY, API_KEY).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.d(TAG, "buildUrl: " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewOrTrailerUrl(String id, String extraType) {
        Uri builtUri = Uri.parse(MOVIE_API_BASE_URL).buildUpon().appendEncodedPath(id).appendEncodedPath(extraType).appendQueryParameter(PARAM_QUERY, API_KEY).build();

        URL reviewOrTrailerUrl = null;
        try {
            reviewOrTrailerUrl = new URL(builtUri.toString());
            Log.d(TAG, "reviewOrTrailerUrl: " + reviewOrTrailerUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return reviewOrTrailerUrl;
    }

    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }
}
