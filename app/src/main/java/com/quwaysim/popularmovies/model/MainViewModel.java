package com.quwaysim.popularmovies.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.quwaysim.popularmovies.db.MovieDatabase;

public class MainViewModel extends AndroidViewModel {
    private LiveData<MovieDetails[]> movieDetails;
    private static final String TAG = MainViewModel.class.getSimpleName();



    public MainViewModel(@NonNull Application application) {
        super(application);

        MovieDatabase mDB = MovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "FavouritesViewModel: Actively Retrieving movies from the db");
        movieDetails = mDB.movieDao().loadMovies();
    }


    public LiveData<MovieDetails[]> getMovieDetails() {
        return movieDetails;
    }
}
