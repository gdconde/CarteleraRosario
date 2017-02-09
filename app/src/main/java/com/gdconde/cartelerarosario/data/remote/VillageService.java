package com.gdconde.cartelerarosario.data.remote;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Single;

/**
 * Created by gdconde on 9/2/17.
 */

public interface VillageService {

    @GET("cines-rosario")
    Single<ResponseBody> getVillageMovies();
}
