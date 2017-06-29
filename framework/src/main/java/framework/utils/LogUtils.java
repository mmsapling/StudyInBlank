
package framework.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * 日志输出, 代理android.util.Log类
 */
public abstract class LogUtils {
    private static String DEFAULT_TAG = "tylz";
    private final static boolean IS_WRITE = false;//写文件开关

    /**
     * 日志级别定义
     */
    private static enum Level {

        /**
         * 详尽
         */
        VERBOSE(2),

        /**
         * 调试
         */
        DEBUG(3),

        /**
         * 消息
         */
        INFO(4),

        /**
         * 警告
         */
        WARN(5),

        /**
         * 错误
         */
        ERROR(6),

        /**
         * 断言
         */
        ASSERT(7);

        private final int level;

        private Level(int level) {
            this.level = level;
        }
    }

    /**
     * 设置日志输出级别, TODO 生产时可以将级别设置为 Level.ERROR
     */
    private static final Level LEVEL = Level.DEBUG;

    /**
     * 日志标签
     */
    private static final String LOG_TAG = LogUtils.class.getSimpleName();

    /**
     * 日志提示
     */
    private static final String LOG_TIP = "Log failed";

    private static final String LOG_FILE_NAME = "tylz.log";
    public static void debug(String message) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.DEBUG.level) {
            try {
                Log.d(getTag(DEFAULT_TAG), message);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.DEBUG.name(), DEFAULT_TAG, message, null);
        }
    }

    public static void debug(String tag, String message) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.DEBUG.level) {
            try {
                Log.d(getTag(tag), message);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.DEBUG.name(), tag, message, null);
        }
    }

    public static void debug(String tag, String message, Throwable tr) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.DEBUG.level) {
            try {
                Log.d(getTag(tag), message, tr);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.DEBUG.name(), tag, message, tr);
        }
    }

    /**
     * error
     *
     * @param tag
     * @param message
     */
    public static void error(String tag, String message) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.ERROR.level) {
            try {
                Log.e(getTag(tag), message);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.ERROR.name(), tag, message, null);
        }
    }
    public static void error(String message) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.ERROR.level) {
            try {
                Log.e(getTag(DEFAULT_TAG), message);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.ERROR.name(), DEFAULT_TAG, message, null);
        }
    }
    /**
     * error
     *
     * @param tag
     * @param message
     * @param tr
     */
    public static void error(String tag, String message, Throwable tr) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.ERROR.level) {
            try {
                Log.e(getTag(tag), message, tr);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.ERROR.name(), tag, message, tr);
        }
    }

    /**
     * info
     *
     * @param tag
     * @param message
     */
    public static void info(String tag, String message) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.DEBUG.level) {
            try {
                Log.i(getTag(tag), message);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.INFO.name(), tag, message, null);
        }
    }

    /**
     * warn
     *
     * @param tag
     * @param message
     * @param tr
     */
    public static void warn(String tag, String message, Throwable tr) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.INFO.level) {
            try {
                Log.w(getTag(tag), message, tr);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.WARN.name(), tag, message, tr);
        }
    }

    public static void warn(String tag, String message) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.DEBUG.level) {
            try {
                Log.i(getTag(tag), message);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.WARN.name(), tag, message, null);
        }
    }
    public static void warn( String message) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.DEBUG.level) {
            try {
                Log.i(getTag(DEFAULT_TAG), message);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(LEVEL.WARN.name(), DEFAULT_TAG, message, null);
        }
    }
    /**
     * info
     *
     * @param tag
     * @param message
     * @param tr
     */
    public static void info(String tag, String message, Throwable tr) {
        if (!LogManager.IS_DEBUG) {
            return;
        }
        if (LEVEL.level <= Level.INFO.level) {
            try {
                Log.i(getTag(tag), message, tr);
            } catch (Throwable e) {
                logError(e);
            }
            writeToFile(Level.INFO.name(), tag, message, tr);
        }
    }

    /**
     * 在写日志出错的时候记录下错误信息
     *
     * @param e
     */
    private static void logError(Throwable e) {
        if (LEVEL.level <= Level.ERROR.level) {
            Log.e(getTag(LOG_TAG), LOG_TIP, e);
        }
    }

    private static void writeToFile(String level, String tag, String message, Throwable tr) {

		/*文件日志开关*/
        if (!LogManager.IS_DEBUG)
            return;
        if (!IS_WRITE)
            return;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                String foldername = Environment.getExternalStorageDirectory().getPath() + "/ccb/ccblog";
                File folder = new File(foldername);

                if (!folder.exists()) {
                    if (!folder.mkdirs()) {
                        warn(tag, "Failed to create log folder[" + foldername + "]");
                        return;
                    }
                }
                File targetFile = new File(foldername, LOG_FILE_NAME);

                if (!targetFile.exists()) {
                    targetFile.createNewFile();
                }
                String content = String.format("%n%s [%s] %s %s", new Date(), level, tag, message);
                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(targetFile, true));
                    os.write(content.getBytes("utf-8"));
                    if (tr != null) {
                        PrintStream ps = new PrintStream(os);
                        tr.printStackTrace(ps);
                    }
                } catch (Throwable e) {
                    logError(e);
                } finally {
                    if (os != null) {
                        os.close();
                    }
                }
            }
        } catch (Exception e) {
            logError(e);
        }
    }

    private static String getTag(String tag) {
        return  tag + " ";
    }

}
