package com.gdconde.cartelerarosario.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gdconde on 10/2/17.
 */

public class MovieDetail {

    public Boolean adult;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("belongs_to_collection")
    public Object belongsToCollection;

    public String budget;

    public ArrayList<Genre> genres = null;

    public String homepage;

    public String id;

    @SerializedName("imdb_id")
    public String imdbId;

    @SerializedName("original_language")
    public String originalLanguage;

    @SerializedName("original_title")
    public String originalTitle;

    public String overview;

    public Float popularity;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("production_companies")
    public ArrayList<ProductionCompany> productionCompanies = null;

    @SerializedName("production_countries")
    public ArrayList<ProductionCountry> productionCountries = null;

    @SerializedName("release_date")
    public String releaseDate;

    public String revenue;

    public String runtime;

    @SerializedName("spoken_languages")
    public ArrayList<SpokenLanguage> spokenLanguages = null;

    public String status;

    public String tagline;

    public String title;

    public Boolean video;

    @SerializedName("vote_average")
    public Double voteAverage;

    @SerializedName("vote_count")
    public Double voteCount;

    public Videos videos;


    public class ProductionCompany {

        public String name;

        public Integer id;
    }

    public class ProductionCountry {

        @SerializedName("iso_3166_1")
        public String iso31661;

        public String name;
    }

    public class Result {

        public String id;

        @SerializedName("iso_639_1")
        public String iso6391;

        @SerializedName("iso_3166_1")
        public String iso31661;

        public String key;

        public String name;

        public String site;

        public Integer size;

        public String type;
    }

    public class SpokenLanguage {

        @SerializedName("iso_639_1")
        public String iso6391;

        public String name;
    }

    public class Videos {

        public ArrayList<Result> results = null;
    }

    public class Genre {

        public Integer id;

        public String name;

    }
}