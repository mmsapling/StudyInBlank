package framework.permission;

import android.content.Context;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public abstract class OnPermisssionDialogClickListener
{
    public void onSettingClick(Context context){
        PermissionDialogUtil.getInstance().jumpSetting(context);
    }
    public abstract void onCancelClick();
}
