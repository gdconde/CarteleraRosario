package com.gdconde.cartelerarosario.ui.detail;

import com.gdconde.cartelerarosario.data.model.MovieDetail;
import com.gdconde.cartelerarosario.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showMovie(MovieDetail movie);

    void showProgress(boolean show);

    void showError();

}