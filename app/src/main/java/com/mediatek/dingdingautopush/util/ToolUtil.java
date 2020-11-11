package com.mediatek.dingdingautopush.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.andr.common.tool.log.LoggerUtil;

import java.io.File;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class ToolUtil
{
    /**
     * 检查系统设置：是否开启辅助服务
     *
     * @param service 辅助服务
     */
    public static boolean isSettingOpen(Class service, Context cxt)
    {
        try
        {
            int enable = Settings.Secure.getInt(cxt.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
            if (enable != 1)
                return false;
            String services = Settings.Secure.getString(cxt.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (!TextUtils.isEmpty(services))
            {
                TextUtils.SimpleStringSplitter split = new TextUtils.SimpleStringSplitter(':');
                split.setString(services);
                while (split.hasNext())
                { // 遍历所有已开启的辅助服务名
                    if (split.next().equalsIgnoreCase(cxt.getPackageName() + "/" + service.getName()))
                        return true;
                }
            }
        } catch (Throwable e)
        {//若出现异常，则说明该手机设置被厂商篡改了,需要适配
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳转到系统设置：开启辅助服务
     */
    public static void jumpToSetting(final Context cxt)
    {
        try
        {
            cxt.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        } catch (Throwable e)
        {//若出现异常，则说明该手机设置被厂商篡改了,需要适配
            try
            {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cxt.startActivity(intent);
            } catch (Throwable e2)
            {
               e.printStackTrace();
            }
        }
    }

    /**
     * 非静默,安装app
     *
     * @param context
     */
    public static void InstallApp(File file, Activity context) {
        if (file == null)
        {
            LoggerUtil.e("要安装的app文件为空!");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {

            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".app.provider", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else
        {
            uri = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.setDataAndType(uri, "application/vnd.android.package-archive");

        context.startActivity(intent);


    }
}
