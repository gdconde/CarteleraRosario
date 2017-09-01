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

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class MoviesPresenter extends BasePresenter<MoviesMvpView> {

    private final DataManager mDataManager;

    @Inject
    public MoviesPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    void getElCairoMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        mDataManager
                .getElCairoMovies(calendar)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

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
                });
    }

    void getShowcaseMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        mDataManager
                .getShowcaseMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

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
                });
    }

    void getMonumentalMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        mDataManager
                .getMonumentalMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ResponseBody responseBody) {
                        Timber.i("Monumental Movies FETCHED");
                        try {
                            for(Movie movie : WebParser.getMonumentalMoviesTitles(responseBody.string())) {
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
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    void getDelCentroMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        mDataManager
                .getDelCentroMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ResponseBody responseBody) {
                        try {
                            for(Movie movie : WebParser.getDelCentroMoviesTitles(responseBody.string())) {
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
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    void getHoytsMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        mDataManager
                .getHoytsMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ArrayList<HoytsAnswer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

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
                });
    }

    void getVillageMovies() {
        checkViewAttached();
        getMvpView().showProgress(true);
        mDataManager
                .getVillageMovies()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<VillageAnswer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull VillageAnswer villageAnswer) {
                        for (VillageAnswer.VillageMovie villageMovie : villageAnswer.data) {
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
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void getMovieData(final Movie movie) {
        checkViewAttached();
        Timber.i("OBTAINING DATA: %s", movie.title);
        mDataManager
                .getMovieData(movie.title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<MovieDbAnswer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

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
                });
    }

    private void getElCairoMovie(String movieUrl) {
        checkViewAttached();
        mDataManager
                .getElCairoMovie(movieUrl)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

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
                });
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
