package com.mediatek.dingdingautopush.module.act;

import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.andr.common.tool.log.LoggerUtil;
import com.mediatek.dingdingautopush.BuildConfig;
import com.mediatek.dingdingautopush.model.DataCenter;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class HtApplication extends Application
{
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate()
    {
        super.onCreate();
        DataCenter.getInstance().setContext(this);
        LoggerUtil.initLogger("dingdingAssist", BuildConfig.DEBUG,true);

    }
}
