package com.hitherejoe.mvpboilerplate.injection.module;

import android.app.Application;
import android.content.Context;

import com.hitherejoe.mvpboilerplate.data.remote.ElCairoService;
import com.hitherejoe.mvpboilerplate.data.remote.ElCairoServiceFactory;
import com.hitherejoe.mvpboilerplate.data.remote.MvpBoilerplateService;
import com.hitherejoe.mvpboilerplate.data.remote.MvpBoilerplateServiceFactory;
import com.hitherejoe.mvpboilerplate.data.remote.TheMovieDbService;
import com.hitherejoe.mvpboilerplate.data.remote.TheMovieDbServiceFactory;
import com.hitherejoe.mvpboilerplate.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    MvpBoilerplateService provideMvpBoilerplateService() {
        return MvpBoilerplateServiceFactory.makeSecretsService();
    }

    @Provides
    @Singleton
    ElCairoService provideElCairoService() {
        return ElCairoServiceFactory.makeSecretsService();
    }

    @Provides
    @Singleton
    TheMovieDbService provideTheMovieDbService() {
        return TheMovieDbServiceFactory.makeSecretsService();
    }
}
