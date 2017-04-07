package com.gdconde.cartelerarosario.data.remote;

import com.gdconde.cartelerarosario.data.model.VillageAnswer;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Single;

/**
 * Created by gdconde on 9/2/17.
 */

public interface VillageService {

    @GET("api/showtimes?complex=4&date=2017-04-07")
    Single<VillageAnswer> getVillageMovies();
}
