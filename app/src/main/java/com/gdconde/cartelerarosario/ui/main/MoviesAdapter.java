package com.gdconde.cartelerarosario.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private ArrayList<Movie> mMovies;
    private ClickListener mClickListener;

    @Inject
    public MoviesAdapter() {
        mMovies = new ArrayList<>();
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
    }

    public boolean isMovieInList(Movie movie) {
        if(mMovies.contains(movie)) {
            int movieIndex = mMovies.indexOf(movie);
            if(!mMovies.get(movieIndex).cinemas.contains(movie.cinemas.get(0))) {
                mMovies.get(movieIndex).cinemas.add(movie.cinemas.get(0));
                notifyDataSetChanged();
            }
            return true;
        }
        return false;
    }

    public void addAll(ArrayList<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addMovie(Movie movie) {
        for(Movie movieId : mMovies) {
            if(movieId.id.equalsIgnoreCase(movie.id)) {
                if(!movieId.cinemas.contains(movie.cinemas.get(0))) {
                    movieId.cinemas.add(movie.cinemas.get(0));
                    notifyDataSetChanged();
                }
            }
        }
        mMovies.add(movie);
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.movie = movie;

        //Load data into UI
        holder.cinemasText.setText(Util.cinemasToString(movie.cinemas));
        holder.titleText.setText(movie.title);
        holder.sinopsisText.setText(movie.sinopsis);
        if(movie.genreIds != null && !movie.genreIds.isEmpty()) {
            holder.genreText.setText(Util.genreIdsToString(movie.genreIds));
        } else {
            holder.genreText.setVisibility(View.GONE);
        }
        if(movie.posterPath != null) {
            Glide.with(holder.posterImage.getContext()).load("https://image.tmdb.org/t/p/w154"+movie.posterPath).into(holder.posterImage);
            holder.posterImage.setVisibility(View.VISIBLE);
        } else {
            holder.posterImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title) TextView titleText;
        @BindView(R.id.text_cinemas) TextView cinemasText;
        @BindView(R.id.text_sinopsis) TextView sinopsisText;
        @BindView(R.id.image_poster) ImageView posterImage;
        @BindView(R.id.text_genre) TextView genreText;

        public Movie movie;

        public MovieViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) mClickListener.onMovieClick(itemView, movie);
                }
            });
        }
    }

    public interface ClickListener {
        void onMovieClick(View view, Movie movie);
    }

}
