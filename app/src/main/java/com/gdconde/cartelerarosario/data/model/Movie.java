package com.gdconde.cartelerarosario.data.model;

import com.gdconde.cartelerarosario.util.StringSimilarity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gdconde on 1/2/17.
 */

public class Movie {

    public static final String EL_CAIRO = "0";
    public static final String SHOWCASE = "1";
    public static final String HOYTS = "2";
    public static final String MADRE_CABRINI = "3";
    public static final String MONUMENTAL = "4";
    public static final String ARTEON = "5";
    public static final String DEL_SIGLO = "6";
    public static final String VILLAGE = "7";

    @SerializedName("poster_path")
    public String posterPath;

    @SerializedName("overview")
    public String sinopsis;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("genre_ids")
    public ArrayList<String> genreIds;

    public String id;

    @SerializedName("original_language")
    public String originalLanguage;

    public String title;

    @SerializedName("backdrop_path")
    public String backdropPath;

    public ArrayList<String> schedule = new ArrayList<>();

    public ArrayList<String> cinemas = new ArrayList<>();

    public String link;

    public Long updateTime;

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

}
