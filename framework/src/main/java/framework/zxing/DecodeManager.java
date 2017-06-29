/**
 * Project Name:ZXingDemo
 * File Name:DecodeManager.java
 * Package Name:com.google.zxing
 * Date:2015-12-15下午3:33:44
 *
 */

package framework.zxing;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.framework.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import framework.app.ActivityManager;
import framework.ui.CcbDialog;
import framework.utils.LogManager;
import framework.zxing.camera.CameraManager;
import framework.zxing.view.ViewfinderResultPointCallback;
import framework.zxing.view.ViewfinderView;

/**
 * ClassName: DecodeManager <br/>
 * Description: 解码处理类 . <br/>
 * date: 2015-12-15 下午3:33:44 <br/>
 *
 * @author luohao
 * @version
 * @since JDK 1.6
 */

public class DecodeManager implements SurfaceHolder.Callback {

	private static DecodeManager instance = null;
	private Activity currAct;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private boolean hasSurface;
	private ActivityHandler handler;
	private Collection<BarcodeFormat> decodeFormats;
	private String characterSet;// 编码类型
	private Map<DecodeHintType, ?> decodeHints;
	private ViewfinderView viewfinderView;
	private CameraManager cameraManager;

	private IDecodeResultListener resultListener = null;

	private DecodeManager() {
		super();
	}

	/**
	 * instance.
	 *
	 * @return the instance
	 * @since JDK 1.6
	 */

	public static DecodeManager getInstance() {

		if (null == instance)
			instance = new DecodeManager();
		return instance;
	}

	public void attachToActivity(Activity act, IDecodeResultListener resultListener) {
		this.currAct = act;
		this.resultListener = resultListener;
		onCreate();
	}

	private void onCreate() {
		if (currAct.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			currAct.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			ZXingConfigManager.getInstance().setOrientation(ZXingConfigManager.Orientation.PORTRAIT);
		} else {
			currAct.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			ZXingConfigManager.getInstance().setOrientation(ZXingConfigManager.Orientation.LANDSCAPE);
		}
		initOnCreate();

	}

	private void initOnCreate() {
		hasSurface = false;
		inactivityTimer = new InactivityTimer(currAct);
		beepManager = new BeepManager(currAct);
	}

	public void onResume() {

		initOnResume();
	}

	private void initOnResume() {

		handler = null;
		decodeFormats = null;
		characterSet = null;

		cameraManager = new CameraManager(currAct);
		viewfinderView = (ViewfinderView) currAct.findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		if (null != viewfinderView) {
			cameraManager.setViewfinderView(viewfinderView);
		}
		beepManager.updatePrefs();

		inactivityTimer.onResume();

		SurfaceView surfaceView = (SurfaceView) currAct.findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (RuntimeException e){
			resultListener.handleError(e.getMessage());
		}
		// Creating the handler starts the preview, which can also throw a
		// RuntimeException.
		if (handler == null) {
			handler = new ActivityHandler(currAct, decodeFormats, decodeHints, characterSet, cameraManager, mScanListener);
		}
	}

	/**
	 * 生成二维码图片
	 *
	 * @param source
	 *            需要生成的资源
	 * @param bitmapWidth
	 *            图片宽
	 * @param bitmapHeight
	 *            图片高
	 * @return 二维码图片
	 */
	public Bitmap createBitmapFromSource(String source, int bitmapWidth, int bitmapHeight) {

		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(source, BarcodeFormat.QR_CODE, bitmapWidth, bitmapHeight);
		} catch (WriterException e) {
			LogManager.logE(e.toString());
		}
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				} else {
					pixels[y * width + x] = 0xffffffff;
				}

			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;

	}

	/**
	 * 通过本地图片读取二维码信息
	 *
	 * @param imgPath
	 *            图片路径
	 * @param listener
	 *            监听器
	 */
	public void deCodeFromImage(String imgPath, IDecodeResultListener listener) {
		if (TextUtils.isEmpty(imgPath))
			return;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap scanBitmap = BitmapFactory.decodeFile(imgPath, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0) {
			sampleSize = 1;
		}
		options.inSampleSize = sampleSize;
		options.inPurgeable = true;
		// inInputShareable：设置是否深拷贝，与inPurgeable结合使用，inPurgeable为false时，该参数无意义
		options.inInputShareable = true;

		if (null != scanBitmap && !scanBitmap.isRecycled()) {
			scanBitmap.recycle();
		}
		scanBitmap = BitmapFactory.decodeFile(imgPath, options);
		deCodeFromImage(scanBitmap, listener);
	}

	/**
	 * 通过本地图片读取二维码信息
	 *
	 * @param scanBitmap
	 *            图片
	 * @param listener
	 *            监听器
	 */
	public void deCodeFromImage(Bitmap scanBitmap, IDecodeResultListener listener) {
		if (null == scanBitmap || null == listener)
			return;
		int width = scanBitmap.getWidth();
		int height = scanBitmap.getHeight();
		int[] pixels = new int[width * height];
		scanBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
		BinaryBitmap byt = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
            Handler handlerDelayed = new Handler();
            handlerDelayed.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onPause();
                }
            }, (long) 500);
            listener.handleDecode(reader.decode(byt), scanBitmap);
		} catch (Exception e) {
			CcbDialog.getInstance().showDialog(currAct, "无法解析二维码", new CcbDialog.OnClickCancelListener() {
				@Override
				public void clickCancel(Dialog dialog) {
					dialog.dismiss();
					onResume();
				}
			}, new CcbDialog.OnClickConfirmListener() {
				@Override
				public void clickConfirm(Dialog dialog) {
					dialog.dismiss();
					onResume();
				}
			});

		}
	}

	private ActivityHandler.IScanListener mScanListener = new ActivityHandler.IScanListener() {

		@Override
		public void handleDecode(Result<?> obj, Bitmap barcode) {

			// TODO Auto-generated method stub
			if (ZXingConfigManager.getInstance().isPlayBeeSound()) {
				beepManager.playBeepSoundAndVibrate();
			}
			if (null == resultListener)
				return;
			resultListener.handleDecode(obj, barcode);
		}

		@Override
		public ViewfinderResultPointCallback getViewfinderResultPointCallback() {

			return new ViewfinderResultPointCallback(viewfinderView);

		}

		@Override
		public void drawViewfinder() {

			viewfinderView.drawViewfinder();
		}
	};

	public void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		// historyManager = null; // Keep for onActivityResult
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) currAct.findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
	}

	public void onDestroy() {
		inactivityTimer.shutdown();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		// TODO Auto-generated method stub
		if (!hasSurface) {
			hasSurface = true;
			if (currAct != ActivityManager.getInstance().getTopAcitivity())
				return;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		// TODO Auto-generated method stub
		hasSurface = false;
	}

	public void openFlashLight() {
		cameraManager.openFlashLight();
	}

	public void closeFlashLight() {
		cameraManager.closeFlashLight();
	}

	public boolean isFlashLightOpen() {
		return cameraManager.isFlashLightOpen();
	}

	public static abstract class IDecodeResultListener {

		public abstract void handleDecode(Result obj, Bitmap barcode);

		public  void handleError(String error){};
	}
}
