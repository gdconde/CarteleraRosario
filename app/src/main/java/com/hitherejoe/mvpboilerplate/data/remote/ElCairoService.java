package com.hitherejoe.mvpboilerplate.data.remote;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

/**
 * Created by gdconde on 27/1/17.
 */

public interface ElCairoService {

    @GET("mes/{month}/index.html")
    Single<ResponseBody> getElCairoMovies(
            @Path("month") Calendar month
    );

    @GET("ver/{movieUrl}")
    Single<ResponseBody> getElCairoMovie(
            @Path("movieUrl") String movieUrl
    );
}
