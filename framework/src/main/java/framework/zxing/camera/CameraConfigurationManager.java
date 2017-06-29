/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package framework.zxing.camera;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import framework.zxing.ZXingConfigManager;
import framework.zxing.camera.open.CameraFacing;
import framework.zxing.camera.open.OpenCamera;
import framework.zxing.view.ViewfinderView;


/**
 * A class which deals with reading, parsing, and setting the camera parameters
 * which are used to configure the camera hardware.
 */
@SuppressLint("NewApi")
final class CameraConfigurationManager {

	private static final String TAG = "CameraConfiguration";

	private final Context context;
	private int cwNeededRotation;
	private int cwRotationFromDisplayToCamera;
	private Point screenResolution;
	private Point cameraResolution;
	private Point bestPreviewSize;
	private Point previewSizeOnScreen;

	CameraConfigurationManager(Context context) {
		this.context = context;
	}

	private ViewfinderView mViewfinderView = null;

	/**
	 * mViewfinderView.
	 * 
	 * @param mViewfinderView
	 *            the mViewfinderView to set
	 * @since JDK 1.6
	 */

	public void setViewfinderView(ViewfinderView mViewfinderView) {

		this.mViewfinderView = mViewfinderView;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(OpenCamera camera) {
		Camera.Parameters parameters = camera.getCamera().getParameters();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();

		int displayRotation = display.getRotation();
		int cwRotationFromNaturalToDisplay;
		switch (displayRotation) {
		case Surface.ROTATION_0:
			cwRotationFromNaturalToDisplay = 0;
			break;
		case Surface.ROTATION_90:
			cwRotationFromNaturalToDisplay = 90;
			break;
		case Surface.ROTATION_180:
			cwRotationFromNaturalToDisplay = 180;
			break;
		case Surface.ROTATION_270:
			cwRotationFromNaturalToDisplay = 270;
			break;
		default:
			// Have seen this return incorrect values like -90
			if (displayRotation % 90 == 0) {
				cwRotationFromNaturalToDisplay = (360 + displayRotation) % 360;
			} else {
				throw new IllegalArgumentException("Bad rotation: " + displayRotation);
			}
		}
		Log.i(TAG, "Display at: " + cwRotationFromNaturalToDisplay);

		int cwRotationFromNaturalToCamera = camera.getOrientation();
		Log.i(TAG, "Camera at: " + cwRotationFromNaturalToCamera);

		// Still not 100% sure about this. But acts like we need to flip this:
		if (camera.getFacing() == CameraFacing.FRONT) {
			cwRotationFromNaturalToCamera = (360 - cwRotationFromNaturalToCamera) % 360;
			Log.i(TAG, "Front camera overriden to: " + cwRotationFromNaturalToCamera);
		}

		cwRotationFromDisplayToCamera = (360 + cwRotationFromNaturalToCamera - cwRotationFromNaturalToDisplay) % 360;
		Log.i(TAG, "Final display orientation: " + cwRotationFromDisplayToCamera);
		if (camera.getFacing() == CameraFacing.FRONT) {
			Log.i(TAG, "Compensating rotation for front camera");
			cwNeededRotation = (360 - cwRotationFromDisplayToCamera) % 360;
		} else {
			cwNeededRotation = cwRotationFromDisplayToCamera;
		}
		Log.i(TAG, "Clockwise rotation from display to camera: " + cwNeededRotation);

		Point theScreenResolution = new Point();
		display.getSize(theScreenResolution);
		screenResolution = theScreenResolution;
		Log.i(TAG, "Screen resolution in current orientation: " + screenResolution);
		if (null != mViewfinderView) {
			View content = ((Activity) mViewfinderView.getContext()).findViewById(android.R.id.content);
			if (content.getHeight() == mViewfinderView.getHeight() && content.getWidth() == mViewfinderView.getWidth()) {
				Log.i("-------------------", "fullscreen no handle");
			} else {
				screenResolution = new Point(mViewfinderView.getWidth(), mViewfinderView.getHeight());
			}
		}
		cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
		Log.i(TAG, "Camera resolution: " + cameraResolution);
		bestPreviewSize = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
		Log.i(TAG, "Best available preview size: " + bestPreviewSize);

		/*
		 * RelativeLayout.LayoutParams params=(LayoutParams)
		 * mViewfinderView.getLayoutParams(); params.width=cameraResolution.y;
		 * params.height=cameraResolution.x;
		 */
//		screenResolution = new Point(cameraResolution.x, cameraResolution.y);
		boolean isScreenPortrait = screenResolution.x < screenResolution.y;
		boolean isPreviewSizePortrait = bestPreviewSize.x < bestPreviewSize.y;

		if (isScreenPortrait == isPreviewSizePortrait) {
			previewSizeOnScreen = bestPreviewSize;
		} else {
			previewSizeOnScreen = new Point(bestPreviewSize.y, bestPreviewSize.x);
		}
		Log.i(TAG, "Preview size on screen: " + previewSizeOnScreen);
	}


	void setDesiredCameraParameters(OpenCamera camera, boolean safeMode) {

		Camera theCamera = camera.getCamera();
		Camera.Parameters parameters = theCamera.getParameters();

		if (parameters == null) {
			Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
			return;
		}

		Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

		if (safeMode) {
			Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
		}

		parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);
		parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);

		if (ZXingConfigManager.getInstance().isPortrait()) {
			theCamera.setDisplayOrientation(90);// 旋转镜头90度，使相机预览方向正确显示 横屏可以去除
		}
		theCamera.setParameters(parameters);

		theCamera.setDisplayOrientation(cwRotationFromDisplayToCamera);

		Camera.Parameters afterParameters = theCamera.getParameters();
		Size afterSize = afterParameters.getPreviewSize();
		if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
			Log.w(TAG, "Camera said it supported preview size " + bestPreviewSize.x + 'x' + bestPreviewSize.y
					+ ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
			bestPreviewSize.x = afterSize.width;
			bestPreviewSize.y = afterSize.height;
		}
	}

	Point getBestPreviewSize() {
		return bestPreviewSize;
	}

	Point getPreviewSizeOnScreen() {
		return previewSizeOnScreen;
	}

	Point getCameraResolution() {
		return cameraResolution;
	}

	Point getScreenResolution() {
		return screenResolution;
	}

	int getCWNeededRotation() {
		return cwNeededRotation;
	}
}
