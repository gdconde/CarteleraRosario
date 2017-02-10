package com.gdconde.cartelerarosario.data.remote;

import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;
import com.gdconde.cartelerarosario.data.model.MovieDetail;

import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("movie/{movieId}")
    Single<MovieDetail> getMovieDetails(
            @Path("movieId") String movieId,
            @Query("language") String language,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendQueries
    );
}
