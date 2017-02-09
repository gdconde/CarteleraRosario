package com.gdconde.cartelerarosario.data.remote;

import com.gdconde.cartelerarosario.data.model.HoytsAnswer;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by gdconde on 9/2/17.
 */

public interface HoytsService {

    @GET("jsonManager.aspx?selected=114&type=cinema&_=1486664087683")
    Single<ArrayList<HoytsAnswer>> getHoytsMovies();
}
