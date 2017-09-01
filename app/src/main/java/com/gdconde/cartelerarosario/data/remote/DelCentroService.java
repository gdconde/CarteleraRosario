package com.gdconde.cartelerarosario.data.remote;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import io.reactivex.Single;

/**
 * Created by gdconde on 13/2/17.
 */

public interface DelCentroService {

    @GET("cines")
    Single<ResponseBody> getDelCentroMovies();
}
