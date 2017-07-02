package com.gdconde.cartelerarosario.data.remote;

import com.gdconde.cartelerarosario.data.model.VillageAnswer;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by gdconde on 9/2/17.
 */

public interface VillageService {

    @GET("movies?complex=4")
    Single<VillageAnswer> getVillageMovies(
            @Query("date") String date
    );
}
