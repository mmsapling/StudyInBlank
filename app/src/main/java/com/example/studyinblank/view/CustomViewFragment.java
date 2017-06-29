package com.example.studyinblank.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.studyinblank.R;

import framework.app.BaseFragment;
import framework.ui.CcbDialog;
import framework.utils.ToastUtils;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public class CustomViewFragment extends BaseFragment implements View.OnClickListener {
    public CustomViewFragment(){
        setPageTag("fragment_custom_view");
        initTitleBar("自定义控件Demo",true,false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_custom_view, null);
        view.findViewById(R.id.btn_show_dialog).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_show_dialog:
                CcbDialog.getInstance().showDialog(getActivity(),"", "测试弹窗", "取消", new CcbDialog.OnClickCancelListener() {
                    @Override
                    public void clickCancel(Dialog dialog) {
                        ToastUtils.showToast("取消");
                    }
                },"确定",new CcbDialog.OnClickConfirmListener(){

                    @Override
                    public void clickConfirm(Dialog dialog) {
                        ToastUtils.showToast("确定");
                    }
                },true);
                break;
        }
    }
}
