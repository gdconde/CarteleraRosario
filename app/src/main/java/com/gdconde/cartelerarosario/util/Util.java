package com.gdconde.cartelerarosario.util;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.gdconde.cartelerarosario.data.model.Genre;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.data.model.MovieDetail;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gdconde on 2/2/17.
 */

public final class Util {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    static int stringToIntMonth(String month) {
        switch (month) {
            case "Enero": return 0;
            case "Febrero": return 1;
            case "Marzo": return 2;
            case "Abril": return 3;
            case "Mayo": return 4;
            case "Junio": return 5;
            case "Julio": return 6;
            case "Agosto": return 7;
            case "Septiembre": return 8;
            case "Octubre": return 9;
            case "Noviembre": return 10;
            case "Diciembre": return 11;
        }
        return -1;
    }

    public static String formatCalendarDateToString(Calendar date, String format) {
        DateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(date.getTime());
    }

    public static String genreIdsToString(ArrayList<String> genreIds) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (; i < genreIds.size() - 1; i++) {
            builder.append(genreIdToString(Integer.valueOf(genreIds.get(i))));
            builder.append(", ");
        }
        builder.append(genreIdToString(Integer.valueOf(genreIds.get(i))));
        return builder.toString();
    }

    public static String genreTextToString(ArrayList<MovieDetail.Genre> genres) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (; i < genres.size() - 1; i++) {
            builder.append(genres.get(i).name);
            builder.append(", ");
        }
        builder.append(genres.get(i).name);
        return builder.toString();
    }

    public static String countryProductionsToString(
            ArrayList<MovieDetail.ProductionCountry> productionCountries) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (; i < productionCountries.size() - 1; i++) {
            builder.append(productionCountries.get(i).name);
            builder.append(", ");
        }
        builder.append(productionCountries.get(i).name);
        return builder.toString();
    }

    public static String cinemasToString(ArrayList<String> cinemas) {
        StringBuilder builder = new StringBuilder();
        if(cinemas.size() > 0) {
            switch (cinemas.get(0)) {
                case Movie.ARTEON: builder.append("Arteón"); break;
                case Movie.DEL_SIGLO: builder.append("Del Siglo"); break;
                case Movie.EL_CAIRO: builder.append("El Cairo"); break;
                case Movie.HOYTS: builder.append("Hoyts"); break;
                case Movie.MADRE_CABRINI: builder.append("Madre Cabrini"); break;
                case Movie.MONUMENTAL: builder.append("Monumental"); break;
                case Movie.SHOWCASE: builder.append("Showcase"); break;
                case Movie.VILLAGE: builder.append("Village"); break;
            }
        }
        for(int i = 1; i < cinemas.size(); i++) {
            switch (cinemas.get(i)) {
                case Movie.ARTEON: builder.append(", Arteón"); break;
                case Movie.DEL_SIGLO: builder.append(", Del Siglo"); break;
                case Movie.EL_CAIRO: builder.append(", El Cairo"); break;
                case Movie.HOYTS: builder.append(", Hoyts"); break;
                case Movie.MADRE_CABRINI: builder.append(", Madre Cabrini"); break;
                case Movie.MONUMENTAL: builder.append(", Monumental"); break;
                case Movie.SHOWCASE: builder.append(", Showcase"); break;
                case Movie.VILLAGE: builder.append(", Village"); break;
            }
        }
        return builder.toString();
    }

    private static String genreIdToString(int genreId) {
        switch (genreId) {
            case Genre.ACTION: return "Acción";
            case Genre.ANIMATION: return "Animada";
            case Genre.AVENTURE: return "Aventura";
            case Genre.COMEDY: return "Comedia";
            case Genre.CRIME: return "Crimen";
            case Genre.DOCUMENTARY: return "Documental";
            case Genre.DRAMA: return "Drama";
            case Genre.FAMILY: return "Familiar";
            case Genre.FANTASY: return "Fantasía";
            case Genre.HISTORY: return "Historia";
            case Genre.HORROR: return "Terror";
            case Genre.MISTERY: return "Misterio";
            case Genre.MUSICAL: return "Musical";
            case Genre.ROMANCE: return "Romántica";
            case Genre.SCIENCE_FICTION: return "Ciencia Ficción";
            case Genre.SUSPENSE: return "Suspenso";
            case Genre.TV_MOVIE: return "Película para TV";
            case Genre.WAR: return "Bélica";
            case Genre.WESTERN: return "Western";
            default: return "Sin género";
        }
    }

    public static void setSpannableStringText(TextView view, String boldText, String text) {
        SpannableString spannableString = new SpannableString(boldText);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
        view.append(spannableString);
        view.append(text);
    }

}
