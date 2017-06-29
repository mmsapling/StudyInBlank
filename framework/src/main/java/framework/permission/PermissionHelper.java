package framework.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import framework.utils.UIUtils;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public class PermissionHelper {
    private static PermissionHelper mInstance = null;
    private OnPermissionListener mListener;
    final static String BUNDLE_PERMISSION = "bundle_permission";
    final static int PERMISSION_REQUEST_CODE = 0xFF00;
    private Context mContext;
    private PermissionHelper(){}
    public synchronized static PermissionHelper getInstance(){
        if(null == mInstance){
            mInstance = new PermissionHelper();
        }
        return mInstance;
    }
    public void requestPermission(Context context, OnPermissionListener listener, String... permissions){
        this.mListener = listener;
        this.mContext = context;
        if(UIUtils.getTargetSdkVersion() < Build.VERSION_CODES.M || Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            listener.onPermissionRequestFailed(permissions);
            return ;
        }
        Intent JumpIntent = new Intent(context,RequestPermisssionActivity.class);
        JumpIntent.putExtra(BUNDLE_PERMISSION,permissions);
        mContext.startActivity(JumpIntent);
    }
    OnPermissionListener getListener(){
        return mListener;
    }
}
