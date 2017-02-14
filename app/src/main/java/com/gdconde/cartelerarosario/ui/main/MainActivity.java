package com.gdconde.cartelerarosario.ui.main;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.gdconde.cartelerarosario.BuildConfig;
import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.ui.base.BaseActivity;
import com.gdconde.cartelerarosario.ui.common.ErrorView;
import com.gdconde.cartelerarosario.ui.detail.DetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, MoviesAdapter.ClickListener,
        ErrorView.ErrorListener {

    @Inject MoviesAdapter mMoviesAdapter;
    @Inject MainPresenter mMainPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.recycler_movies) RecyclerView mMoviesRecycler;
    @BindView(R.id.swipe_to_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRemoteConfig();
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

       /* new CountDownTimer(60000, 2000) {
            @Override
            public void onTick(long l) {
                if(l > 57000) {
                    mMainPresenter.getShowcaseMovies();
                } else if(l > 45000) {
                    mMainPresenter.getElCairoMovies();
                    mMainPresenter.getDelCentroMovies();
                } else if(l > 32000){
                    mMainPresenter.getVillageMovies();
                } else if(l > 20000){
                    mMainPresenter.getHoytsMovies();
                } else if(l > 8000){
                    mMainPresenter.getMonumentalMovies();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    public boolean isMovieInList(Movie movie) {
        return mMoviesAdapter.isMovieInList(movie);
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
    public void onMovieClick(View view, Movie movie) {
        startActivity(DetailActivity.getStartIntent(this, movie.id, movie.cinemas));
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
        new CountDownTimer(60000, 2000) {
            @Override
            public void onTick(long l) {
                if(l > 57000) {
                    mMainPresenter.getShowcaseMovies();
                } else if(l > 45000) {
                    mMainPresenter.getElCairoMovies();
                    mMainPresenter.getDelCentroMovies();
                } else if(l > 32000){
                    mMainPresenter.getVillageMovies();
                } else if(l > 20000){
                    mMainPresenter.getHoytsMovies();
                } else if(l > 8000){
                    mMainPresenter.getMonumentalMovies();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void getRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Create Remote Config Setting to enable developer mode.
        // Fetching configs from the server is normally limited to 5 requests per hour.
        // Enabling developer mode allows many more requests to be made per hour, so developers
        // can test different config values during development.
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]

        // Set default Remote Config values. In general you should have in app defaults for all
        // values that you may configure using Remote Config later on. The idea is that you
        // use the in app defaults and when you need to adjust those defaults, you set an updated
        // value in the App Manager console. Then the next time you application fetches from the
        // server, the updated value will be used. You can set defaults via an xml file like done
        // here or you can set defaults inline by using one of the other setDefaults methods.S
        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        // [END set_default_values]

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Timber.i("Fetch Succeeded");

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Timber.e("Fetch Failed");
                        }
                        setCinemasAvailable();
                    }
                });
        // [END fetch_config_with_callback]
    }

    private void setCinemasAvailable() {
        if(mFirebaseRemoteConfig.getBoolean("showcase_enabled")) {
            mMainPresenter.getShowcaseMovies();
        }
    }
}