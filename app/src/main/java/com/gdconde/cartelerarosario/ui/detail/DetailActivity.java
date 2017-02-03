package com.gdconde.cartelerarosario.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.ui.base.BaseActivity;
import com.gdconde.cartelerarosario.ui.common.ErrorView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements DetailMvpView, ErrorView.ErrorListener {

    public static final String EXTRA_POKEMON_NAME = "EXTRA_POKEMON_NAME";

    @Inject DetailPresenter mDetailPresenter;

    @BindView(R.id.view_error) ErrorView mErrorView;
    @BindView(R.id.image_pokemon) ImageView mPokemonImage;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.layout_stats) LinearLayout mStatLayout;
    @BindView(R.id.layout_pokemon) View mPokemonLayout;

    private String mPokemonName;

    public static Intent getStartIntent(Context context, String pokemonName) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_POKEMON_NAME, pokemonName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mDetailPresenter.attachView(this);

        mPokemonName = getIntent().getStringExtra(EXTRA_POKEMON_NAME);
        if (mPokemonName == null) {
            throw new IllegalArgumentException("Detail Activity requires a pokemon name@");
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(mPokemonName.substring(0, 1).toUpperCase() + mPokemonName.substring(1));

        mErrorView.setErrorListener(this);

//        mDetailPresenter.getPokemon(mPokemonName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailPresenter.detachView();
    }

//    @Override
//    public void showPokemon(Pokemon pokemon) {
//        if (pokemon.sprites != null && pokemon.sprites.frontDefault != null) {
//            Glide.with(this)
//                    .load(pokemon.sprites.frontDefault)
//                    .into(mPokemonImage);
//        }
//        mPokemonLayout.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void showStat(Statistic statistic) {
//        StatisticView statisticView = new StatisticView(this);
//        statisticView.setStat(statistic);
//        mStatLayout.addView(statisticView);
//    }

    @Override
    public void showProgress(boolean show) {
        mErrorView.setVisibility(View.GONE);
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError() {
        mPokemonLayout.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReloadData() {
//        mDetailPresenter.getPokemon(mPokemonName);
    }
}