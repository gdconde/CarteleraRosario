package com.gdconde.cartelerarosario.data.local;

import android.database.Cursor;

import com.gdconde.cartelerarosario.data.model.Movie;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by gdconde on 13/2/17.
 */

public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    /**
     * Remove all the data from all the tables in the database.
     */
    public Observable<Void> clearTables() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    Cursor cursor = mDb.query("SELECT name FROM sqlite_master WHERE type='table'");
                    while (cursor.moveToNext()) {
                        mDb.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    }
                    cursor.close();
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    // Delete all movies in table and add the new ones.
    public Observable<Void> addMovies(final List<Movie> movies) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.MoviesTable.TABLE_NAME, null);
                    for (Movie movie : movies) {
                        mDb.insert(Db.MoviesTable.TABLE_NAME,
                                Db.MoviesTable.toContentValues(movie));
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Movie> findRegisteredMovie(final String id) {
        return Observable.create(new Observable.OnSubscribe<Movie>() {
            @Override
            public void call(Subscriber<? super Movie> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.MoviesTable.TABLE_NAME +
                                " WHERE " + Db.MoviesTable.COLUMN_ID + " = ?", id);
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.MoviesTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Void> addMovie(final Movie movie) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.insert(Db.MoviesTable.TABLE_NAME, Db.MoviesTable.toContentValues(movie));

                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<ArrayList<Movie>> getMovies() {
        return Observable.create(new Observable.OnSubscribe<ArrayList<Movie>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Movie>> subscriber) {
                Cursor cursor = mDb.query(
                    "SELECT * FROM " + Db.MoviesTable.TABLE_NAME);

                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.MoviesTable.parseCursor(cursor));
                }
            }
        });
    }

}
