package com.example.studyinblank.android;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import framework.app.BaseFragment;
import framework.utils.LogUtils;

/**
 * Created by cxw on 2017/6/27.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/6/27
 * @描述: fragment生命周期
 */

public class FragmentLifeFra extends BaseFragment {

    private TextView mTvView;
    private StringBuffer  mSb = new StringBuffer();;
    private int mIndex = 0;
    public FragmentLifeFra() {
        initTitleBar("fragment生命周期", true, false);
        show("FragmentLifeFra()");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        show("onAttach()");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        show("onAttach()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTvView = new TextView(getActivity());
        show("onCreateView()");
        return mTvView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show("onActivityCreated()");
        mTvView.setText(mSb.toString());

    }

    @Override
    public void onStart() {
        super.onStart();
        show("onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        show("onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        show("onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        show("onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        show("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        show("onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        show("onDetach()");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.debug("setUserVisibleHint()---> isVisibleToUser = " + isVisibleToUser);
        show("setUserVisibleHint()---> isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public boolean getUserVisibleHint() {
        show("getUserVisibleHint() == " + super.getUserVisibleHint());
        return super.getUserVisibleHint();
    }


    private void show(String msg){
        LogUtils.debug(msg + "顺序" + mIndex);
        mSb.append(msg).append("顺序" + mIndex++).append("\n");
    }
}
