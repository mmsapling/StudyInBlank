package framework.config;

import android.os.Environment;

/**
 * Created by cxw on 2017/5/8.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/8
 * @描述: TODO
 */
public class AppGlobal {
    /*********************************** 截图相关 **************************************/

    public static final String IMAGE_FILE_LOCATION = "file://" + Environment.getExternalStorageDirectory().getPath() + "/tmp.png";
    /**
     * 请求码跳转相机
     */
    public static final int CCB_REQUEST_CODE_OPEN_CAMERA = 0xFF05;
    /**
     * 请求码跳转相册
     */
    public static final int CCB_REQUEST_CODE_ALBUM = 0xFF06;
    public static final int CCB_REQUEST_CODE_CLIP = 0xFF07;
    public static final String CROP_INTENT_TYPE = "crop_type";
    public static final String CROP_TYPE_CAMERA = "crop_camera";
    public static final String TYPE_CALL_CAMERA = "call_camera";
    public static final String TYPE_CALL_GALLERY = "call_gallery";
    public static final String CROP_TYPE_GALLERY = "crop_gallery";
}
