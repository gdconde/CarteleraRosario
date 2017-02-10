package com.gdconde.cartelerarosario.ui.main;


import com.gdconde.cartelerarosario.data.DataManager;
import com.gdconde.cartelerarosario.data.model.HoytsAnswer;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;
import com.gdconde.cartelerarosario.injection.ConfigPersistent;
import com.gdconde.cartelerarosario.ui.base.BasePresenter;
import com.gdconde.cartelerarosario.util.WebParser;

import java.io.IOException;
import java.util.ArrayList;
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
                            for(Movie movie : WebParser.getElCairoMoviesTitles(value.string())) {
                                if(!getMvpView().isMovieInList(movie)) {
                                    getMovieData(movie);
                                }
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
                            getMvpView().showMovie(WebParser.getElCairoMovieData(value.string()));
                        } catch (IOException e) {
                            Timber.e(e, "There was an error fetching El Cairo web");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    public void getShowcaseMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getShowcaseMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            for (Movie movie : WebParser.getShowcaseMoviesTitles(value.string())) {
                                if(!getMvpView().isMovieInList(movie)) {
                                    getMovieData(movie);
                                }
                            }
                        } catch(IOException e) {
                            Timber.e(e, "There was an error fetching El Cairo web");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    public void getHoytsMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getHoytsMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ArrayList<HoytsAnswer>>() {
                    @Override
                    public void onSuccess(ArrayList<HoytsAnswer> value) {
                        ArrayList<String> titles = new ArrayList<String>();
                        for(HoytsAnswer title : value) {
                            String realTitle = title.label.replace("SUBTITULADA", "")
                                    .replace("CASTELLANO","")
                                    .replace("3D","")
                                    .replace("2D","")
                                    .replace("XD","")
                                    .trim();
                            if(titles.contains(realTitle)) {
                                continue;
                            }
                            titles.add(realTitle);
                            Movie movie = new Movie();
                            movie.title = realTitle;
                            movie.cinemas.add(Movie.HOYTS);
                            if(!getMvpView().isMovieInList(movie)) {
                                getMovieData(movie);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    public void getVillageMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getVillageMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            for (Movie movie : WebParser.getVillageMoviesTitles(value.string())) {
                                if(!getMvpView().isMovieInList(movie)) {
                                    getMovieData(movie);
                                }
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

    private void getMovieData(final Movie movie) {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getMovieData(movie.title)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new SingleSubscriber<MovieDbAnswer>() {
            @Override
            public void onSuccess(MovieDbAnswer movieData) {
                if(movieData.total_results == 0) {
                    getElCairoMovie(movie.link);
                    return;
                }
                movieData.results.get(0).cinemas = movie.cinemas;

                getMvpView().showProgress(false);
                getMvpView().showMovie(movieData.results.get(0));
            }

            @Override
            public void onError(Throwable error) {
            }
        }));
    }

}
