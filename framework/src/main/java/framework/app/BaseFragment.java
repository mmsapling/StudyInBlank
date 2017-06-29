package framework.app;


import android.app.Fragment;
import android.os.Bundle;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/23 11:41
 *  @描述：    基础fragment
 */
public class BaseFragment extends Fragment {
    private String mTitle;
    /**
     * 是否显示标题栏左边的返回按钮
     */
    private boolean mIsShowBackButton;
    /**
     * 是否显示右边按钮
     */
    private boolean mIsShowRightButton;
    /**
     * 页面的标记，记住当前页面的位置，用户返回
     */
    private Object mPageTag;

    public String getTitle() {
        return mTitle;
    }



    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Object getPageTag() {
        return mPageTag;
    }

    public void setPageTag(Object pageTag) {
        this.mPageTag = pageTag;
    }

    /**
     *
     * @return
     *      true: 返回按钮事件处理完成
     *      false:返回按钮事件交给activity
     */
    public boolean onBackPressFragment(){
        return false;
    }

    /**
     * 初始化标题栏，应当实例化BaseFragment的时候调用此方法
     * @param title
     *          标题
     * @param isShowBackButton
     *      是否显示标题栏左边按钮
     * @param isShowRightButton
     *      是否显示标题栏右边按钮
     */
    protected  void initTitleBar(String title,boolean isShowBackButton,boolean isShowRightButton){
        this.mTitle = title;
        this.mIsShowBackButton = isShowBackButton;
        this.mIsShowRightButton = isShowRightButton;

    }
    public boolean isShowBackBtn(){
        return mIsShowBackButton;
    }
    public boolean isShowRightBtn(){
        return mIsShowRightButton;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseActivity activity = (BaseActivity) getActivity();
        if(activity != null){
            activity.useDefaultTitle(getTitle(),isShowBackBtn(),isShowRightBtn());
        }
    }

    /**
     * 用户activityForResult
     * @param bundle
     */
    public void onResult(Object bundle){

    }

    /**
     * 自定义返回点击事件
     */
    public FragmentBack mFragmentBack = null;
    public void setBackClick(FragmentBack fragmentBack){
        this.mFragmentBack = fragmentBack;
    }
    public interface  FragmentBack{
        void onBackClick();
    }
}
