package com.hitherejoe.mvpboilerplate.ui.main;

import com.hitherejoe.mvpboilerplate.data.model.Movie;
import com.hitherejoe.mvpboilerplate.ui.base.MvpView;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public interface MainMvpView extends MvpView {

    void showMovies(ArrayList<Movie> movies);

    void showMovie(Movie movie);

    void showProgress(boolean show);

    void showError();

}