package com.gdconde.cartelerarosario.data;

import com.gdconde.cartelerarosario.BuildConfig;
import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;
import com.gdconde.cartelerarosario.data.remote.ElCairoService;
import com.gdconde.cartelerarosario.data.remote.ShowcaseService;
import com.gdconde.cartelerarosario.data.remote.TheMovieDbService;
import com.gdconde.cartelerarosario.util.Util;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import rx.Single;

@Singleton
public class DataManager {

    private final ElCairoService mElCairoService;
    private final ShowcaseService mShowcaseService;
    private final TheMovieDbService mTheMovieDbService;

    @Inject
    public DataManager(
           TheMovieDbService theMovieDbService,
            ElCairoService elCairoService,
            ShowcaseService showcaseService) {
        mElCairoService = elCairoService;
        mShowcaseService = showcaseService;
        mTheMovieDbService = theMovieDbService;
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

}