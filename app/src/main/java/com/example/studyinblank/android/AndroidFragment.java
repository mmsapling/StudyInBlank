package com.example.studyinblank.android;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.studyinblank.R;
import com.example.studyinblank.adapter.MainAdapter;

import java.util.Arrays;
import java.util.List;

import framework.app.ActivityManager;
import framework.app.BaseFragment;
import framework.mvp.UserLoginActivity;

/**
 * Created by cxw on 2017/6/27.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/6/27
 * @描述: TODO
 */

public class AndroidFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private MainAdapter mAdapter;

    public AndroidFragment(){
        initTitleBar("Android知识学习",true,false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = new ListView(getActivity());
        return mListView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new MainAdapter(getActivity(), getDatasByRes(R.array.android_data));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private List<String> getDatasByRes(@ArrayRes int resId) {
        String[] strings = getResources().getStringArray(resId);
        return Arrays.asList(strings);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                ActivityManager.getInstance().startActivity(getActivity(), FragmentLifeFra.class);
                break;
        }
    }
}
