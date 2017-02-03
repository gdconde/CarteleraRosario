package com.gdconde.cartelerarosario.injection.component;

import android.app.Application;
import android.content.Context;

import com.gdconde.cartelerarosario.data.DataManager;
import com.gdconde.cartelerarosario.data.remote.ElCairoService;
import com.gdconde.cartelerarosario.data.remote.TheMovieDbService;
import com.gdconde.cartelerarosario.injection.ApplicationContext;
import com.gdconde.cartelerarosario.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    DataManager dataManager();
    ElCairoService elCairoService();
    TheMovieDbService theMovieDbService();
}
