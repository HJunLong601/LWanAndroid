package com.hjl.commonlib.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * created by long on 2019/11/23
 */
public class HealthyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Class> list;
    private List<String> mTitles;

    public HealthyFragmentPagerAdapter(FragmentManager fm, List<Class> list) {
        super(fm);
        this.list = list;
    }

    public HealthyFragmentPagerAdapter(FragmentManager fm, List<Class> list, List<String> titles) {
        super(fm);
        this.list = list;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int i) {

        try {
            return (Fragment) list.get(i).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            return mTitles.get(position);
        }
        return super.getPageTitle(position);
    }

}
