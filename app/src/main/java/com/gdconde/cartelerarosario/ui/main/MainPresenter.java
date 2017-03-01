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
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observer;
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

    private void getElCairoMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        getMvpView().showProgressText("El Cairo");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        mSubscriptions.add(mDataManager.getElCairoMovies(calendar)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        getMvpView().showProgress(false);
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

    private void getElCairoMovie(String movieUrl) {
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

    private void getShowcaseMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        getMvpView().showProgressText("Showcase");
        mSubscriptions.add(mDataManager.getShowcaseMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            getMvpView().showProgress(false);
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
                        getMvpView().showProgress(false);
                    }
                }));
    }

    private void getMonumentalMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getMonumentalMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            for(Movie movie : WebParser.getMonumentalMoviesTitles(value.string())) {
                                getMovieData(movie);
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

    private void getDelCentroMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getDelCentroMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            for(Movie movie : WebParser.getDelCentroMoviesTitles(value.string())) {
                                getMovieData(movie);
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

    private void getHoytsMovies() {
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

    private void getVillageMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        getMvpView().showProgressText("Village");
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
        getMvpView().showProgress(true);
        getMvpView().showProgressText("");
        mSubscriptions.add(mDataManager.getMovieData(movie.title)
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.io())
        .subscribe(new SingleSubscriber<MovieDbAnswer>() {
            @Override
            public void onSuccess(MovieDbAnswer movieData) {
                movieData.results.get(0).cinemas = movie.cinemas;
                if(movieData.results.get(0).genreIds.isEmpty()) return;
                mSubscriptions.add(mDataManager.addMovieToDb(movieData.results.get(0))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Movie>() {
                            @Override
                            public void onCompleted() {
                                getMvpView().showProgress(false);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Movie movie) {
                                getMvpView().showMovie(movie);
                            }
                        }));
                getMvpView().showProgress(false);
            }

            @Override
            public void onError(Throwable error) {
            }
        }));
    }

    private void getMoviesFromDb() {
        checkViewAttached();
        getMvpView().showProgress(true);
        mSubscriptions.add(mDataManager.getMoviesFromDb()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<List<Movie>>() {
            @Override
            public void onCompleted() {
                getMvpView().showProgress(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Movie> movies) {
                getMvpView().showMovies((ArrayList<Movie>) movies);
            }
        }));
    }

    public void getMovies(final boolean showcaseEnabled,
                          final boolean elCairoEnabled,
                          final boolean hoytsEnabled,
                          final boolean villageEnabled,
                          final boolean delCentroEnabled,
                          final boolean monumentalEnabled) {
        checkViewAttached();
        mSubscriptions.add(mDataManager.isMoviesTableEmptyOrOutdated()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                getMvpView().showProgress(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isMoviesTableEmptyOrOutdated) {
                if (isMoviesTableEmptyOrOutdated) {
                    if(showcaseEnabled) getShowcaseMovies();
                    if(elCairoEnabled) getElCairoMovies();
                    if(hoytsEnabled) getHoytsMovies();
                    if(villageEnabled) getVillageMovies();
                    if(delCentroEnabled) getDelCentroMovies();
                    if(monumentalEnabled) getMonumentalMovies();
                } else {
                    getMoviesFromDb();
                }
            }
        }));
    }

}
