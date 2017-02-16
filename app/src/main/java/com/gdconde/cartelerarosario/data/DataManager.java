package com.gdconde.cartelerarosario.data;

import com.gdconde.cartelerarosario.BuildConfig;
import com.gdconde.cartelerarosario.data.local.DatabaseHelper;
import com.gdconde.cartelerarosario.data.model.HoytsAnswer;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;
import com.gdconde.cartelerarosario.data.model.MovieDetail;
import com.gdconde.cartelerarosario.data.remote.DelCentroService;
import com.gdconde.cartelerarosario.data.remote.ElCairoService;
import com.gdconde.cartelerarosario.data.remote.HoytsService;
import com.gdconde.cartelerarosario.data.remote.MonumentalService;
import com.gdconde.cartelerarosario.data.remote.ShowcaseService;
import com.gdconde.cartelerarosario.data.remote.TheMovieDbService;
import com.gdconde.cartelerarosario.data.remote.VillageService;
import com.gdconde.cartelerarosario.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Single;

@Singleton
public class DataManager {

    private final ElCairoService mElCairoService;
    private final ShowcaseService mShowcaseService;
    private final HoytsService mHoytsService;
    private final VillageService mVillageService;
    private final DelCentroService mDelCentroService;
    private final MonumentalService mMonumentalService;
    private final TheMovieDbService mTheMovieDbService;
    private final DatabaseHelper mDatabaseHelper;

    @Inject
    public DataManager(TheMovieDbService theMovieDbService,
                       ElCairoService elCairoService,
                       ShowcaseService showcaseService,
                       VillageService villageService,
                       HoytsService hoytsService,
                       DelCentroService delCentroService,
                       MonumentalService monumentalService,
                       DatabaseHelper databaseHelper) {
        mElCairoService = elCairoService;
        mShowcaseService = showcaseService;
        mHoytsService = hoytsService;
        mVillageService = villageService;
        mDelCentroService = delCentroService;
        mMonumentalService = monumentalService;
        mTheMovieDbService = theMovieDbService;
        mDatabaseHelper = databaseHelper;
    }


    public Single<ResponseBody> getElCairoMovies(Calendar date) {
        return mElCairoService
                .getElCairoMovies(Util.formatCalendarDateToString(date, "yyyy-MM-dd"));
    }

    public Single<ResponseBody> getElCairoMovie(String url) {
        return mElCairoService.getElCairoMovie(url);
    }

    public Single<ResponseBody> getShowcaseMovies() {
        return mShowcaseService.getShowcaseMovies();
    }

    public Single<MovieDbAnswer> getMovieData(String movieTitle) {
        return mTheMovieDbService
                .getMovieData(BuildConfig.THEMOVIEDB_APIKEY, "es", movieTitle, "AR");
    }

    public Single<ArrayList<HoytsAnswer>> getHoytsMovies() {
        return mHoytsService.getHoytsMovies();
    }

    public Single<ResponseBody> getVillageMovies() {
        return mVillageService.getVillageMovies();
    }

    public Single<MovieDetail> getMovieDetail(String movieId) {
        return mTheMovieDbService
                .getMovieDetails(movieId, "es", BuildConfig.THEMOVIEDB_APIKEY, "credits,videos");
    }

    public Single<ResponseBody> getMonumentalMovies() {
        return mMonumentalService.getMonumentalMovies();
    }

    public Single<ResponseBody> getDelCentroMovies() {
        return mDelCentroService.getDelCentroMovies();
    }

    public Observable<Movie> addMovieToDb(Movie movie) {
        return mDatabaseHelper.addMovie(movie);
    }

    public Observable<List<Movie>> getMoviesFromDb() {
        return mDatabaseHelper.getMovies();
    }

}