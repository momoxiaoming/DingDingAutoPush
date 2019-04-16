package com.mediatek.dingdingautopush.module.act;

import android.app.Application;

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
    @Override
    public void onCreate()
    {
        super.onCreate();
        DataCenter.getInstance().setContext(this);
    }
}
