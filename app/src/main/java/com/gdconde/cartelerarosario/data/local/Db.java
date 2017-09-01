package com.gdconde.cartelerarosario.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.gdconde.cartelerarosario.data.model.Movie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by gdconde on 13/2/17.
 */

public class Db {

    public Db() { }

    public static final class MoviesTable {
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "uuid";
        public static final String COLUMN_POSTER = "posterPath";
        public static final String COLUMN_BACKDROP = "backdropPath";
        public static final String COLUMN_SINOPSIS = "sinopsis";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_SCHEDULE = "schedule";
        public static final String COLUMN_CINEMAS = "cinemas";
        public static final String COLUMN_UPDATE_TIME = "updateTime";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " TEXT PRIMARY KEY, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_POSTER + " TEXT, " +
                        COLUMN_BACKDROP + " TEXT, " +
                        COLUMN_SINOPSIS + " TEXT, " +
                        COLUMN_RELEASE_DATE + " TEXT, " +
                        COLUMN_GENRES + " TEXT, " +
                        COLUMN_LANGUAGE + " TEXT, " +
                        COLUMN_SCHEDULE + " TEXT, " +
                        COLUMN_CINEMAS + " TEXT, " +
                        COLUMN_UPDATE_TIME + " LONG" +
                        " );";

        public static ContentValues toContentValues(Movie movie) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, movie.id);
            values.put(COLUMN_TITLE, movie.title);
            values.put(COLUMN_POSTER, movie.posterPath);
            values.put(COLUMN_BACKDROP, movie.backdropPath);
            values.put(COLUMN_SINOPSIS, movie.sinopsis);
            values.put(COLUMN_RELEASE_DATE, movie.releaseDate);
            values.put(COLUMN_GENRES, movie.genreIds.toString());
            values.put(COLUMN_LANGUAGE, movie.originalLanguage);
            values.put(COLUMN_SCHEDULE, movie.schedule.toString());
            values.put(COLUMN_CINEMAS, movie.cinemas.toString());
            values.put(COLUMN_UPDATE_TIME, Calendar.getInstance().getTimeInMillis() / 1000);
            return values;
        }

        public static Movie parseCursor(Cursor cursor) {
            Movie movie = new Movie();
            movie.id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            movie.title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            movie.posterPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER));
            movie.backdropPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BACKDROP));
            movie.sinopsis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SINOPSIS));
            movie.releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE));
            movie.genreIds =
                    new ArrayList<>(
                            Arrays.asList(
                                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRES))
                                            .replace("[","")
                                            .replace("]","")
                                            .replace(" ","")
                                            .split(",")));
            if (movie.genreIds.get(0).equalsIgnoreCase("")) movie.genreIds = null;
            movie.originalLanguage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGE));
            movie.schedule =
                    new ArrayList<>(
                            Arrays.asList(
                                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCHEDULE))
                                            .replace("[","")
                                            .replace("]","")
                                            .replace(" ","")
                                            .split(",")));
            if (movie.schedule.get(0).equalsIgnoreCase("")) movie.schedule = null;
            movie.cinemas =
                    new ArrayList<>(
                            Arrays.asList(
                                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CINEMAS))
                                            .replace("[","")
                                            .replace("]","")
                                            .replace(" ","")
                                            .split(",")));
            if (movie.cinemas.get(0).equalsIgnoreCase("")) movie.cinemas = null;
            movie.updateTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATE_TIME));
            return movie;
        }
    }

}
