package com.gdconde.cartelerarosario.util;

import com.gdconde.cartelerarosario.data.model.Genre;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gdconde on 2/2/17.
 */

public final class Util {

    public static int stringToIntMonth(String month) {
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

    public static String genreIdsToString(ArrayList<Integer> genreIds) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (; i < genreIds.size() - 1; i++) {
            builder.append(genreIdToString(genreIds.get(i)));
            builder.append(", ");
        }
        builder.append(genreIdToString(genreIds.get(i)));
        return builder.toString();
    }

    public static String genreIdToString(int genreId) {
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

}
