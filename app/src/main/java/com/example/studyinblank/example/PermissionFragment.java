package com.example.studyinblank.example;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studyinblank.R;

import framework.app.BaseFragment;
import framework.permission.OnPermissionListener;
import framework.permission.OnPermisssionDialogClickListener;
import framework.permission.PermissionDialogUtil;
import framework.permission.PermissionHelper;
import framework.utils.ToastUtils;
import framework.utils.UIUtils;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public class PermissionFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvVersion;
    public PermissionFragment(){
        setPageTag("fragment_permission");
        initTitleBar("权限判断",true,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_permission, null);
        view.findViewById(R.id.btn_permission).setOnClickListener(this);
        mTvVersion = (TextView) view.findViewById(R.id.tv_version);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StringBuffer data = new StringBuffer();
        data.append("TargetSdkVersion = ");
        data.append(UIUtils.getTargetSdkVersion());
        data.append("\n");
        data.append("Build.VERSION.SDK_INT = ");
        data.append(Build.VERSION.SDK_INT);
        data.append("\n");
        data.append("has camera permission = " + UIUtils.hasAppPermission(Manifest.permission.CAMERA));
        mTvVersion.setText(data.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_permission:
                if(!UIUtils.hasAppPermission(Manifest.permission.CAMERA)){
                    requestPermission();
                }
                break;
        }
    }

    private void requestPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        PermissionHelper.getInstance().requestPermission(getActivity(), new OnPermissionListener() {
            @Override
            public void onPermissionRequestSuccess(String... permission) {
                ToastUtils.showToast("成功" + permission);
            }

            @Override
            public void onPermissionRequestFailed(String... permission) {
                failPermission(permission);
            }
        },permissions);
    }

    private void failPermission(String[] permission) {
        PermissionDialogUtil.getInstance().showPermissionDeniedDialog(getActivity(), new OnPermisssionDialogClickListener() {
            @Override
            public void onCancelClick() {
                ToastUtils.showToast("反悔");
            }
        },permission);
    }
}
