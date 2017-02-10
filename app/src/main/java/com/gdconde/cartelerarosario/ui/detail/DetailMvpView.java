package com.gdconde.cartelerarosario.ui.detail;

import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showMovie(Movie movie);

    void showProgress(boolean show);

    void showError();

}