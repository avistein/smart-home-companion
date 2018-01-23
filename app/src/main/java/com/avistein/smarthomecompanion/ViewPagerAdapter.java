package com.avistein.smarthomecompanion;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



/**
 * Created by avistein on 24/12/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 2;
    private static final String[] TITLES= new String[]{"Ring Bell", "Light Controls"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new RingBellFragment();
            case 1:
                return new LightControlsFragment();
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return TITLES[position];
    }
}
