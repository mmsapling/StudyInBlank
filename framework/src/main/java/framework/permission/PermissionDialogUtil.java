package framework.permission;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.provider.Settings;

import framework.app.ActivityManager;
import framework.app.BaseApplication;
import framework.ui.CcbDialog;
import framework.utils.LogManager;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public class PermissionDialogUtil {
    private static PermissionDialogUtil mInstance = null;
    private PermissionDialogUtil(){

    }
    public synchronized static PermissionDialogUtil getInstance(){
        if(null == mInstance){
            mInstance = new PermissionDialogUtil();
        }
        return mInstance;
    }
    public void showPermissionDeniedDialog(final Context context, final OnPermisssionDialogClickListener listener,String... permisssions){
        CcbDialog.getInstance().showDialog(context, "", String.format("在设置—应用—StudyInBlank—权限中开启%s权限,以正常使用StudyInBlank功能", getPermissionLabel(permisssions)), "取消", new CcbDialog.OnClickCancelListener() {
            @Override
            public void clickCancel(Dialog dialog) {
                dialog.dismiss();
                if (null == listener) {
                    return;
                }
                listener.onCancelClick();
            }
        }, "去设置", new CcbDialog.OnClickConfirmListener() {
            @Override
            public void clickConfirm(Dialog dialog) {
                dialog.dismiss();
                if(null == listener){
                    jumpSetting(context);
                    return;
                }
                listener.onSettingClick(context);
            }
        },false);
    }

     void jumpSetting(Context context) {
        if(null == context){
            context = ActivityManager.getInstance().getTopAcitivity();
        }
        Intent jumpIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(String.format("package:%s",context.getPackageName())));
        jumpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(jumpIntent);
    }

    public String getPermissionLabel(String ...permisssions){
        StringBuffer label = new StringBuffer();
        try{
            PackageManager pm = BaseApplication.getInstance().getPackageManager();
            for(String p : permisssions){
                PermissionInfo pi = pm.getPermissionInfo(p, 0);
                PermissionGroupInfo pgi = pm.getPermissionGroupInfo(pi.group, 0);
                label.append(pgi.loadLabel(pm).toString());
                label.append("、");
            }
            label.delete(label.length() - 1,label.length());
        }catch (Exception e){
            LogManager.logE("=============permission_desc loading error===========");
        }
        return label.toString();
    }
}
