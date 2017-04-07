package com.gdconde.cartelerarosario.injection.component;

import com.gdconde.cartelerarosario.injection.PerFragment;
import com.gdconde.cartelerarosario.injection.module.FragmentModule;
import com.gdconde.cartelerarosario.ui.base.BaseFragment;
import com.gdconde.cartelerarosario.ui.main.CinemasFragment;
import com.gdconde.cartelerarosario.ui.main.MoviesFragment;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(BaseFragment baseFragment);
    void inject(MoviesFragment moviesFragment);
    void inject(CinemasFragment cinemasFragment);
}