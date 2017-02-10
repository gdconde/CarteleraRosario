package com.gdconde.cartelerarosario.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.data.model.MovieDetail;
import com.gdconde.cartelerarosario.ui.base.BaseActivity;
import com.gdconde.cartelerarosario.ui.common.ErrorView;
import com.gdconde.cartelerarosario.util.Util;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements DetailMvpView, ErrorView.ErrorListener {

    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";

    @Inject DetailPresenter mDetailPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_collapsing) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.image_backdrop) ImageView mBackdropImage;
    @BindView(R.id.layout_movie) NestedScrollView mMovieLayout;
    @BindView(R.id.text_genre) TextView mGenreText;
    @BindView(R.id.text_cinemas) TextView mCinemasText;
    @BindView(R.id.text_country) TextView mCountryText;
    @BindView(R.id.text_language) TextView mLanguageText;
    @BindView(R.id.text_release_date) TextView mReleaseDateText;
    @BindView(R.id.text_runtime) TextView mRuntimeText;
    @BindView(R.id.text_sinopsis) TextView mSinopsisText;
    @BindView(R.id.text_tagline) TextView mTaglineText;

    private String mMovieId;

    public static Intent getStartIntent(Context context, String movieId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
    public void showMovie(MovieDetail movie) {
        mMovieLayout.setVisibility(View.VISIBLE);
        mCollapsingToolbar.setTitle(movie.title);
        mCollapsingToolbar
                .setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780"+movie.backdropPath).into(mBackdropImage);

        mGenreText.setText(Util.genreTextToString(movie.genres));
        mCountryText.setText(String.format(
                "Pais: %1$s", Util.countryProductionsToString(movie.productionCountries)));
        mLanguageText.setText(String.format("Idioma original: %1$s", movie.originalLanguage));
        mReleaseDateText.setText(String.format("Fecha de estreno: %1$s", movie.releaseDate));
        mRuntimeText.setText(String.format("Duración: %1$s minutos", movie.runtime));
        mSinopsisText.setText(movie.overview);
        if(movie.tagline != null && !movie.tagline.isEmpty()) {
            mTaglineText.setText(movie.tagline);
        } else {
            mTaglineText.setVisibility(View.INVISIBLE);
        }
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