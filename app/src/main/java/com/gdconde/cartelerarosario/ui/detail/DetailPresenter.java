package com.gdconde.cartelerarosario.ui.detail;


import com.gdconde.cartelerarosario.data.DataManager;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.injection.ConfigPersistent;
import com.gdconde.cartelerarosario.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;

    @Inject
    public DetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
        mSubscriptions = null;
    }



    public void getMovieDetails(String movieId) {
        checkViewAttached();
        getMvpView().showProgress(true);
        mSubscriptions.add(mDataManager.getMovieDetail(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<Movie>() {
                    @Override
                    public void onSuccess(Movie value) {
                        getMvpView().showProgress(false);
                        getMvpView().showMovie(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        getMvpView().showProgress(false);
                        getMvpView().showError();
                        Timber.e(error, "There was a problem retrieving the movie...");
                    }
                }));
    }

}
