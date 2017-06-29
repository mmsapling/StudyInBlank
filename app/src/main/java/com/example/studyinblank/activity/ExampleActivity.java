package com.example.studyinblank.activity;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.studyinblank.R;
import com.example.studyinblank.adapter.MainAdapter;
import com.example.studyinblank.example.CameraFragment;
import com.example.studyinblank.view.CustomViewFragment;
import com.example.studyinblank.example.HtmlDemoBackFragment;
import com.example.studyinblank.example.HtmlDemoFragment;
import com.example.studyinblank.example.PermissionFragment;
import com.example.studyinblank.example.ScanDemoFragment;
import com.example.studyinblank.fragment.SettingFragment;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import framework.annotation.GenericDemo;
import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.mvp.UserLoginActivity;


public class ExampleActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView mListView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        useDefaultTitle("学习用例入口", true, false);
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new MainAdapter(this, getDatasByRes(R.array.example_data));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private List<String> getDatasByRes(@ArrayRes int resId) {
        String[] strings = getResources().getStringArray(resId);
        return Arrays.asList(strings);
    }

    @Override
    public void onClick(View view) {

    }

    private void test() {
        try {
            Method method = GenericDemo.class.getMethod("add", Vector.class);
            Type[] types = method.getGenericParameterTypes();
            for (Type type : types) {
                Log.d("tylz", type.toString());
            }
            if (types[0] != null) {
                ParameterizedType parameterizedTypes = (ParameterizedType) types[0];
                Log.d("tylz", parameterizedTypes.getRawType().toString());
                Log.d("tylz", parameterizedTypes.getActualTypeArguments()[0].toString());
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.d("tylz", "没有add方法");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                ActivityManager.getInstance().startActivity(this, UserLoginActivity.class);
                break;
            case 1:
                ActivityManager.getInstance().startActivity(this, CustomViewFragment.class);
                break;
            case 2:
                ActivityManager.getInstance().startFragment(this, new SettingFragment());
                break;
            case 3:
                ActivityManager.getInstance().startActivity(this, CameraFragment.class);
                break;
            case 4:
                ActivityManager.getInstance().startActivity(this, PermissionFragment.class);
                break;
            case 5:
                ActivityManager.getInstance().startActivity(this, ScanDemoFragment.class);
                break;
            case 6:
                ActivityManager.getInstance().startActivity(this, HtmlDemoFragment.class);
                break;
            case 7:
                ActivityManager.getInstance().startActivity(this, HtmlDemoBackFragment.class);
                break;
        }
    }
}
