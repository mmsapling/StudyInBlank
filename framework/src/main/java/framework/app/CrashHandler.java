package framework.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import framework.utils.LogUtils;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/1 9:46
 *  @描述：    异常日志捕捉
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler mCrashHandlerInstance;
    private final boolean DEBUG               = true;
    private final String  CRASH_LOG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Tylz" + File.separator + "CrashLog" + File.separator;
    private Context mContext;
    private final String                          CRASH_LOG_FILE_NAME = "crash.txt";
    protected     Thread.UncaughtExceptionHandler mExceptionHandler   = null;
    private       String                          mVersionName        = "";
    private       String                          mVersionCode        = "";

    private CrashHandler() {}

    @Override
    public void uncaughtException(Thread t, Throwable e) {
       LogUtils.error(e.toString());
        writeLogToSdCard(e);
        if(null != mExceptionHandler){
            mExceptionHandler.uncaughtException(t,e);
        }
    }

    private void writeLogToSdCard(Throwable e) {
        if (!isSdCardMounted() || !DEBUG) {
            return;
        }
        File logDir = new File(CRASH_LOG_FILE_PATH);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        PrintWriter    printWriter = null;
        BufferedWriter bw          = null;
        long           currSysTime = System.currentTimeMillis();
        String formatTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(currSysTime));
        File crashLog = new File(CRASH_LOG_FILE_PATH + formatTime + "_" + CRASH_LOG_FILE_NAME);
        if (crashLog.exists()) {
            return;
        }
        try {
            StringBuffer trace = new StringBuffer();
            printWriter = new PrintWriter(crashLog);
            printWriter.println(formatTime);
            trace.append(getPhoneInfo(mContext));
            Writer result = new StringWriter();
            printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            String stackTrace = result.toString();
            printWriter.close();
            trace.append(stackTrace);
            bw = new BufferedWriter(new FileWriter(crashLog));
            bw.write(trace.toString());
            bw.flush();
        } catch (Exception e1) {
           LogUtils.error(e1.toString());
        }finally {
            printWriter.close();
            try {
                bw.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public String getPhoneInfo(Context mContext) {
        StringBuffer mBuffer = new StringBuffer();
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                    metrics);
            double x          = Math.pow(metrics.widthPixels / metrics.xdpi, 2);
            double y          = Math.pow(metrics.heightPixels / metrics.ydpi, 2);
            double screenSize = Math.sqrt(x + y);
            mBuffer.append("screenSize: ");
            mBuffer.append(screenSize);
            mBuffer.append("\n");

            mBuffer.append("width: ");
            mBuffer.append(metrics.widthPixels);
            mBuffer.append("\n");

            mBuffer.append("height: ");
            mBuffer.append(metrics.heightPixels);
            mBuffer.append("\n");

            mBuffer.append("AppVersion: ");
            mBuffer.append(mVersionName);
            mBuffer.append("\n");

            mBuffer.append("App Version Code: ");
            mBuffer.append(mVersionCode);
            mBuffer.append("\n");

            mBuffer.append("Android Version: ");
            mBuffer.append(Build.VERSION.RELEASE);
            mBuffer.append("\n");
            mBuffer.append("Android Version Code: ");
            mBuffer.append(Build.VERSION.SDK_INT);
            mBuffer.append("\n");

            mBuffer.append("Vendor：");
            mBuffer.append(Build.MANUFACTURER);
            mBuffer.append("\n");

            mBuffer.append("Model：");
            mBuffer.append(Build.MODEL);
            mBuffer.append("\n");

            mBuffer.append("SerialNumber：");
            mBuffer.append(Build.SERIAL);
            mBuffer.append("\n");

            mBuffer.append("CPU ABI：");
            mBuffer.append(Build.CPU_ABI);
            mBuffer.append("\n");
            mBuffer.append("CPU ABI 2：");
            mBuffer.append(Build.CPU_ABI2);
            mBuffer.append("\n");
        } catch (Exception e) {
           LogUtils.error(e.toString());
        }
        return mBuffer.toString();
    }

    public boolean isSdCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static CrashHandler getInstance() {
        if (null == mCrashHandlerInstance) {
            mCrashHandlerInstance = new CrashHandler();
        }
        return mCrashHandlerInstance;
    }

    public void initCrashHandler(Context context) {
        this.mContext = context;
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                                               PackageManager.GET_ACTIVITIES);
            mVersionCode = String.valueOf(pi.versionCode);
            mVersionName = pi.versionName;
        } catch (Exception e) {
           LogUtils.error(e.toString());
        }
    }
}
