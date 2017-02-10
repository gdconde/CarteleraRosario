package com.gdconde.cartelerarosario.data.model;

import com.gdconde.cartelerarosario.util.StringSimilarity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gdconde on 1/2/17.
 */

public class Movie {

    public static final int EL_CAIRO = 0;
    public static final int SHOWCASE = 1;
    public static final int HOYTS = 2;
    public static final int MADRE_CABRINI = 3;
    public static final int MONUMENTAL = 4;
    public static final int ARTEON = 5;
    public static final int DEL_SIGLO = 6;
    public static final int VILLAGE = 7;

    public String title;

    public ArrayList<Long> schedule = new ArrayList<>();

    public ArrayList<Integer> cinemas = new ArrayList<>();

    public String link;

    public String id;

    @SerializedName("overview")
    public String sinopsis;

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("genre_ids")
    public ArrayList<Integer> genreIds;

    @SerializedName("imdb_id")
    public String imdbId;

    public int runtime;

    @SerializedName("original_language")
    public String originalLanguage;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Movie)) {
            return false;
        } else {
            if(StringSimilarity.similarity(((Movie)obj).title, title) < 0.6) {
                return false;
            }
        }
        return true;
    }

    /*public class Results {
        public ArrayList<Videos> results;
    }

    public class Videos {

        public String id;

        @SerializedName("iso_639_1")
        public String iso6391;

        @SerializedName("iso_3166_1")
        public String iso31661;

        public String key;

        public String name;

        public String site;

        public int size;

        public String type;
    }*/
}
