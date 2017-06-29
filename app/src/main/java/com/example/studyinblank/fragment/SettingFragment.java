package com.example.studyinblank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studyinblank.R;

import framework.app.BaseFragment;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/27 14:55
 *  @描述：    TODO
 */
public class SettingFragment extends BaseFragment {
    public SettingFragment(){
        setPageTag("fragment_ setting");
        initTitleBar("设置",true,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fra_setting,null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBackClick(new FragmentBack() {
            @Override
            public void onBackClick() {
                Toast.makeText(getActivity(),".....",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
