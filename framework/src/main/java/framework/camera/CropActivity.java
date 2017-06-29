package framework.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import framework.config.AppGlobal;
import framework.utils.LogManager;

/**
 * Created by cxw on 2017/5/8.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/8
 * @描述: 截图监听结果处理
 */
public class CropActivity extends Activity {
    private String mRequestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
        mRequestType = getIntent().getStringExtra(AppGlobal.CROP_INTENT_TYPE);
        switch (mRequestType) {
            case AppGlobal.CROP_TYPE_CAMERA://拍照并截图
            case AppGlobal.TYPE_CALL_CAMERA://打开相机
                try {
                    MbsCameraApi.requestCameraTakePhotoWithCheck(this, AppGlobal.CCB_REQUEST_CODE_OPEN_CAMERA, Uri.parse(CropHelper.getInstance().getLocalPath()));
                } catch (Exception e) {
                    LogManager.logE(e.getMessage());
                    CropHelper.getInstance().getCropListener().onFailed(
                            AppGlobal.CCB_REQUEST_CODE_OPEN_CAMERA, 0, getSuggestionErrMsg(AppGlobal.CCB_REQUEST_CODE_OPEN_CAMERA));
                }
                break;
            case AppGlobal.CROP_TYPE_GALLERY://从相册选择并截图
            case AppGlobal.TYPE_CALL_GALLERY://从相册选择
                try {
                    MbsCameraApi.requestOpenAlbumWithCheck(this, AppGlobal.CCB_REQUEST_CODE_ALBUM);
                } catch (Exception e) {
                    LogManager.logE(e.getMessage());
                    CropHelper.getInstance().getCropListener().onFailed(
                            AppGlobal.CCB_REQUEST_CODE_ALBUM, 0, getSuggestionErrMsg(AppGlobal.CCB_REQUEST_CODE_ALBUM));
                }
                break;
        }
    }

    private String getSuggestionErrMsg(int requestCode) {
        switch (requestCode) {
            case AppGlobal.CCB_REQUEST_CODE_OPEN_CAMERA:
                return "您当前的摄像头不可用";
            case AppGlobal.CCB_REQUEST_CODE_ALBUM:
                return "您当前的相册不可用";
            case AppGlobal.CCB_REQUEST_CODE_CLIP:
                return "您当期的裁剪工具不可用";
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            CropHelper.getInstance().getCropListener().onFailed(requestCode,resultCode,getSuggestionErrMsg(requestCode));
            finish();
            return;
        }
        // 调用摄像头
        if (requestCode == AppGlobal.CCB_REQUEST_CODE_OPEN_CAMERA) {
            if (AppGlobal.TYPE_CALL_CAMERA.equals(mRequestType)) {
                CropHelper.getInstance().getCropListener().onChooseFinish(CropHelper.getInstance().getLocalPath());
                finish();
                return;
            }
            MbsCameraApi.startPhotoZoom(Uri.parse(CropHelper.getInstance().getLocalPath()), this, AppGlobal.CCB_REQUEST_CODE_CLIP);
            return;
        }

        // 调用相册
        if (requestCode == AppGlobal.CCB_REQUEST_CODE_ALBUM) {
            if (data != null) {
                Uri uri = data.getData();
                if (AppGlobal.TYPE_CALL_GALLERY.equals(mRequestType)) {
                    CropHelper.getInstance().getCropListener().onChooseFinish(CropHelper.getInstance().getRealPathByUri(uri));
                    finish();
                    return;
                }

                MbsCameraApi.startPhotoZoom(uri, this, AppGlobal.CCB_REQUEST_CODE_CLIP);
            }
        }

        // 裁切图片
        if (requestCode == AppGlobal.CCB_REQUEST_CODE_CLIP) {
            if (data != null) {
                final Bitmap bmp = MbsCameraApi.getBmpFromIntent(data);
                CropHelper.getInstance().getCropListener().onCropComplete(bmp);
            } else {
                CropHelper.getInstance().getCropListener().onFailed(requestCode, resultCode, getSuggestionErrMsg(requestCode));
            }

            finish();
        }
    }
}
