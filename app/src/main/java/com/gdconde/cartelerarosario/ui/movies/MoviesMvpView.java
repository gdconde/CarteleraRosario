package com.gdconde.cartelerarosario.ui.movies;

import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.ui.base.MvpView;

import java.util.ArrayList;

/**
 * Created by gdconde on 2/3/17.
 */

public interface MoviesMvpView extends MvpView {

    void showMovies(ArrayList<Movie> movies);

    void addMovie(Movie movie);

    void showProgress(boolean show);

    void showProgress(String text);

    boolean isMovieInList(Movie movie);

    void updateMovie(Movie movie);
}
