package com.gdconde.cartelerarosario.ui.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.ui.base.BaseActivity;
import com.gdconde.cartelerarosario.ui.common.ErrorView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView, MoviesAdapter.ClickListener,
        ErrorView.ErrorListener {

    @Inject MoviesAdapter mMoviesAdapter;
    @Inject MainPresenter mMainPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.recycler_movies) RecyclerView mMoviesRecycler;
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
        mMoviesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMoviesRecycler.setAdapter(mMoviesAdapter);

        mErrorView.setErrorListener(this);

        mMainPresenter.getElCairoMovies();
        mMainPresenter.getShowcaseMovies();
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

        mMoviesRecycler.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovie(Movie movie) {
        mMoviesAdapter.addMovie(movie);
        mMoviesAdapter.notifyDataSetChanged();

        mMoviesRecycler.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMovieClick(String movie) {
        //TODO show movie detail activity
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            if (mMoviesRecycler.getVisibility() == View.VISIBLE
                    && mMoviesAdapter.getItemCount() > 0) {
                mSwipeRefreshLayout.setRefreshing(true);
            } else {
                mProgress.setVisibility(View.VISIBLE);

                mMoviesRecycler.setVisibility(View.GONE);
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
        mMoviesRecycler.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReloadData() {
        mMainPresenter.getElCairoMovies();
    }
}