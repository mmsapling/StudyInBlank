package framework.utils;

import android.app.Activity;

import android.widget.Toast;

import framework.app.ActivityManager;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public class ToastUtils {
    public static void showToast(String msg){
        Toast.makeText(ActivityManager.getInstance().getTopAcitivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
