package com.quwaysim.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quwaysim.popularmovies.model.MovieDetails;
import com.quwaysim.popularmovies.ui.DetailsActivity;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private MovieDetails[] mMovieDetails;
    private final Context mContext;

    public MovieAdapter(Context mContext, MovieDetails[] movieDetails) {
        mMovieDetails = movieDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {
        holder.bindMoviePoster(mMovieDetails[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("details", mMovieDetails[position]);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (mMovieDetails == null || mMovieDetails.length == 0) {
            return -1;
        }
        return mMovieDetails.length;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {


        ImageView mMoviePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_poster_rv_row);
        }

        public void bindMoviePoster(MovieDetails mMovieDetails) {
            Picasso.get().load(mMovieDetails.getMoviePoster()).into(mMoviePoster);

        }
    }
}
