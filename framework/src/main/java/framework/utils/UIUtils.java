package framework.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;

import framework.app.BaseApplication;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/20 16:42
 *  @描述：    TODO
 */
public class UIUtils {
    /**
     * dp-->px
     */
    public static int dp2Px(Context context, int dp) {
        //1.px/dp = density    ==> px和dp倍数关系
        //2.px/(ppi/160) = dp  ==>ppi

        float density = context.getResources().getDisplayMetrics().density; //1.5
        //        int ppi = getResources().getDisplayMetrics().densityDpi;//160 240 320

        int px = (int) (dp * density + .5f);
        return px;
    }

    /**
     * px-->dp
     */
    public static int px2Dp(Context context, int px) {
        //1.px/dp = density    ==> px和dp倍数关系
        float density = context.getResources().getDisplayMetrics().density; //1.5
        int dp = (int) (px / density + .5f);
        return dp;
    }

    /**
     * 获取target版本信息
     * @return
     */
    public static int getTargetSdkVersion() {
        int version = 0;
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
            version = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            LogManager.logE("==========get package info error===========");
        }
        return version;
    }

    /**
     * 检查是否有权限
     * @param permission
     * @return
     */
    public static boolean hasAppPermission(String permission){
        if(TextUtils.isEmpty(permission))
            return true;
        if(getTargetSdkVersion() < Build.VERSION_CODES.M || Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            LogUtils.debug("if...");
            return PackageManager.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(BaseApplication.getInstance(),permission);
        }else{
            LogUtils.debug("else...");
            return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BaseApplication.getInstance(),permission);
        }
    }

}
