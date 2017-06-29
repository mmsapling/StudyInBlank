package framework.annotation;

import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/21 15:06
 *  @描述：    TODO
 */
public class Father {

    private String name;
    @CheckResult
    public String getName() {
        return name;
    }
    @CallSuper
    public void setName(String name) {
        this.name = name;
    }
    public void show(){
        String name = getName();
    }
}
