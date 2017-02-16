package com.gdconde.cartelerarosario.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import com.gdconde.cartelerarosario.data.model.Movie;

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

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " TEXT PRIMARY KEY, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_POSTER + " TEXT, " +
                        COLUMN_BACKDROP + " TEXT, " +
                        COLUMN_SINOPSIS + " TEXT, " +
                        COLUMN_RELEASE_DATE + " TEXT, " +
//                        COLUMN_GENRES + " TEXT," +
                        COLUMN_LANGUAGE + " TEXT" +
                        /*COLUMN_SCHEDULE + " TEXT," +
                        COLUMN_CINEMAS + " TEXT" +*/
                        " );";

        public static ContentValues toContentValues(Movie movie) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, movie.id);
            values.put(COLUMN_TITLE, movie.title);
            values.put(COLUMN_POSTER, movie.posterPath);
            values.put(COLUMN_BACKDROP, movie.backdropPath);
            values.put(COLUMN_SINOPSIS, movie.sinopsis);
            values.put(COLUMN_RELEASE_DATE, movie.releaseDate);
            values.put(COLUMN_LANGUAGE, movie.originalLanguage);
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
            movie.originalLanguage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGE));
            return movie;
        }
    }

}
