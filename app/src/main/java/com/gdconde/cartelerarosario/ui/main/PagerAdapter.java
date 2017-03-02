package com.gdconde.cartelerarosario.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by gdconde on 2/3/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumberOfTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.mNumberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            MoviesFragment tabMovies = new MoviesFragment();
            return tabMovies;
        } else if (position == 1) {
            CinemasFragment tabCinemas = new CinemasFragment();
            return tabCinemas;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mNumberOfTabs;
    }
}
