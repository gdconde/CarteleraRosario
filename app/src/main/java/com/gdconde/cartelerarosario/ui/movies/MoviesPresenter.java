package com.gdconde.cartelerarosario.ui.movies;

import com.gdconde.cartelerarosario.data.DataManager;
import com.gdconde.cartelerarosario.data.model.HoytsAnswer;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;
import com.gdconde.cartelerarosario.data.model.VillageAnswer;
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

/**
 * Created by gdconde on 2/3/17.
 */

public class MoviesPresenter extends BasePresenter<MoviesMvpView> {

    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;

    @Inject
    public MoviesPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MoviesMvpView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
        mSubscriptions = null;
    }

    void getElCairoMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        mSubscriptions.add(mDataManager.getElCairoMovies(calendar)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        Timber.i("El Cairo Movies FETCHED");
                        try {
                            for(Movie movie : WebParser.getElCairoMoviesTitles(value.string())) {
                                if(!getMvpView().isMovieInList(movie)) {
                                    Timber.i("%s not in list.", movie.title);
                                    getMovieData(movie);
                                } else {
                                    Timber.i("%s already in list.", movie.title);
                                    getMvpView().updateMovie(movie);
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

    void getShowcaseMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getShowcaseMovies()
        getMvpView().showProgress(true);
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        Timber.i("Showcase Movies FETCHED");
                        try {
                            ArrayList<Movie> movies = WebParser.getShowcaseMoviesTitles(value.string());
                            for (Movie movie : movies) {
                                if(!getMvpView().isMovieInList(movie)) {
                                    Timber.i("%s not in list.", movie.title);
                                    getMovieData(movie);
                                } else {
                                    getMvpView().updateMovie(movie);
                                    Timber.i("%s already in list.", movie.title);
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

    void getMonumentalMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getMonumentalMovies()
        getMvpView().showProgress(true);
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        Timber.i("Monumental Movies FETCHED");
                        try {
                            for(Movie movie : WebParser.getMonumentalMoviesTitles(value.string())) {
                                if(!getMvpView().isMovieInList(movie)) {
                                    Timber.i("%s not in list.", movie.title);
                                    getMovieData(movie);
                                } else {
                                    Timber.i("%s already in list.", movie.title);
                                    getMvpView().updateMovie(movie);
                                }
                            }
                        } catch(IOException e) {
                            Timber.e(e, "There was an error fetching Monumental web");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    void getDelCentroMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getDelCentroMovies()
        getMvpView().showProgress(true);
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody value) {
                        try {
                            for(Movie movie : WebParser.getDelCentroMoviesTitles(value.string())) {
                                if(!getMvpView().isMovieInList(movie)) {
                                    getMovieData(movie);
                                } else {
                                    getMvpView().updateMovie(movie);
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

    void getHoytsMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getHoytsMovies()
        getMvpView().showProgress(true);
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<ArrayList<HoytsAnswer>>() {
                    @Override
                    public void onSuccess(ArrayList<HoytsAnswer> value) {
                        ArrayList<String> titles = new ArrayList<>();
                        for(HoytsAnswer title : value) {
                            String realTitle = title.label
                                    .replace("SUBTITULADA", "")
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
                            } else {
                                getMvpView().updateMovie(movie);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    void getVillageMovies() {
        checkViewAttached();
        mSubscriptions.add(mDataManager.getVillageMovies()
        getMvpView().showProgress(true);
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<VillageAnswer>() {
                    @Override
                    public void onSuccess(VillageAnswer titles) {
                        for (VillageAnswer.VillageMovie villageMovie : titles.data) {
                            Movie movie = new Movie();
                            movie.title = villageMovie.title_translated;
                            movie.cinemas.add(Movie.VILLAGE);
                            if(!getMvpView().isMovieInList(movie)) {
                                getMovieData(movie);
                            } else {
                                getMvpView().updateMovie(movie);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                    }
                }));
    }

    private void getMovieData(final Movie movie) {
        checkViewAttached();
        Timber.i("OBTAINING DATA: %s", movie.title);
        mSubscriptions.add(mDataManager.getMovieData(movie.title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<MovieDbAnswer>() {
                    @Override
                    public void onSuccess(MovieDbAnswer movieData) {
                        getMvpView().showProgress(false);
                        int count = movieData.total_results;
                        if (movieData.total_results > 5) {
                            count = 5;
                        }
                        for (int i = 0; i < count; i++) {
                            if (movie.cinemas.get(0).equalsIgnoreCase(Movie.EL_CAIRO)) {
                                movieData.results.get(0).cinemas = movie.cinemas;
                                Timber.i("DATA OBTAINED: %s", movieData.results.get(0).title);
                                if(movieData.results.get(0).genreIds.isEmpty()) return;
                                getMvpView().addMovie(movieData.results.get(0));
                            }
                            else if (movieData.results.get(i).popularity > 1.2 &&
                                    Integer.valueOf(movieData.results.get(i).releaseDate.substring(0,4)) > 2015) {
                                movieData.results.get(i).cinemas = movie.cinemas;
                                Timber.i("DATA OBTAINED: %s", movieData.results.get(i).title);
                                if(movieData.results.get(i).genreIds.isEmpty()) return;
                                getMvpView().addMovie(movieData.results.get(i));
                                return;
                            }
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
                        try {
                            getMvpView().addMovie(WebParser.getElCairoMovieData(value.string()));
                        } catch (IOException e) {
                            Timber.e(e, "There was an error fetching El Cairo web");
                        }
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                }));
    }

    /*public void getMoviesFromDbOrNetwork(final boolean showcaseEnabled,
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
                            Timber.i("Movie Table EMPTY or OUTDATED");
                            if(showcaseEnabled) getShowcaseMovies();
                            if(elCairoEnabled) getElCairoMovies();
                            if(hoytsEnabled) getHoytsMovies();
                            if(villageEnabled) getVillageMovies();
                            if(delCentroEnabled) getDelCentroMovies();
                            if(monumentalEnabled) getMonumentalMovies();
                        } else {
                            Timber.i("Movies fetched from DB");
                            getMoviesFromDb();
                        }
                    }
                }));
    }*/

    /*public void getMoviesFromNetwork(boolean showcaseEnabled,
                                     boolean elCairoEnabled,
                                     boolean hoytsEnabled,
                                     boolean villageEnabled,
                                     boolean delCentroEnabled,
                                     boolean monumentalEnabled) {
        checkViewAttached();
        if(showcaseEnabled) getShowcaseMovies();
        if(elCairoEnabled) getElCairoMovies();
        if(hoytsEnabled) getHoytsMovies();
        if(villageEnabled) getVillageMovies();
        if(delCentroEnabled) getDelCentroMovies();
        if(monumentalEnabled) getMonumentalMovies();
    }*/

    /*private void getMoviesFromDb() {
        checkViewAttached();
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
    }*/
}
