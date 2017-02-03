package com.gdconde.cartelerarosario;

import android.app.Application;
import android.content.Context;

import com.gdconde.cartelerarosario.injection.component.ApplicationComponent;
import com.gdconde.cartelerarosario.injection.component.DaggerApplicationComponent;
import com.gdconde.cartelerarosario.injection.module.ApplicationModule;

import timber.log.Timber;

public class App extends Application  {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
