package com.gdconde.cartelerarosario.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gdconde on 1/2/17.
 */

public class Movie {

    public String title;

    public ArrayList<Long> schedule;

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

    @SerializedName("original_language")
    public String originalLanguage;


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Movie)) {
            return false;
        } else {
            if(!((Movie)obj).title.equalsIgnoreCase(title)) {
                return false;
            } else {
                if(!((Movie)obj).schedule.equals(schedule)) {
                    return false;
                }
            }
        }
        return true;
    }
}
