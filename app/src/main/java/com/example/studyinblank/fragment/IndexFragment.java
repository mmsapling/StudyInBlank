package com.example.studyinblank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.studyinblank.activity.ExampleActivity;
import com.example.studyinblank.R;
import com.example.studyinblank.android.AndroidFragment;
import com.example.studyinblank.view.CustomViewFragment;

import framework.app.ActivityManager;
import framework.app.BaseFragment;
import framework.utils.LogUtils;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/27 9:12
 *  @描述：    TODO
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtnExample;
    private Button mBtnAndroid;
    private Button mBtnView;
    public IndexFragment(){
        initTitleBar("首页",false,false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.debug(getClass().getSimpleName() + "--->setUserVisibleHint()---> isVisibleToUser = " + isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fra_index, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mBtnExample = (Button) rootView.findViewById(R.id.btn_example);
        mBtnAndroid = (Button) rootView.findViewById(R.id.btn_android);
        mBtnView = (Button) rootView.findViewById(R.id.btn_view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnAndroid.setOnClickListener(this);
        mBtnExample.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_android:
                ActivityManager.getInstance().startActivity(getActivity(), AndroidFragment.class);
                break;
            case R.id.btn_example:
                ActivityManager.getInstance().startActivity(getActivity(), ExampleActivity.class);
                break;
            case R.id.btn_view:
                ActivityManager.getInstance().startActivity(getActivity(), CustomViewFragment.class);
                break;
        }
    }
}
