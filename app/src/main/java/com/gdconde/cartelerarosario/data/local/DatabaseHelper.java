package com.gdconde.cartelerarosario.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdconde.cartelerarosario.data.model.Movie;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


/**
 * Created by gdconde on 13/2/17.
 */

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Movie> addMovies(final Collection<Movie> newMovies) {
        return Observable.create(new Observable.OnSubscribe<Movie>() {
            @Override
            public void call(Subscriber<? super Movie> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    for(Movie movie : newMovies) {
                        long result = mDb.insert(Db.MoviesTable.TABLE_NAME,
                                Db.MoviesTable.toContentValues(movie),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if(result >= 0) subscriber.onNext(movie);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Movie>> getMovies() {
        return mDb.createQuery(Db.MoviesTable.TABLE_NAME,
                "SELECT * FROM " + Db.MoviesTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, Movie>() {
                    @Override
                    public Movie call(Cursor cursor) {
                        return Db.MoviesTable.parseCursor(cursor);
                    }
                });
    }


}
