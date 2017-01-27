package com.hitherejoe.mvpboilerplate.data.remote;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Single;

/**
 * Created by gdconde on 27/1/17.
 */

public interface ElCairoService {

    @GET("index.html")
    Single<ResponseBody> getElCairoMovies();
}
