package com.mediatek.dingdingautopush.module.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <pre>
 *     author:
 *     time  : 2019-11-19
 *     desc  : new class
 * </pre>
 */
public class MainReciver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {

        context.startService(new Intent(context, TaskService.class));
    }
}
