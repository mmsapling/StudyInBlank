package framework.mvp.view;

import framework.mvp.bean.User;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 9:27
 *  @描述：    TODO
 */
public interface IUserLoginView {
    String getUserName();

    String getPassword();

    void showLoading();

    void hideLoading();

    void toMainActivity(User user);

    void showFailedError();

    void clearUserName();

    void clearPassword();
}
