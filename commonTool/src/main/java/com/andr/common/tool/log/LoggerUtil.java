package com.andr.common.tool.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.text.SimpleDateFormat;

public class LoggerUtil
{

    private static String TAG_NAME = "LoggerTag";
    private static boolean IS_CACHE = false;     //true表示缓存日志到文件
    private static boolean IS_LOGGABLE = false;     //true表示输出Log

    /**
     * 日志系统初始化
     * @param Tag 打印的Tag
     * @param openLog 是否打开log
     * @param saveLog 是否保存log
     */
    public static void initLogger(String Tag, boolean openLog, boolean saveLog) {

        TAG_NAME = Tag;
        IS_LOGGABLE = openLog;
        IS_CACHE = saveLog;


        init();
    }


    private static void init() {


        if (!IS_LOGGABLE)   // 保存Log信息到File文件中,文件路径 /sdcard/logger
        {
            //DiskLogAdapter 添加配置
            FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
//                    .date(new Date())  // 设置保存的时间，默认data();
                    .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))  // 设置保存的格式化时间,默认 yyyy.MM.dd HH:mm:ss.SSS
//                    .logStrategy()  // 更改日志策略以打印出。默认的logcat
                    .tag(TAG_NAME)  // 自定义日志标记
                    .build();
            Logger.addLogAdapter(new DiskLogAdapter(formatStrategy)
            {
                @Override
                public boolean isLoggable(int priority, @Nullable String tag) {
                    return IS_CACHE;
                }
            });
        } else   // 不保存Log信息
        {
            //AndroidLogAdapter 添加的配置
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                    // 是否显示线程消息，默认为true
                    .methodCount(0)         // (Optional) How many method line to show. Default 2
                    // 要显示多少个方法行。默认值为2
                    .methodOffset(1)        // (Optional) Skips some method invokes in stack trace. Default 5
                    // 跳过堆栈跟踪中的某些方法调用。默认为 5
                    //  .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                    // 更改日志策略以打印出。默认的logcat
                    .tag(TAG_NAME)   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                    // 每个日志的自定义标记。默认pretty_logger
                    .build();
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy)
            {
                @Override
                public boolean isLoggable(int priority, @Nullable String tag) {
                    return IS_LOGGABLE;
                }
            });
        }

    }

    public static void d(@Nullable Object object) {
        Logger.d(object);
    }


    public static void i(@NonNull String message, @Nullable Object... args) {
        Logger.i(message, args);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        Logger.d(message, args);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        Logger.e(message, args);
    }

    public static void json(String json) {
        Logger.json(json);

    }
    public static void xml(String xml) {
        Logger.xml(xml);

    }


}
