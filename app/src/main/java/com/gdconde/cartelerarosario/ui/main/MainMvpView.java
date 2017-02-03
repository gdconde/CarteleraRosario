package com.gdconde.cartelerarosario.ui.main;

import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.ui.base.MvpView;

import java.util.ArrayList;

public interface MainMvpView extends MvpView {

    void showMovies(ArrayList<Movie> movies);

    void showMovie(Movie movie);

    void showProgress(boolean show);

    void showError();

}