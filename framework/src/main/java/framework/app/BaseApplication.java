package framework.app;

import android.app.Application;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/23 10:34
 *  @描述：    TODO
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        /**
         * 异常日志捕捉
         */
        CrashHandler.getInstance().initCrashHandler(getApplicationContext());
    }
    private static BaseApplication mInstance;
    public static BaseApplication getInstance(){
        if(mInstance == null){
            throw new RuntimeException("IlleagelStateExp:instance is null,application error");
        }
        return mInstance;
    }

}
