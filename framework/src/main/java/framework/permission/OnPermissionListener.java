package framework.permission;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public interface OnPermissionListener {
    void onPermissionRequestSuccess(String ... permission);

    void onPermissionRequestFailed(String ... permission);
}
