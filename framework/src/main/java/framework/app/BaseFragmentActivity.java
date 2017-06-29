package framework.app;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.framework.R;

import framework.utils.LogManager;
import framework.utils.LogUtils;

/*
  *  @创建者:   xuanwen
  *  @创建时间:  2017/3/23 11:45
  *  @描述：    带标题栏的activity，仅用作BaseFragment的容器
  */
public class BaseFragmentActivity extends BaseActivity {

    private BaseFragment mFragment;

    public BaseFragment getFragment() {
        return mFragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        long fragmentId = getIntent().getLongExtra(ActivityManager.FRAGMENT_KEY, 0);
        BaseFragment baseFragment = ActivityManager.getInstance().getFragment(fragmentId);
        mFragment = baseFragment;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment_activity);
        if (baseFragment != null) {
            setPageTag(baseFragment.getPageTag());
            useDefaultTitle(baseFragment.getTitle(), baseFragment.isShowBackBtn(), baseFragment.isShowRightBtn());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, baseFragment);
            transaction.commit();
            ActivityManager.getInstance().removeFragment(fragmentId);
        }

    }

    @Override
    public void handleBack() {
        if (null != mFragment && mFragment.onBackPressFragment()) {
            if (mFragment.mFragmentBack != null) {
                mFragment.mFragmentBack.onBackClick();
            }
        } else {
            super.handleBack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode,resultCode,data);
        LogManager.logD("requestCode = " + requestCode);
    }

    @Override
    public void onResult(Object bundle) {
        mFragment.onResult(bundle);
    }
}