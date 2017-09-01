package com.gdconde.cartelerarosario.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdconde.cartelerarosario.data.model.Movie;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gdconde on 13/2/17.
 */

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = new SqlBrite.Builder().build().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public boolean isMoviesTableEmptyOrOutdated() {
        Cursor cursor1 = mDb.query("SELECT count(*) FROM " + Db.MoviesTable.TABLE_NAME);
        cursor1.moveToFirst();
        boolean isMoviesTableEmpty = cursor1.getInt(0) == 0;
        if (isMoviesTableEmpty) return true;

        Cursor cursor2 = mDb.query("SELECT count(*) FROM " + Db.MoviesTable.TABLE_NAME +
                " WHERE " + Db.MoviesTable.COLUMN_UPDATE_TIME + " = null");
        cursor2.moveToFirst();
        boolean isMoviesTableBadlyFilled = cursor2.getInt(0) > 0;
        if (isMoviesTableBadlyFilled) return true;

        Cursor cursor3 = mDb.query("SELECT MAX(" + Db.MoviesTable.COLUMN_UPDATE_TIME + ") FROM " +
                Db.MoviesTable.TABLE_NAME);
        cursor3.moveToFirst();
        long timeWithoutUpdate = Calendar.getInstance().getTimeInMillis() / 1000 -
                cursor3.getLong(0);
        return timeWithoutUpdate > 7 * 24 * 60 * 60;

    }


    public Observable<Movie> addMovies(final Collection<Movie> newMovies) {
        return Observable.create(new ObservableOnSubscribe<Movie>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Movie> e) throws Exception {
                if (e.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    for(Movie movie : newMovies) {
                        long result = mDb.insert(Db.MoviesTable.TABLE_NAME,
                                Db.MoviesTable.toContentValues(movie),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if(result >= 0) e.onNext(movie);
                    }
                    transaction.markSuccessful();
                    e.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Movie> addMovie(final Movie movie) {
        return Observable.create(new ObservableOnSubscribe<Movie>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Movie> e) throws Exception {
                if (e.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.insert(Db.MoviesTable.TABLE_NAME,
                            Db.MoviesTable.toContentValues(movie),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    transaction.markSuccessful();
                    e.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Movie>> getMovies() {
        return mDb.createQuery(Db.MoviesTable.TABLE_NAME,
                "SELECT * FROM " + Db.MoviesTable.TABLE_NAME)
                .mapToList(new Function<Cursor, Movie>() {
                    @Override
                    public Movie apply(@NonNull Cursor cursor) throws Exception {
                        return Db.MoviesTable.parseCursor(cursor);
                    }
                });
    }


}
