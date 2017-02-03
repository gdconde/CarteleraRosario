package com.hitherejoe.mvpboilerplate.data;

import com.hitherejoe.mvpboilerplate.BuildConfig;
import com.hitherejoe.mvpboilerplate.data.model.Movie;
import com.hitherejoe.mvpboilerplate.data.model.MovieDbAnswer;
import com.hitherejoe.mvpboilerplate.data.model.NamedResource;
import com.hitherejoe.mvpboilerplate.data.model.Pokemon;
import com.hitherejoe.mvpboilerplate.data.remote.ElCairoService;
import com.hitherejoe.mvpboilerplate.data.remote.MvpBoilerplateService;
import com.hitherejoe.mvpboilerplate.data.remote.MvpBoilerplateService.PokemonListResponse;
import com.hitherejoe.mvpboilerplate.data.remote.TheMovieDbService;
import com.hitherejoe.mvpboilerplate.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import rx.Single;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final MvpBoilerplateService mMvpBoilerplateService;
    private final ElCairoService mElCairoService;
    private final TheMovieDbService mTheMovieDbService;

    @Inject
    public DataManager(MvpBoilerplateService mvpBoilerplateService,
                       ElCairoService elCairoService,
                       TheMovieDbService theMovieDbService) {
        mMvpBoilerplateService = mvpBoilerplateService;
        mElCairoService = elCairoService;
        mTheMovieDbService = theMovieDbService;
    }

    public Single<List<String>> getPokemonList(int limit) {
        return mMvpBoilerplateService.getPokemonList(limit)
                .flatMap(new Func1<MvpBoilerplateService.PokemonListResponse,
                        Single<? extends List<String>>>() {
                            @Override
                            public Single<? extends List<String>>
                                    call(PokemonListResponse pokemonListResponse) {
                                List<String> pokemonNames = new ArrayList<>();
                                for (NamedResource pokemon : pokemonListResponse.results) {
                                    pokemonNames.add(pokemon.name);
                                }
                                return Single.just(pokemonNames);
                            }
                        });
    }

    public Single<Pokemon> getPokemon(String name) {
        return mMvpBoilerplateService.getPokemon(name);
    }

    public Single<ResponseBody> getElCairoMovies(Calendar date) {
        return mElCairoService
                .getElCairoMovies(Util.formatCalendarDateToString(date, "yyyy-MM-dd"));
    }

    public Single<ResponseBody> getElCairoMovie(String url) {
        return mElCairoService.getElCairoMovie(url);
    }

    public Single<MovieDbAnswer> getMovieData(String movieTitle) {
        return mTheMovieDbService
                .getMovieData(BuildConfig.THEMOVIEDB_APIKEY, "es", movieTitle, "AR");
    }

}