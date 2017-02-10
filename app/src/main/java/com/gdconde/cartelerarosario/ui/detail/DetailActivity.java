package com.gdconde.cartelerarosario.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.ui.base.BaseActivity;
import com.gdconde.cartelerarosario.ui.common.ErrorView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements DetailMvpView, ErrorView.ErrorListener {

    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";

    @Inject DetailPresenter mDetailPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.image_backdrop) ImageView mBackdropImage;
    @BindView(R.id.layout_movie) NestedScrollView mMovieLayout;

    private String mMovieId;

    public static Intent getStartIntent(Context context, String movieId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mDetailPresenter.attachView(this);

        mMovieId = getIntent().getStringExtra(EXTRA_MOVIE_ID);
        if (mMovieId == null) {
            throw new IllegalArgumentException("Detail Activity requires a movie id@");
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mErrorView.setErrorListener(this);

        mDetailPresenter.getMovieDetails(mMovieId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.detachView();
    }

    @Override
    public void showMovie(Movie movie) {
        mMovieLayout.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w300"+movie.backdropPath).into(mBackdropImage);
    }

    @Override
    public void showProgress(boolean show) {
        mErrorView.setVisibility(View.GONE);
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError() {
        mMovieLayout.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReloadData() {
        mDetailPresenter.getMovieDetails(mMovieId);
    }
}