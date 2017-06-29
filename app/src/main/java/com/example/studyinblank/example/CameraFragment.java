package com.example.studyinblank.example;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.studyinblank.R;

import framework.app.BaseFragment;
import framework.camera.CropHelper;
import framework.camera.MbsCameraApi;
import framework.camera.OnCropListener;
import framework.permission.OnPermissionListener;
import framework.permission.OnPermisssionDialogClickListener;
import framework.permission.PermissionDialogUtil;
import framework.permission.PermissionHelper;
import framework.utils.LogUtils;
import framework.utils.ToastUtils;
import framework.utils.UIUtils;

/**
 * Created by cxw on 2017/5/8.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/8
 * @描述: TODO
 */
public class CameraFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_TAKE_PHOTO = 1000;
    private static final int REQUEST_CODE_PHOTO = 1001;
    private Button mBtnTakePhoto;
    private Button mBtnPhoto;
    private Button mBtnPhotoJieTu;
    private Button mBtnTakePhotoJieTu;
    private ImageView mIvImage;

    public CameraFragment() {
        initTitleBar("相机相关", true, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_camera, null);
        mBtnTakePhoto = (Button) view.findViewById(R.id.btn_take_photo);
        mBtnPhoto = (Button) view.findViewById(R.id.btn_photo);
        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mBtnPhotoJieTu = (Button) view.findViewById(R.id.btn_xiangce_jietu);
        mBtnTakePhotoJieTu = (Button) view.findViewById(R.id.btn_paizhao_jietu);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnTakePhoto.setOnClickListener(this);
        mBtnPhoto.setOnClickListener(this);
        mBtnTakePhotoJieTu.setOnClickListener(this);
        mBtnPhotoJieTu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo:
                MbsCameraApi.requestOpenAlbum(getActivity(), REQUEST_CODE_PHOTO);
                break;
            case R.id.btn_take_photo:
                if (UIUtils.hasAppPermission(Manifest.permission.CAMERA)) {
                    LogUtils.debug("has camera permission");
                    MbsCameraApi.requestCameraTakePhoto(getActivity(), REQUEST_CODE_TAKE_PHOTO);
                } else {
                    PermissionHelper.getInstance().requestPermission(getActivity(), new OnPermissionListener() {
                        @Override
                        public void onPermissionRequestSuccess(String... permission) {
                            MbsCameraApi.requestCameraTakePhoto(getActivity(), REQUEST_CODE_TAKE_PHOTO);
                        }

                        @Override
                        public void onPermissionRequestFailed(String... permission) {
                            PermissionDialogUtil.getInstance().showPermissionDeniedDialog(getActivity(), new OnPermisssionDialogClickListener() {
                                @Override
                                public void onCancelClick() {

                                }
                            }, permission);
                        }
                    }, Manifest.permission.CAMERA);
                }
                break;
            case R.id.btn_paizhao_jietu:
                CropHelper.getInstance().startCropFromCamera(getActivity(), new OnCropListener() {
                    @Override
                    public void onCropComplete(Bitmap bitmap) {
                        super.onCropComplete(bitmap);
                        mIvImage.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFailed(int requestCode, int resultCode, String suggestErrMsg) {
                        super.onFailed(requestCode, resultCode, suggestErrMsg);
                        ToastUtils.showToast(suggestErrMsg);
                    }

                });
                break;
            case R.id.btn_xiangce_jietu:
                CropHelper.getInstance().startCropFromGallery(getActivity(), new OnCropListener() {
                    @Override
                    public void onCropComplete(Bitmap bitmap) {
                        super.onCropComplete(bitmap);
                        mIvImage.setImageBitmap(bitmap);
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                Bitmap bitmap = MbsCameraApi.getBmpFromIntent(data);
                if (null != bitmap) {
                    mIvImage.setImageBitmap(bitmap);
                    LogUtils.debug("bitmap is not null");
                } else {
                    LogUtils.debug("bitmap is null");
                }
            }else if(requestCode == REQUEST_CODE_PHOTO){
                String pathByUri = MbsCameraApi.getRealPathByUri(data.getData());
                Bitmap bitmap = BitmapFactory.decodeFile(pathByUri);
                mIvImage.setImageBitmap(bitmap);
            }
        }
    }
}
