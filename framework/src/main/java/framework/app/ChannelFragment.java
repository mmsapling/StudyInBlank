package framework.app;

/**
 * Created by cxw on 2017/6/27.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/6/27
 * @描述: 频道页专用基类
 */

public abstract class ChannelFragment extends BaseFragment{
    public boolean isVisible = false;
    public int initStatus = -1; //-1：没有初始化 0：正在初始化 1：初始化完成
    /**
     * 在这里实现Fragment数据的缓加载
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }
    }

    private void onVisible() {

    }
}
