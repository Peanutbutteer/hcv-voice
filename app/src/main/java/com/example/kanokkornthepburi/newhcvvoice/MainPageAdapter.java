package com.example.kanokkornthepburi.newhcvvoice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by kanokkornthepburi on 5/20/2017 AD.
 */

public class MainPageAdapter extends FragmentStatePagerAdapter {
    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return VoiceControlsFragment.newInstance();
        } else {
            return GraphFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Voice Control";
        } else {
            return "Graph";
        }
    }
}
