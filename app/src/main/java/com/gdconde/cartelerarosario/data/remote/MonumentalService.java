package com.gdconde.cartelerarosario.data.remote;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Single;

/**
 * Created by gdconde on 13/2/17.
 */

public interface MonumentalService {

    @GET("/")
    Single<ResponseBody> getMonumentalMovies();
}
