package com.hitherejoe.mvpboilerplate.ui.main;

import com.hitherejoe.mvpboilerplate.ui.base.MvpView;

import org.jsoup.select.Elements;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showPokemon(List<String> pokemon);

    void showProgress(boolean show);

    void showError();

    void showFirstMovie(String movieTitle);

}