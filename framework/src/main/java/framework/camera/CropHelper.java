package framework.camera;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import framework.app.BaseApplication;
import framework.config.AppGlobal;
/**
 * Created by cxw on 2017/5/8.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/8
 * @描述: 截图工具类
 */
public class CropHelper {
    private static CropHelper mInstance  = null;
    private Context mContext;
    private OnCropListener mListener;
    private String mLocalPath;
    private CropHelper(){}
    public synchronized  static CropHelper getInstance(){
        if(null == mInstance){
            mInstance = new CropHelper();
        }
        return mInstance;
    }
    OnCropListener getCropListener(){
        return mListener;
    }
    String getLocalPath(){
        return mLocalPath;
    }
    /**
     * 跳转相机截取图片(默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     */
    public void startCropFromCamera(Context context,OnCropListener l){
        startCropFromCamera(context,null,l);
    }

    /**
     * 跳转相机截取图片 (默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     */
    public void startCropFromCamera(Context context,String saveLocalPath,OnCropListener l){
        startCropFromCamera(context,saveLocalPath,AppGlobal.CROP_TYPE_CAMERA,l);

    }
    /**
     * 跳转相机截取图片 (默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     * @param requestType 请求类型
     */
    private void startCropFromCamera(Context context,String saveLocalPath,String requestType,OnCropListener l){
        this.mLocalPath = TextUtils.isEmpty(saveLocalPath) ? AppGlobal.IMAGE_FILE_LOCATION : saveLocalPath;
        this.mContext = context;
        this.mListener = l ;
        Intent jumpIntent = new Intent(context,CropActivity.class);
        jumpIntent.putExtra(AppGlobal.CROP_INTENT_TYPE,requestType);
        context.startActivity(jumpIntent);

    }
    /**
     * 调用相机
     * @param context 上下文
     * @param saveLocalPath 保存路径
     * @param l 监听
     */
    public void callCamera(Context context,String saveLocalPath,OnCropListener l){
        startCropFromCamera(context,saveLocalPath,AppGlobal.TYPE_CALL_CAMERA,l);

    }

    /**
     * 跳转相册截取图片 (默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     */
    public void startCropFromGallery(Context context,OnCropListener l){
        startCropFromGallery(context,null,l);
    }

    /**
     * 跳转相机截取图片
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     */
    public void startCropFromGallery(Context context,String saveLocalPath,OnCropListener l){
        startCropFromGallery(context,l,saveLocalPath,AppGlobal.CROP_TYPE_GALLERY);
    }

    /**
     *
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     * @param requestType 请求类型
     */
    private void startCropFromGallery(Context context,OnCropListener l,String saveLocalPath,String requestType){
        this.mLocalPath = TextUtils.isEmpty(saveLocalPath) ? AppGlobal.IMAGE_FILE_LOCATION : saveLocalPath;
        this.mContext = context;
        this.mListener = l ;
        Intent jumpIntent = new Intent(context,CropActivity.class);
        jumpIntent.putExtra(AppGlobal.CROP_INTENT_TYPE,requestType);
        context.startActivity(jumpIntent);
    }

    /**
     * 调用相册
     * @param context 上下文
     * @param l 监听
     */
    public void callGallery(Context context,OnCropListener l){
        startCropFromCamera(context,null,AppGlobal.TYPE_CALL_GALLERY,l);
    }

    public String getRealPathByUri(Uri uri){

        if(null == uri)
            return "";
        String [] ps = {MediaStore.Images.Media.DATA};
        Cursor pathCursor = new CursorLoader(BaseApplication.getInstance(),uri,ps,null,null,null).loadInBackground();
        if(null == pathCursor )
            return uri.getPath();
        pathCursor.moveToFirst();
        return pathCursor.getString(pathCursor.getColumnIndex(MediaStore.Images.Media.DATA));
    }
}
