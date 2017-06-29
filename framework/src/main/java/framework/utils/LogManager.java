package framework.utils;

/**
 * 自定义日志 有开关， 并且可以输入到sd卡中
 */
public class LogManager {


    public static final boolean IS_INSURANCE_TEST = false;// 是否为代理保险测试版
    public static final boolean IS_RELEASE_VERSION = false;// 是否生产版本开关,true-生产版，false-内测版

    public static final boolean IS_DEBUG = true;// 日志开关,true为打开

    public static final boolean IsClientTypeTest = true;// 测试版开关，true为测试版客户端



    public static final boolean CLOSE_VERIFY_CERTIFICATE = true;// 证书认证开关,true为不验证证书

    public static final boolean IS_CONN = true;// 通讯连接方式开关，默认为true;
    // true代表调用HttpClient,
    // false代表调用HttpsURLConnection


    public static void logV(String msg) {
        if (!IS_DEBUG)
            return;

        if (msg.length() < LOG_LENGTH) {
            android.util.Log.v(TAG, msg);
        } else {
            // msg过长
            String str1 = msg.substring(0, LOG_LENGTH);
            android.util.Log.v(TAG, str1);
            logV(msg.substring(LOG_LENGTH));
        }
    }

    public static void logD(String msg) {
        if (!IS_DEBUG)
            return;

        if (msg.length() < LOG_LENGTH) {
            android.util.Log.d(TAG, msg);
        } else {
            // msg过长
            String str1 = msg.substring(0, LOG_LENGTH);
            android.util.Log.d(TAG, str1);
            logD(msg.substring(LOG_LENGTH));
        }
    }

    public static void logI(String msg) {
        if (!IS_DEBUG)
            return;

        if (msg.length() < LOG_LENGTH) {
            android.util.Log.i(TAG, msg);
        } else {
            // msg过长
            String str1 = msg.substring(0, LOG_LENGTH);
            android.util.Log.i(TAG, str1);
            logI(msg.substring(LOG_LENGTH));
        }
    }

    public static void logW(String msg) {
        if (!IS_DEBUG)
            return;

        if (msg.length() < LOG_LENGTH) {
            android.util.Log.w(TAG, msg);
        } else {
            // msg过长
            String str1 = msg.substring(0, LOG_LENGTH);
            android.util.Log.w(TAG, str1);
            logW(msg.substring(LOG_LENGTH));
        }
    }

    public static void logE(String msg) {
        if (!IS_DEBUG)
            return;

        if (msg.length() < LOG_LENGTH) {
            String errorPoint = "error at " + Thread.currentThread().getStackTrace()[2].getMethodName() + " called by "
                    + Thread.currentThread().getStackTrace()[3].getClassName() + "::"
                    + Thread.currentThread().getStackTrace()[3].getMethodName();

            android.util.Log.e(TAG, msg);
            android.util.Log.e(TAG, errorPoint);
        } else {
            // msg过长
            String str1 = msg.substring(0, LOG_LENGTH);
            android.util.Log.e(TAG, str1);
            logE(msg.substring(LOG_LENGTH));
        }
    }

    /**
     * 打印流程,默认i levle
     *
     * @param msg 可以为null
     */
    public static void printProcess(String msg) {
        if (!IS_DEBUG)
            return;

        String info = Thread.currentThread().getStackTrace()[3].getClassName() + //
                "::" + Thread.currentThread().getStackTrace()[3].getMethodName();
        if (msg != null)
            info += "  " + msg;

        logI(info);
    }

    /**
     * 在方法内部调用，打印调用信息
     */
    public static void whoInvokeMe() {
        if (!IS_DEBUG)
            return;

        String callerInfo = Thread.currentThread().getStackTrace()[3].getMethodName() + //
                " called by " + Thread.currentThread().getStackTrace()[4].getClassName() + //
                "::" + Thread.currentThread().getStackTrace()[4].getMethodName();

        logI(callerInfo);
    }

    public static final String TAG = "mmsapling";

    private static final int LOG_LENGTH = 3000; // android对多余某个长度的string,logcat会截断
}

