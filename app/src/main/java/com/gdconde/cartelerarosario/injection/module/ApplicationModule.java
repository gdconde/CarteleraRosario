package com.gdconde.cartelerarosario.injection.module;

import android.app.Application;
import android.content.Context;

import com.gdconde.cartelerarosario.data.remote.ElCairoService;
import com.gdconde.cartelerarosario.data.remote.ElCairoServiceFactory;
import com.gdconde.cartelerarosario.data.remote.HoytsService;
import com.gdconde.cartelerarosario.data.remote.HoytsServiceFactory;
import com.gdconde.cartelerarosario.data.remote.ShowcaseService;
import com.gdconde.cartelerarosario.data.remote.ShowcaseServiceFactory;
import com.gdconde.cartelerarosario.data.remote.TheMovieDbService;
import com.gdconde.cartelerarosario.data.remote.TheMovieDbServiceFactory;
import com.gdconde.cartelerarosario.data.remote.VillageService;
import com.gdconde.cartelerarosario.data.remote.VillageServiceFactory;
import com.gdconde.cartelerarosario.injection.ApplicationContext;

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
    ElCairoService provideElCairoService() {
        return ElCairoServiceFactory.makeSecretsService();
    }

    @Provides
    @Singleton
    TheMovieDbService provideTheMovieDbService() {
        return TheMovieDbServiceFactory.makeSecretsService();
    }

    @Provides
    @Singleton
    ShowcaseService provideShowcaseService() {
        return ShowcaseServiceFactory.makeSecretsService();
    }

    @Provides
    @Singleton
    HoytsService provideHoytsService() {
        return HoytsServiceFactory.makeSecretsService();
    }

    @Provides
    @Singleton
    VillageService provideVillageService() {
        return VillageServiceFactory.makeSecretsService();
    }
}
