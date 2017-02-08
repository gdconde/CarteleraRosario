package com.gdconde.cartelerarosario.data.remote;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Single;

/**
 * Created by gdconde on 8/2/17.
 */

public interface ShowcaseService {

    @GET("rosario")
    Single<ResponseBody> getShowcaseMovies();

}
