package framework.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public class RequestPermisssionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
        Intent i = getIntent();
        if(null == i){
            finish();
            return;
        }
        String[] permisssions = i.getStringArrayExtra(PermissionHelper.BUNDLE_PERMISSION);
        if(null == permisssions || 0 == permisssions.length){
            finish();
            return;
        }
        ActivityCompat.requestPermissions(this,permisssions,PermissionHelper.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        finish();
        if(requestCode != PermissionHelper.PERMISSION_REQUEST_CODE){
            return;
        }
        List<String> deniedList = new ArrayList();
        List<String> grantedList = new ArrayList();
        for(int i = 0,count = permissions.length; i < count;i++){
            String permission = permissions[i];
            int result = grantResults[i];
            if(result == PackageManager.PERMISSION_GRANTED){
                grantedList.add(permission);
            }else{
                deniedList.add(permission);
            }
        }
        OnPermissionListener listener = PermissionHelper.getInstance().getListener();
        if(null != listener){
            if(deniedList.size() > 0){
                listener.onPermissionRequestFailed(deniedList.toArray(new String[deniedList.size()]));
            }
            if(grantedList.size() > 0){
                listener.onPermissionRequestSuccess(grantedList.toArray(new String[grantedList.size()]));
            }
        }
    }
}
