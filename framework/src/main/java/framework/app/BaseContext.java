package framework.app;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/23 17:20
 *  @描述：    程序运行上下文
 */
public class BaseContext {
    /**
     * 系统上下文
     */
    private Context mApplicationContext;
    /**
     * 当前的Activity
     */
    private WeakReference<Activity> mCurrentActivityRef;
    /**
     * 屏幕每英寸点数
     */
    private int mDensityDpi;
    /**
     * 设备信息
     */
    private String mDevice;
    /**
     * 平台信息
     */
    private String mPlatForm;
    /**
     * 本地登录状态，登陆成功为true，退出登录置为false
     */
    private boolean mIsLoginState = false;

    public boolean isLoginState() {
        return mIsLoginState;
    }

    public void setLoginState(boolean loginState) {
        this.mIsLoginState = loginState;
    }

    public Activity getCurrentActivity() {
        if (mCurrentActivityRef != null) {
            return mCurrentActivityRef.get();
        }
        return null;
    }

    public Context getApplicationContext() {
        return mApplicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.mApplicationContext = applicationContext;
    }

    public WeakReference<Activity> getCurrentActivityRef() {
        return mCurrentActivityRef;
    }

    public void setCurrentActivityRef(WeakReference<Activity> currentActivityRef) {
        this.mCurrentActivityRef = currentActivityRef;
    }

    public int getDensityDpi() {
        return mDensityDpi;
    }

    public void setDensityDpi(int densityDpi) {
        this.mDensityDpi = densityDpi;
    }

    public String getDevice() {
        return mDevice;
    }

    public void setDevice(String device) {
        this.mDevice = device;
    }

    public String getPlatForm() {
        return mPlatForm;
    }

    public void setPlatForm(String platForm) {
        this.mPlatForm = platForm;
    }
}
