package framework.mvp.presenter;

import android.os.Handler;

import framework.mvp.bean.User;
import framework.mvp.biz.IUserBiz;
import framework.mvp.biz.OnLoginListener;
import framework.mvp.biz.UserBiz;
import framework.mvp.view.IUserLoginView;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 9:28
 *  @描述：    TODO
 */
public class UserLoginPresenter {
    private IUserBiz mUserBiz;
    private IUserLoginView mUserLoginView;
    private Handler mHandler = new Handler();

    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.mUserLoginView = userLoginView;
        this.mUserBiz = new UserBiz();
    }
    public void clear(){
        mUserLoginView.clearPassword();
        mUserLoginView.clearUserName();
    }
    public void login() {
        mUserLoginView.showLoading();
        mUserBiz.login(mUserLoginView.getUserName(), mUserLoginView.getPassword(), new OnLoginListener() {
            @Override
            public void loginSuccess(final User user) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mUserLoginView.toMainActivity(user);
                        mUserLoginView.hideLoading();
                    }
                });
            }

            @Override
            public void loginFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mUserLoginView.showFailedError();
                        mUserLoginView.hideLoading();
                    }
                });
            }
        });
    }
}
