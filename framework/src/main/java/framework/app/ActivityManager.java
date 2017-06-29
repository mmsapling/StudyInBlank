package framework.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import framework.utils.LogManager;
import framework.utils.LogUtils;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/23 10:41
 *  @描述：    活动管理器
 */
public class ActivityManager {
    private static final String TAG = ActivityManager.class.getSimpleName();
    public static final String FRAGMENT_KEY = "fragment";
    /**
     * 用户缓存当前已被打开的Activity
     */
    private List<WeakReference<BaseActivity>> mActivityCache = new ArrayList<>();
    private List<BaseActivity> mActivitys = new ArrayList<>();
    /**
     * 临时缓存activities
     */
    private List<BaseActivity> mTempActivities = new ArrayList<BaseActivity>();
    private static ActivityManager mInstance = null;
    private Map<Long, BaseFragment> mFragments = new ConcurrentHashMap<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (mInstance == null) {
            synchronized (ActivityManager.class) {
                if (mInstance == null) {
                    mInstance = new ActivityManager();
                }
            }
        }
        return mInstance;
    }

    public void addActivity(BaseActivity activity) {
        if (activity == null) {
            LogUtils.warn(String.format("Failed to addActivity(%s)", activity));
            return;
        }
        mActivitys.add(activity);
    }

    /**
     * 获取当前页面的上一个页面
     *
     * @param activity 当前页面
     * @return
     */
    public BaseActivity getPreActivity(BaseActivity activity) {
        int index = mActivitys.indexOf(activity);
        if (-1 == index || index < 1) {
            return null;
        }
        return mActivitys.get(index - 1);
    }

    public List<BaseActivity> getAllActivities() {
        return mActivitys;
    }

    public void removeActivity(BaseActivity activity) {
        if (mActivitys == null || activity == null) {
            LogUtils.warn(String.format("Failed to removeActivity(%s)", activity));
            return;
        }
        mActivitys.remove(activity);
    }

    public BaseActivity getTopAcitivity() {
        if (null == mActivitys || 0 == mActivitys.size()) {
            return null;
        }
        BaseActivity topActivity = mActivitys.get(mActivitys.size() - 1);
        if (topActivity.isFinishing()) {
            removeActivity(topActivity);
            return getTopAcitivity();
        }
        return topActivity;
    }

    public BaseFragment getFragment(long id) {
        return mFragments.get(id);
    }

    public void removeFragment(long id) {
        mFragments.remove(id);
    }

    public void startActivity(Context context, Class clazz) {
        //如果是home页几个类，特殊处理
        String className = clazz.getSimpleName();
        LogManager.logD("className = " + className);
        try {

            //转化成对应的类，根据父类跳转
            if (BaseActivity.class.isAssignableFrom(clazz)) {
                LogManager.logD("基类是BaseActivity");
                Intent intent = new Intent(context, clazz);
                context.startActivity(intent);
            } else if (BaseFragment.class.isAssignableFrom(clazz)) {
                LogManager.logD("基类是BaseFragment");
                startFragment(context, (BaseFragment) clazz.newInstance());
            } else {
                LogManager.logD("当前类未继承BaseFragment或BaseActivity");
            }
        } catch (Exception e) {
            LogUtils.error("startBaseActivity:" + e.toString());
        }
    }

    public void startFragment(Context context, BaseFragment fragment) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        if (fragment != null) {
            long id = System.currentTimeMillis();
            mFragments.put(id, fragment);
            intent.putExtra(FRAGMENT_KEY, id);
        }
        context.startActivity(intent);
    }

    public void putFragment(long id, BaseFragment fragment) {
        mFragments.put(id, fragment);
    }

    /**
     * 返回到标记页面之前的页面，标记页面也关闭
     *
     * @param tag    页面标记
     * @param bundle
     */
    public void backToTagFront(Object tag, Object bundle) {
        if (mActivitys == null) {
            LogUtils.warn("Failed to backToTagFront()");
            return;
        }
        int step = 0;
        synchronized (mActivitys) {
            for (BaseActivity activity : mActivitys) {
                step++;
                if (tag != null && tag.equals(activity.getPageTag())) {
                    break;
                }
            }
        }
        if (step > 0) {
            step -= 1;
        }
        backTo(step, bundle);
    }

    /**
     * 返回到标记页面之前的页面，标记页面也关闭
     *
     * @param tag 页面标记
     */
    public void backToTagFront(Object tag) {
        if (mActivitys == null) {
            LogUtils.warn("Failed to backToTagFront()");
            return;
        }
        int step = 0;
        synchronized (mActivitys) {
            for (BaseActivity activity : mActivitys) {
                step++;
                if (tag != null && tag.equals(activity.getPageTag())) {
                    break;
                }
            }
        }
        if (step > 0) {
            step -= 1;
        }
        backTo(step);
    }

    private void backTo(int step, Object bundle) {
        backTo(step);
        mActivitys.get(mActivitys.size() - 1).onResult(bundle);
    }

    /**
     * 判断是否有页面标志
     * @param tag
     * @return
     */
    public boolean hasTag(Object tag){
        if(mActivitys == null){
            return false;
        }
        synchronized (mActivitys){
            for(BaseActivity activity :mActivitys){
                if(tag != null && tag.equals(activity.getPageTag())){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 返回到应用程序首页
     */
    public void backToHome() {
        backTo(1);
    }

    public void backToHome(Bundle bundle) {
        backTo(1);
        mActivitys.get(mActivitys.size() - 1).onResult(bundle);
    }

    /**
     * 返回到标记页面，标记页面不关闭
     *
     * @param tag 页面标记
     */
    public void backToTag(Object tag) {
        if (mActivitys == null) {
            LogUtils.warn(String.format("Failed to backToTag()"));
            return;
        }
        int step = 0;
        synchronized (mActivitys) {
            for (BaseActivity activity : mActivitys) {
                step++;
                if (tag != null && tag.equals(activity.getPageTag())) {
                    break;
                }
            }
        }
        backTo(step);
    }

    /**
     * 返回指定的步骤
     *
     * @param step 指定的步骤
     */
    private void backTo(int step) {
        if (mActivitys == null) {
            LogUtils.warn(String.format("Failed to backTo(%s)", step));
            return;
        }
        synchronized (mActivitys) {
            if (step < 0 || step > mActivitys.size()) {
                return;
            }
            List<BaseActivity> sub = new ArrayList<>(mActivitys.subList(step, mActivitys.size()));
            while (!sub.isEmpty()) {
                BaseActivity tempActivity = sub.remove(0);
                mActivitys.remove(tempActivity);
                if (tempActivity != null && !tempActivity.isFinishing()) {
                    if (sub.size() != 0) {
                        tempActivity.setFinishAnimation(0);
                    }
                    tempActivity.finish();
                }
            }
        }
    }


}

