package framework.mvp.biz;

import android.os.SystemClock;

import framework.mvp.bean.User;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 9:29
 *  @描述：    TODO
 */
public class UserBiz implements IUserBiz {
    @Override
    public void login(final String username, final String password, final OnLoginListener loginListener) {
        //模拟子线程耗时操作
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                //模拟登陆成功
                if("cxw".equals(username) && "123".equals(password)){
                    User user = new User();
                    user.setUsername("cxw");
                    user.setPassword("123");
                    loginListener.loginSuccess(user);
                }else{
                    loginListener.loginFailed();
                }
            }
        }.start();
    }
}
