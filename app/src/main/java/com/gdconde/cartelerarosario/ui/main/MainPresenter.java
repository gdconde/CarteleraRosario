package com.gdconde.cartelerarosario.ui.main;

import com.gdconde.cartelerarosario.injection.ConfigPersistent;
import com.gdconde.cartelerarosario.ui.base.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    @Inject
    public MainPresenter() {
    }

}
