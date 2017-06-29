package com.example.studyinblank.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.studyinblank.R;
import com.google.zxing.Result;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import framework.app.BaseFragment;
import framework.utils.LogUtils;
import framework.utils.ToastUtils;
import framework.zxing.DecodeManager;

/**
 * Created by cxw on 2017/5/9.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/9
 * @描述: TODO
 */
public class ScanDemoFragment extends BaseFragment implements View.OnClickListener {
    public ScanDemoFragment(){
        initTitleBar("二维码扫描",true,false);
    }
    private boolean mCanScan = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_scan_demo, null);
        view.findViewById(R.id.btn_scan).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DecodeManager.getInstance().attachToActivity(getActivity(),mResultListener);
        String name = "1111111111111111111111111111111111111111";
        try{
            FileOutputStream stream = getActivity().openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(name);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private DecodeManager.IDecodeResultListener mResultListener = new DecodeManager.IDecodeResultListener() {
        @Override
        public void handleDecode(Result obj, Bitmap barcode) {
            LogUtils.debug(obj.getText().toString());
            ToastUtils.showToast("result========================" + obj.getText().toString());
            mCanScan = false;
        }
    };

    @Override
    public void onResume() {
        DecodeManager.getInstance().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        DecodeManager.getInstance().onPause();

    }

    @Override
    public void onDestroy() {
        DecodeManager.getInstance().onPause();
        super.onDestroy();
    }



    @Override
    public void onClick(View v) {
        if (DecodeManager.getInstance().isFlashLightOpen()) {
            ((Button)v).setText("Open FlashLight");
            DecodeManager.getInstance().closeFlashLight();
            DecodeManager.getInstance().deCodeFromImage(Environment.getExternalStorageDirectory()+"/1465378713.png",mResultListener);
            return;
        }
        DecodeManager.getInstance().openFlashLight();
        ((Button)v).setText("Close FlashLight");
    }
}
