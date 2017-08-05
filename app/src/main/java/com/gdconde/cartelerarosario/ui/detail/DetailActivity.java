package com.gdconde.cartelerarosario.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.MovieDetail;
import com.gdconde.cartelerarosario.ui.base.BaseActivity;
import com.gdconde.cartelerarosario.ui.common.ErrorView;
import com.gdconde.cartelerarosario.util.Util;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements DetailMvpView, ErrorView.ErrorListener {

    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";
    public static final String EXTRA_CINEMAS = "EXTRA_CINEMAS";

    @Inject DetailPresenter mDetailPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_collapsing) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.image_backdrop) ImageView mBackdropImage;
    @BindView(R.id.layout_movie) NestedScrollView mMovieLayout;
    @BindView(R.id.text_genre) TextView mGenreText;
    @BindView(R.id.text_cast) TextView mCastText;
    @BindView(R.id.text_director) TextView mDirectorText;
    @BindView(R.id.text_country) TextView mCountryText;
    @BindView(R.id.text_language) TextView mLanguageText;
    @BindView(R.id.text_release_date) TextView mReleaseDateText;
    @BindView(R.id.text_runtime) TextView mRuntimeText;
    @BindView(R.id.text_sinopsis) TextView mSinopsisText;
    @BindView(R.id.text_tagline) TextView mTaglineText;
    @BindView(R.id.image_poster) ImageView mPosterImageView;

    private String mMovieId;

    public static Intent getStartIntent(Context context, String movieId, ArrayList<String> cinemas) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        intent.putExtra(EXTRA_CINEMAS, cinemas);
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
        ArrayList<String> mCinemas = getIntent().getStringArrayListExtra(EXTRA_CINEMAS);
        if (mCinemas == null) {
            throw new IllegalArgumentException("Detail Activity requires movie cinemas@");
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

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780" + movie.backdropPath)
                .into(mBackdropImage);
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342" + movie.posterPath)
                .into(mPosterImageView);

        for(int i = 0; i < 10; i++) {
            if(movie.credits.crew.get(i).job.equalsIgnoreCase("Director")) {
                Util.setSpannableStringText(
                        mDirectorText,
                        "Director: ",
                        movie.credits.cast.get(i).name);
                break;
            }
        }
        if (mDirectorText.getText().toString().isEmpty()) {
            Util.setSpannableStringText(
                    mDirectorText,
                    "Director: ",
                    "Not found");
        }

        if(movie.credits.cast != null && movie.credits.cast.size() > 7) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                builder.append(movie.credits.cast.get(i).name);
                builder.append(", ");
            }
            builder.append(movie.credits.cast.get(5).name);
            Util.setSpannableStringText(mCastText, "Actores: ", builder.toString());
        }

        mGenreText.setText(Util.genreTextToString(movie.genres));
        Util.setSpannableStringText(mCountryText, "País: ", Util.countryProductionsToString(movie.productionCountries));
        Util.setSpannableStringText(mLanguageText,"Idioma: ", movie.originalLanguage);
        Util.setSpannableStringText(mReleaseDateText, "Fecha de estreno: ", movie.releaseDate);
        Util.setSpannableStringText(mRuntimeText, "Duración: ", String.format("%1$s min", movie.runtime));
        mSinopsisText.setText(movie.overview);
        if(movie.tagline != null && !movie.tagline.isEmpty()) {
            mTaglineText.setText(movie.tagline);
        } else {
            mTaglineText.setVisibility(View.GONE);
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