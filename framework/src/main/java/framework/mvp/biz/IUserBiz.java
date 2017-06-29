package framework.mvp.biz;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/22 9:29
 *  @描述：    TODO
 */
public interface IUserBiz {
    void login(String username,String password,OnLoginListener loginListener);
}
