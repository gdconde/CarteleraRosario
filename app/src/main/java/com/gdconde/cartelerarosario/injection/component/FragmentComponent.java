package com.gdconde.cartelerarosario.injection.component;

import com.gdconde.cartelerarosario.injection.PerFragment;
import com.gdconde.cartelerarosario.injection.module.FragmentModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}