package com.hitherejoe.mvpboilerplate.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitherejoe.mvpboilerplate.R;
import com.hitherejoe.mvpboilerplate.data.model.Movie;
import com.squareup.picasso.Picasso;

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

    public void addMovie(Movie movie) {
        if(mMovies.contains(movie)) {
            return;
        } else {
            mMovies.add(movie);
        }
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

        StringBuilder builder = new StringBuilder();

        //Create DateFormat to show timestamp as string
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.US);

        //TODO: create a method in utils to convert arraylist<timestamp> into a good looking String
        if(movie.schedule != null && !movie.schedule.isEmpty()) {
            for (Long timestamp : movie.schedule) {
                Date date = new Date(timestamp);
                builder.append(sdf.format(date));
            }
            holder.timeText.setText(builder.toString());
        }

        //Load data into UI
        holder.titleText.setText(movie.title);
        holder.sinopsisText.setText(movie.sinopsis);
        if(movie.posterPath != null) {
            Picasso.with(holder.posterImage.getContext()).load("https://image.tmdb.org/t/p/w154"+movie.posterPath).into(holder.posterImage);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title) TextView titleText;
        @BindView(R.id.text_time) TextView timeText;
        @BindView(R.id.text_sinopsis) TextView sinopsisText;
        @BindView(R.id.image_poster) ImageView posterImage;

        public Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mClickListener != null) mClickListener.onMovieClick(mMovies);
                }
            });
        }
    }

    public interface ClickListener {
        void onMovieClick(String movie);
    }

}
