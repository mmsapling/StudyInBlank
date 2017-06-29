package framework.camera;

import android.graphics.Bitmap;

/**
 * Created by cxw on 2017/5/8.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/8
 * @描述: 截图监听器
 */
public abstract class OnCropListener {
    public void onCropComplete(Bitmap bitmap){}
    public void onFailed(int requestCode,int resultCode,String suggestErrMsg){}
    public void onChooseFinish(String photoPath){}
}
