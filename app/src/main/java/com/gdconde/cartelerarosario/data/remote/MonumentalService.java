package com.gdconde.cartelerarosario.data.remote;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by gdconde on 13/2/17.
 */

public interface MonumentalService {

    @GET("/")
    Single<ResponseBody> getMonumentalMovies();
}
