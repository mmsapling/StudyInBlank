package framework.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.framework.R;

import java.util.HashMap;
import java.util.List;

import framework.utils.LogManager;
import framework.utils.SystemBarTintManager;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/23 9:50
 *  @描述：    TODO
 */
public class BaseActivity extends Activity {
    private final String TAG = BaseActivity.class.getSimpleName();
    /**
     * 菜单根布局
     */
    private RelativeLayout mRLRootContainer;
    private Button mBtnLeft;
    /**
     * 右边按钮
     */
    private Button mBtnRight;
    /**
     * 中间标题
     */
    private TextView mTvTitle;
    /**
     * 是否允许app从后台恢复后，对设置了图案锁屏的用户，显示图案锁屏
     * 默认是true
     */
  private boolean mIsAllowShowPatternLock = true;
    protected void setStartAnimation(int startAnimation) {
        mStartAnimation = startAnimation;
    }

    protected void setFinishAnimation(int finishAnimation) {
        this.mFinishAnimation = finishAnimation;
    }

    /**
     * 页面进入动画
     */
    protected int mStartAnimation = R.anim.right_to_left_enter;

    /**
     * 页面退出时动画
     */
    protected int mFinishAnimation = R.anim.left_to_right_enter;

    public int getStartAnimation() {
        return mStartAnimation;
    }


    public int getFinishAnimation() {
        return mFinishAnimation;
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        //super.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 当前页面能否关闭
     */
    public boolean mCanFinish = true;
    /**
     * app是否进入后台
     * 在onPause设置(可能)，在onResume检查
     */
    protected boolean mIsAppGoingToBackground = false;
    /**
     * app进入后台时间，毫秒
     */
    protected long mAppGoingToBackgroundMills;
    protected BaseActivity mContext;
    /**
     * 页面的标记，记住当前页面的位置，用户返回
     */
    private Object mPageTag;
    /**
     * 当前页功能id
     */
    private HashMap<String, Object> mTitleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogManager.logD(getClass().getSimpleName() + "onCreate()...");
        mContext = BaseActivity.this;
        setWindowAlpha(0.0f);
        ActivityManager.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void setWindowAlpha(float alpha) {
        getWindow().getDecorView().setAlpha(alpha);
    }

    protected float getWindowAlpha() {
        return getWindow().getDecorView().getAlpha();
    }

    public boolean isCanFinish() {
        return mCanFinish;
    }

    protected void resetTitleText(String title) {
        ((TextView) findViewById(R.id.tv_menu_title)).setText(title);
    }

    /**
     * 当前页面是否显示
     *
     * @return 界面显示为true
     */
    public boolean isShow() {
        //1.0 完全不透明， 0.0完全透明
        return 1.0f == getWindowAlpha();
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param activity
     * @param color
     */
    public void setSystemBar(Activity activity, int color) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintColor(color);
    }

    @TargetApi(19)
    private void setTranslucentStatus(Boolean b) {

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        final int attributes = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (b) {
            layoutParams.flags |= attributes;
        }
        window.setAttributes(layoutParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogManager.logD(this.getClass().getSimpleName() + " onResume()...");
        setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if(mIsAppGoingToBackground){
            //从后台恢复
            mIsAppGoingToBackground = false;
            LogManager.logD(this.getClass().getSimpleName() + " onAppCameBackToForeground().");
            onAppComeBackToForeground();
        }
    }

    public Button getBtnLeft() {
        return mBtnLeft;
    }

    public Button getBtnRight() {
        return mBtnRight;
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }
    public void setTvTitle(String title){
        mTvTitle.setText(title);
    }
    public void setPageTag(Object pageTag) {
        this.mPageTag = pageTag;
    }

    public Object getPageTag() {
        return this.mPageTag;
    }

    /**
     * 设置标题栏为透明背景色
     */
    public void setTitleBackgroundTransparent() {
        mRLRootContainer.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 设定标题栏左侧按钮为返回键
     */
    public void setTitleLeftBack() {
        mBtnLeft.setBackgroundResource(R.mipmap.back);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBack();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogManager.logD("onStart()...");
        if (isShow()) {
            return;
        }
        onStartLoading();

    }

    protected void onStartLoading() {
        onLoadingFinish(true);
    }

    /**
     * 预加载结束
     *
     * @param showActivity 是否显示Activity
     */
    private void onLoadingFinish(boolean showActivity) {
        LogManager.logD("onLoadingFinish");
        if (showActivity) {
            //防止多次调用onLoadingFinish产生进场动画
            if (isShow()) {
                return;
            }
            setWindowAlpha(1.0f);
            if (0 == getStartAnimation()) {
                return;
            }
            //用下列方法动画混乱了，不知道为什么
            //Animation startAnimation = AnimationUtils.loadAnimation(this, getStartAnimation());
            //getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT).startAnimation(startAnimation);
            super.overridePendingTransition(getStartAnimation(),0);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        LogManager.logD(this.getClass().getSimpleName() + " onDestroy()...");
        ActivityManager.getInstance().removeActivity(this);
        //隐藏软键盘
        super.onDestroy();
    }
    private void checkIfAppGoingBackground(){
        BaseActivity topAcitivity = ActivityManager.getInstance().getTopAcitivity();
        if(null != topAcitivity){
            if(this == topAcitivity && !this.isFinishing()){
                //栈顶是当前activity
                //不是由于finish产生的onPause
                mIsAppGoingToBackground = true;
                mAppGoingToBackgroundMills = System.currentTimeMillis();
                LogManager.logD(this.getClass().getSimpleName() + " onAppGoingToBackground()...");
                onAppGoingToBackground();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogManager.logD(this.getClass().getSimpleName() + " onStop()...");
        checkIfAppGoingBackground();
    }

    /**
     * app进入后台
     */
    protected  void onAppGoingToBackground(){
        //在桌面logo图标上显示未读信息的数量
        try{

        }catch (Exception e){
            LogManager.logE(e.toString());
        }
    }
    public void setAllowShowPatternLock(boolean allowShowPatternLock){
        mIsAllowShowPatternLock = allowShowPatternLock;
    }
    public boolean isAllowShowPatternLock(){
        return mIsAllowShowPatternLock;
    }
    /**
     * app从后台恢复
     */
    protected void onAppComeBackToForeground(){
        //验证手势密码，如果需要的话
        if(isAllowShowPatternLock()){

        }
    }
    @Override
    public void finish() {
        super.finish();
        if (0 == getFinishAnimation()) {
            return;
        }
        if (!isShow()) {
            return;
        }
        super.overridePendingTransition(0, getFinishAnimation());
    }

    public void handleBack() {
        //如果有drawerlayout之类的就先关掉它
        LogManager.logD("BaseActivity back click");
        finish();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    /**
     * 引用标题栏的控件
     */
    private void findViewById() {
        mRLRootContainer = (RelativeLayout) findViewById(R.id.rl_menu_container);
        mBtnLeft = (Button) findViewById(R.id.btn_menu_left);
        mBtnRight = (Button) findViewById(R.id.btn_menu_right);
        mTvTitle = (TextView) findViewById(R.id.tv_menu_title);

    }

    protected void finish(Object bundle) {
        List<BaseActivity> allActivities = ActivityManager.getInstance().getAllActivities();
        //当前记录Activity列表小于两个 不处理
        if (allActivities.size() < 2) {
            finish();
            return;
        }
        //为空不处理
        BaseActivity preActivity = allActivities.get(allActivities.size() - 2);
        if (null == preActivity) {
            finish();
            return;
        }
        //通知数据
        finish();
        preActivity.onResult(bundle);
    }

    public void onResult(Object bundle) {

    }

    public void setBtnLeftClickListener(View.OnClickListener listener) {
        mBtnLeft.setOnClickListener(listener);
    }

    public void setBtnRightClickListener(View.OnClickListener listener) {
        mBtnRight.setOnClickListener(listener);
    }

    public void setBtnLeftBackground(@DrawableRes int resId) {
        mBtnLeft.setBackgroundResource(resId);
    }

    public void setBtnRightBackground(@DrawableRes int resId) {
        mBtnRight.setBackgroundResource(resId);
    }

    /**
     * 使用默认的标题栏
     *
     * @param title          中间标题
     * @param isShowBtnLeft  是否展示左边按钮
     * @param isShowBtnRight 是否展示右边按钮
     */
    protected void useDefaultTitle(String title, boolean isShowBtnLeft, boolean isShowBtnRight) {
        findViewById();
        //文字标题
        mTvTitle.setText(title);

        setTitleLeftBack();

        mBtnLeft.setVisibility(isShowBtnLeft ? View.VISIBLE : View.GONE);
        mBtnRight.setVisibility(isShowBtnRight ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
            android.app.ActivityManager.TaskDescription taskDescription = new android.app.ActivityManager.TaskDescription(null, null, color);
            setTaskDescription(taskDescription);
        }
    }

    /**
     * 用于功能模块内跳转
     *
     * @param clazz
     */
    public void startBaseActivity(Class clazz) {
        ActivityManager.getInstance().startActivity(this, clazz);
    }


}
