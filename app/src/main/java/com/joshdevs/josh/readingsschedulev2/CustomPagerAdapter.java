package com.joshdevs.josh.readingsschedulev2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.joshdevs.josh.readingsschedulev2.Models.ImportModel;

/**
 * Created by Josh on 2015-10-14.
 */
public class CustomPagerAdapter extends FragmentPagerAdapter {
    public static final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"By Course", "By Date"};
    private Context context;
    private Bundle bundle;

    public CustomPagerAdapter(FragmentManager fm, Context context, Bundle bundle) {
        super(fm);
        this.context = context;
        this.bundle = bundle;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ByCourseFragment.newInstance((ImportModel) bundle.getParcelable("import"));
            case 1:
            default:
                return ByDateFragment.newInstance((ImportModel) bundle.getParcelable("import"));



        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
