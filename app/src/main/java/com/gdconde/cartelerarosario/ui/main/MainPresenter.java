package com.gdconde.cartelerarosario.ui.main;


import com.gdconde.cartelerarosario.data.DataManager;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;
import com.gdconde.cartelerarosario.injection.ConfigPersistent;
import com.gdconde.cartelerarosario.ui.base.BasePresenter;
import com.gdconde.cartelerarosario.util.WebParser;

import java.io.IOException;
import java.util.Calendar;

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

    public void getElCairoMovies() {
        checkViewAttached();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        mSubscriptions.add(mDataManager.getElCairoMovies(calendar)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            for(String movieUrl : WebParser.parseElCairoMovies(value.string())) {
                                getElCairoMovie(movieUrl);
                            }
                        } catch (IOException e) {
                            Timber.e(e, "There was an error fetching El Cairo web");
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
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        getMvpView().showProgress(false);
                        try {
                            getMovieData(WebParser.parseElCairoMovie(value.string()));
                        } catch (IOException e) {
                            Timber.e(e, "There was an error fetching El Cairo web");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    public void getMovieData(final Movie movie) {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getMovieData(movie.title)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new SingleSubscriber<MovieDbAnswer>() {
            @Override
            public void onSuccess(MovieDbAnswer value) {
                value.results.get(0).schedule = movie.schedule;
                if(value.results.get(0).sinopsis.isEmpty() && !movie.sinopsis.isEmpty()) {
                    value.results.get(0).sinopsis = movie.sinopsis;
                }
                getMvpView().showProgress(false);
                getMvpView().showMovie(value.results.get(0));
            }

            @Override
            public void onError(Throwable error) {

            }
        }));
    }

}
