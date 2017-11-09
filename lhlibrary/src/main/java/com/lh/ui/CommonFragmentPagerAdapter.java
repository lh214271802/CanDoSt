package com.lh.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by liaohui on 2017/9/20.
 */

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public CommonFragmentPagerAdapter(FragmentManager fm, @NonNull List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null) return null;
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        if (fragments == null) return 0;
        return fragments.size();
    }
}
