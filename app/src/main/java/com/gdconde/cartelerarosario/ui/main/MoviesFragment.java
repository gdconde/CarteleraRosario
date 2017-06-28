package com.gdconde.cartelerarosario.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gdconde.cartelerarosario.BuildConfig;
import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.ui.base.BaseFragment;
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

/**
 * Created by gdconde on 2/3/17.
 */

public class MoviesFragment extends BaseFragment
        implements MoviesMvpView, MoviesAdapter.ClickListener {

    public MoviesFragment() {}

    @Inject MoviesPresenter mMoviesPresenter;
    @Inject MoviesAdapter mMoviesAdapter;

    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.progress_text) TextView mProgressText;
    @BindView(R.id.recycler_movies) RecyclerView mMoviesRecycler;
    @BindView(R.id.swipe_to_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean showcaseEnabled;
    private boolean elCairoEnabled;
    private boolean hoytsEnabled;
    private boolean villageEnabled;
    private boolean delCentroEnabled;
    private boolean monumentalEnabled;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
        mMoviesPresenter.attachView(this);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.primary);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.white);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mMoviesPresenter.getHoytsMovies();
                    }
                });

        mMoviesAdapter.setClickListener(this);
        mMoviesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMoviesRecycler.setAdapter(mMoviesAdapter);

        mMoviesPresenter.getHoytsMovies();

        return view;
    }

    @Override
    public boolean isMovieInList(Movie movie) {
        return mMoviesAdapter.isMovieInList(movie);
    }

    @Override
    public void updateMovie(Movie movie) {
        mMoviesAdapter.updateMovie(movie);
    }

    @Override
    public void showMovies(ArrayList<Movie> movies) {
        mMoviesAdapter.setMovies(movies);
        mMoviesAdapter.notifyDataSetChanged();

        mMoviesRecycler.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addMovie(Movie movie) {
        mMoviesAdapter.addMovie(movie);
    }

    @Override
    public void onMovieClick(View view, Movie movie) {
        startActivity(DetailActivity.getStartIntent(getContext(), movie.id, movie.cinemas));
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mProgress.setVisibility(View.VISIBLE);
            mProgressText.setVisibility(View.VISIBLE);
            mMoviesRecycler.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.GONE);
            mProgressText.setVisibility(View.GONE);
            mMoviesRecycler.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showProgressText(String text) {
        mProgressText.setText(String.format("Obteniendo películas del cine %1$s", text));
    }

    /*public void getRemoteConfig(final boolean fromNetworkOnly) {
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Timber.i("Firebase Fetch Succeeded");
                            // Once the config is successfully fetched it must be activated before
                            // newly fetched values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Timber.e("Firebase Fetch Failed");
                        }
                        setCinemasAvailable(fromNetworkOnly);
                    }
                });
    }*/

    /*private void setCinemasAvailable(boolean fromNetworkOnly) {
        showcaseEnabled = mFirebaseRemoteConfig.getBoolean("showcase_enabled");
        elCairoEnabled = mFirebaseRemoteConfig.getBoolean("el_cairo_enabled");
        hoytsEnabled = mFirebaseRemoteConfig.getBoolean("hoyts_enabled");
        villageEnabled = mFirebaseRemoteConfig.getBoolean("village_enabled");
        delCentroEnabled = mFirebaseRemoteConfig.getBoolean("del_centro_enabled");
        monumentalEnabled = mFirebaseRemoteConfig.getBoolean("monumental_enabled");

        if(fromNetworkOnly) {
            mMoviesPresenter.getMoviesFromNetwork(showcaseEnabled,
                    elCairoEnabled,
                    hoytsEnabled,
                    villageEnabled,
                    delCentroEnabled,
                    monumentalEnabled);
        } else {
            mMoviesPresenter.getMoviesFromDbOrNetwork(showcaseEnabled,
                    elCairoEnabled,
                    hoytsEnabled,
                    villageEnabled,
                    delCentroEnabled,
                    monumentalEnabled);
        }

        Timber.i("Cinemas Enabled: %s %s %s %s %s %s",
                showcaseEnabled ? "Showcase":"",
                elCairoEnabled ? "El Cairo":"",
                hoytsEnabled ? "Hoyts":"",
                villageEnabled ? "Village":"",
                delCentroEnabled ? "Del Centro":"",
                monumentalEnabled ? "Monumental":"");
    }*/
}
