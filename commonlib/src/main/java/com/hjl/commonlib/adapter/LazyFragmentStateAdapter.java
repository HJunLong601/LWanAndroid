package com.hjl.commonlib.adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * author: long
 * description ViewPager Adapter for ViewPager2
 * Date: 2020/5/26
 */
public class LazyFragmentStateAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;

    public LazyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity,List<Fragment> fragments) {
        super(fragmentActivity);
        this.mFragments = fragments;
    }

    public LazyFragmentStateAdapter(@NonNull Fragment fragment,List<Fragment> fragments) {
        super(fragment);
        this.mFragments = fragments;
    }

    public LazyFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
