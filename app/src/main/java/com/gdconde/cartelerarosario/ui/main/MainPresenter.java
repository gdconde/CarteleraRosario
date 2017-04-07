package com.gdconde.cartelerarosario.ui.main;

import com.gdconde.cartelerarosario.data.DataManager;
import com.gdconde.cartelerarosario.data.model.HoytsAnswer;
import com.gdconde.cartelerarosario.data.model.Movie;
import com.gdconde.cartelerarosario.data.model.MovieDbAnswer;
import com.gdconde.cartelerarosario.injection.ConfigPersistent;
import com.gdconde.cartelerarosario.ui.base.BasePresenter;
import com.gdconde.cartelerarosario.util.WebParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private CompositeSubscription mSubscriptions;

    @Inject
    public MainPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        mSubscriptions.unsubscribe();
        mSubscriptions = null;
    }

}
