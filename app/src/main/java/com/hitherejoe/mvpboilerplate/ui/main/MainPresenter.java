package com.hitherejoe.mvpboilerplate.ui.main;


import com.hitherejoe.mvpboilerplate.data.DataManager;
import com.hitherejoe.mvpboilerplate.injection.ConfigPersistent;
import com.hitherejoe.mvpboilerplate.ui.base.BasePresenter;
import com.hitherejoe.mvpboilerplate.util.WebParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
        mSubscriptions = null;
    }

    /*public void getPokemon(int limit) {
        checkViewAttached();
        getMvpView().showProgress(true);
        mSubscriptions.add(mDataManager.getPokemonList(limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<List<String>>() {
                    @Override
                    public void onSuccess(List<String> pokemon) {
                        getMvpView().showProgress(false);
                        getMvpView().showPokemon(pokemon);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showProgress(false);
                        getMvpView().showError();
                        Timber.e(error, "There was an error retrieving the pokemon");
                    }
                }));
    }*/

    public void getElCairoMovies() {
        checkViewAttached();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        mSubscriptions.add(mDataManager.getElCairoMovies(calendar)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            for(String movieUrl : WebParser.parseElCairoMovies(value.string())) {
                                getElCairoMovie(movieUrl);
                            }
                        } catch (IOException e) {
                            Timber.e(e, "There was an error fetching el cairo web");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    public void getElCairoMovie(String movieUrl) {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getElCairoMovie(movieUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        getMvpView().showProgress(false);
                        try {
                            getMvpView().showMovie(WebParser.parseElCairoMovie(value.string()));
                        } catch (IOException e) {
                            Timber.e(e, "There was an error fetching el cairo web");
                        }

                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

}
