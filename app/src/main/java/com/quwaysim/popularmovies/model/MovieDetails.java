package com.quwaysim.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movieDetails_table")
public class MovieDetails implements Parcelable {
    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>() {
        public MovieDetails createFromParcel(Parcel src) {
            return new MovieDetails(src);
        }

        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    //Title, release date, movie poster, vote average, plot synopsis, ID, review and trailer.
    private String mMovieTitle;
    private String mReleaseDate;
    private String mMoviePoster;
    private String mVoteAverage;
    private String mSynopsis;

    @PrimaryKey(autoGenerate = true)
    private int dbMovieId;
    private String mID;
    private String mReview;
    private String mTrailer;

    public MovieDetails() {
    }

    public MovieDetails(Parcel parcel) {
        mMovieTitle = parcel.readString();
        mReleaseDate = parcel.readString();
        mMoviePoster = parcel.readString();
        mVoteAverage = parcel.readString();
        mSynopsis = parcel.readString();
        mID = parcel.readString();
        mReview = parcel.readString();
        mTrailer = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mMoviePoster);
        dest.writeString(mVoteAverage);
        dest.writeString(mSynopsis);
        dest.writeString(mID);
        dest.writeString(mReview);
        dest.writeString(mTrailer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        mReview = review;
    }

    public String getTrailer() {
        return mTrailer;
    }

    public void setTrailer(String trailer) {
        mTrailer = trailer;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        mMovieTitle = movieTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        mMoviePoster = moviePoster;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    @Override
    public String toString() {
        return "MovieDetails{" +
                "mMovieTitle='" + mMovieTitle + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mMoviePoster='" + mMoviePoster + '\'' +
                ", mVoteAverage='" + mVoteAverage + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mID='" + mID + '\'' +
                ", mReview='" + mReview + '\'' +
                ", mTrailer='" + mTrailer + '\'' +
                '}';
    }

    public int getDbMovieId() {
        return dbMovieId;
    }

    public void setDbMovieId(int dbMovieId) {
        this.dbMovieId = dbMovieId;
    }
}
