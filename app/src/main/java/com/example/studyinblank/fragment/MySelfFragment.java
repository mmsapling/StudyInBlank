package com.example.studyinblank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studyinblank.R;

import framework.app.BaseFragment;
import framework.utils.LogUtils;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/27 9:11
 *  @描述：    TODO
 */
public class MySelfFragment extends BaseFragment {
    public MySelfFragment(){
        initTitleBar("我的",false,false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.debug(getClass().getSimpleName() + "--->setUserVisibleHint()---> isVisibleToUser = " + isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return View.inflate(getActivity(), R.layout.fra_myself,null);
    }
}
