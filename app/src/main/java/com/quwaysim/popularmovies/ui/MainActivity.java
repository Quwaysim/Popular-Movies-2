package com.quwaysim.popularmovies.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quwaysim.popularmovies.MovieAdapter;
import com.quwaysim.popularmovies.R;
import com.quwaysim.popularmovies.db.MovieDatabase;
import com.quwaysim.popularmovies.model.MainViewModel;
import com.quwaysim.popularmovies.model.MovieDetails;
import com.quwaysim.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_OF_COLUMNS = 2;
    private static final String LIST_STATE_KEY = "saved_instance";
    private final String popular = "popular";
    private final String topRated = "top_rated";
    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    private MenuItem menuItem;
    MovieDetails[] mMovieList;
    ProgressBar mProgressBar;
    String api = "popular";
    MovieDatabase mDB;
    RecyclerView.LayoutManager mLayoutManager;
    private int selectedItem;
    private String TAG = MainActivity.class.getSimpleName();
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.movies_rv);
        mRecyclerView.getRecycledViewPool().clear();
        mDB = MovieDatabase.getInstance(getApplicationContext());
        mLayoutManager = new GridLayoutManager(MainActivity.this, NUM_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt("OPTION");
        }

        if (isOnline()) {
            fetchMovies(api);
        } else {
            Toast.makeText(this, "No Internet Connection. App Needs Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("OPTION", selectedItem);
        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("LIST_STATE_KEY", mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        selectedItem = outState.getInt("OPTION");

        // Retrieve list state and list/item positions
        if (outState != null)
            mListState = outState.getParcelable("LIST_STATE_KEY");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        switch (selectedItem){
            case R.id.popular:
                menuItem = menu.findItem(R.id.popular);
                menuItem.setChecked (true);
                break;

            case R.id.top_rated:
                menuItem = menu.findItem(R.id.top_rated);
                menuItem.setChecked (true);
                break;

            case R.id.favourites:
                menuItem = menu.findItem(R.id.favourites);
                menuItem.setChecked (true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                selectedItem = item.getItemId();
                item.setVisible(true);
                sortedMovies(popular);
                return true;
            case R.id.top_rated:
                selectedItem = item.getItemId();
                item.setVisible(true);
                sortedMovies(topRated);
                return true;
            case R.id.favourites:
                selectedItem = item.getItemId();
                item.setVisible(true);
                setupViewModel();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void sortedMovies(String sortType) {
        if (isOnline()) {
            URL sortTypeUrl = NetworkUtils.buildUrl(sortType);
            mRecyclerView.getRecycledViewPool().clear();
            new FetchMovie().execute(sortTypeUrl);
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    void setupViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getMovieDetails().observe(this, new Observer<MovieDetails[]>() {
            @Override
            public void onChanged(MovieDetails[] movieDetails) {
                if (movieDetails.length == 0) {
                    Toast.makeText(getApplicationContext(), "No Movies in Favourites", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onChanged: updating List of Movies from LiveData in ViewModel");
                MovieAdapter mAdapterNew = new MovieAdapter(getApplicationContext(), movieDetails);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapterNew);
            }
        });
    }

    public void fetchMovies(String api) {
        URL apiURL = NetworkUtils.buildUrl(api);
        new FetchMovie().execute(apiURL);
    }

    //https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public class FetchMovie extends AsyncTask<URL, Void, String> {
        private final String TAG = "FetchMovieAsyncTask";

        private int mNoOfMovies;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String movieResults = null;
            try {
                movieResults = NetworkUtils.getResponseFromUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movieResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                mProgressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject parsedMoviesJSON = new JSONObject(s);
                    JSONArray moviesArray = parsedMoviesJSON.getJSONArray("results");

                    mNoOfMovies = moviesArray.length();
                    moviesArray.getString(1);
                    mMovieList = new MovieDetails[20];
                    for (int i = 0; i < mNoOfMovies; i++) {
                        MovieDetails mMovDetails = new MovieDetails();
                        String details = moviesArray.get(i).toString();
                        JSONObject detailsParsed = new JSONObject(details);
                        mMovDetails.setMoviePoster("http://image.tmdb.org/t/p/w185/"
                                + detailsParsed.getString("poster_path"));
                        mMovDetails.setMovieTitle(detailsParsed.getString("title"));
                        mMovDetails.setReleaseDate(detailsParsed.getString("release_date"));
                        mMovDetails.setSynopsis(detailsParsed.getString("overview"));
                        mMovDetails.setVoteAverage(detailsParsed.getString("vote_average"));
                        mMovDetails.setID(detailsParsed.getString("id"));
                        mMovieList[i] = mMovDetails;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter = new MovieAdapter(getApplicationContext(), mMovieList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                Toast.makeText(getApplicationContext(), "Movies Fetched", Toast.LENGTH_SHORT).show();
            }
        }
    }
}