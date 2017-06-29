package com.example.studyinblank.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import framework.app.BaseFragment;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/27 9:55
 *  @描述：    TODO
 */
public class MainTabShowFragmentAdapter extends FragmentPagerAdapter {
    private BaseFragment[] mFragments;
    public MainTabShowFragmentAdapter(FragmentManager fm, BaseFragment[] fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        if(mFragments != null && mFragments.length != 0){
            return mFragments[position];
        }
        return null;
    }

    @Override
    public int getCount() {
        if(mFragments != null && mFragments.length != 0){
            return mFragments.length;
        }
        return 0;
    }
}
