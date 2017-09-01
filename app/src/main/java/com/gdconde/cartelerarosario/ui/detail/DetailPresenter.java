package com.gdconde.cartelerarosario.ui.detail;


import com.gdconde.cartelerarosario.data.DataManager;
import com.gdconde.cartelerarosario.data.model.MovieDetail;
import com.gdconde.cartelerarosario.injection.ConfigPersistent;
import com.gdconde.cartelerarosario.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager mDataManager;

    @Inject
    public DetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void getMovieDetails(String movieId) {
        checkViewAttached();
        getMvpView().showProgress(true);
        mDataManager
                .getMovieDetail(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<MovieDetail>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull MovieDetail movieDetail) {
                        getMvpView().showProgress(false);
                        getMvpView().showMovie(movieDetail);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().showProgress(false);
                        getMvpView().showError();
                        Timber.e(e, "There was a problem retrieving the movie...");
                    }
                });
    }

}
