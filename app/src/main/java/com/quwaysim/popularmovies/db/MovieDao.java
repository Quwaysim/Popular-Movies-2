package com.quwaysim.popularmovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.quwaysim.popularmovies.model.MovieDetails;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movieDetails_table ORDER BY mID")
    LiveData<MovieDetails[]> loadMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie (MovieDetails movieDetails);

    @Query("DELETE FROM movieDetails_table WHERE mID = :mID")
    void deleteMovie (String mID);

}
