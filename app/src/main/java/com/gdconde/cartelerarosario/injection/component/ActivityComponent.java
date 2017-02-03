package com.gdconde.cartelerarosario.injection.component;

import com.gdconde.cartelerarosario.injection.PerActivity;
import com.gdconde.cartelerarosario.injection.module.ActivityModule;
import com.gdconde.cartelerarosario.ui.base.BaseActivity;
import com.gdconde.cartelerarosario.ui.detail.DetailActivity;
import com.gdconde.cartelerarosario.ui.main.MainActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
}
