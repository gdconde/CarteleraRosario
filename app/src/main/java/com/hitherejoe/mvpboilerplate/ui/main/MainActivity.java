package com.hitherejoe.mvpboilerplate.ui.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.hitherejoe.mvpboilerplate.R;
import com.hitherejoe.mvpboilerplate.data.model.Movie;
import com.hitherejoe.mvpboilerplate.ui.base.BaseActivity;
import com.hitherejoe.mvpboilerplate.ui.common.ErrorView;
import com.hitherejoe.mvpboilerplate.ui.detail.DetailActivity;
import com.hitherejoe.mvpboilerplate.util.DialogFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView, MoviesAdapter.ClickListener,
        ErrorView.ErrorListener {

    @Inject MoviesAdapter mMoviesAdapter;
    @Inject MainPresenter mMainPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.recycler_pokemon) RecyclerView mPokemonRecycler;
    @BindView(R.id.swipe_to_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);

        setSupportActionBar(mToolbar);

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primary);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.white);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mMainPresenter.getElCairoMovies();
                    }
                });

        mMoviesAdapter.setClickListener(this);
        mPokemonRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPokemonRecycler.setAdapter(mMoviesAdapter);

        mErrorView.setErrorListener(this);

//        mMainPresenter.getPokemon(POKEMON_COUNT);
        mMainPresenter.getElCairoMovies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    public void showMovies(ArrayList<Movie> movies) {
        mMoviesAdapter.setMovies(movies);
        mMoviesAdapter.notifyDataSetChanged();

        mPokemonRecycler.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovie(Movie movie) {
        mMoviesAdapter.addMovie(movie);
        mMoviesAdapter.notifyDataSetChanged();

        mPokemonRecycler.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieClick(String movie) {
        //TODO show movie detail activity
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            if (mPokemonRecycler.getVisibility() == View.VISIBLE
                    && mMoviesAdapter.getItemCount() > 0) {
                mSwipeRefreshLayout.setRefreshing(true);
            } else {
                mProgress.setVisibility(View.VISIBLE);

                mPokemonRecycler.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }

            mErrorView.setVisibility(View.GONE);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError() {
        mPokemonRecycler.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReloadData() {
        mMainPresenter.getElCairoMovies();
    }
}