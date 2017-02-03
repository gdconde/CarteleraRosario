package com.gdconde.cartelerarosario.data.remote;

import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by gdconde on 3/2/17.
 */

public interface TheMovieDbService {

    @GET("search/movie")
    Single<MovieDbAnswer> getMovieData(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String movieTitle,
            @Query("region") String countryAlpha
    );
}
