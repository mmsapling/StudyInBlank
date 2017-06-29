package framework.mvp.biz;

import framework.mvp.bean.User;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 9:29
 *  @描述：    TODO
 */
public interface OnLoginListener {
    void loginSuccess(User user);
    void loginFailed();

}
