package com.andr.common.tool.apk;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;

import com.andr.common.tool.encode.SecUtils;
import com.andr.common.tool.encode.StrCharset;
import com.andr.common.tool.log.LoggerUtil;
import com.andr.common.tool.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoming on 2018/9/18.
 */

public  class AppUtils
{

    /**
     * 获取packInfo
     */
    public static PackageInfo getPackageInfo(Context context, String packageName, int flag)
    {
        PackageInfo rlt = null;
        if (null == context || StringUtil.isStringEmpty(packageName))
        {
            return rlt;
        }
        try
        {
            PackageManager pm = context.getPackageManager();
            if (pm != null)
            {
                rlt = pm.getPackageInfo(packageName, flag);
            }
        } catch (PackageManager.NameNotFoundException e)
        {
            LoggerUtil.e("找不到该包名");
        }

        return rlt;
    }


    /**
     * 获取签名
     * @param context
     * @param pakeName
     * @return
     */
    public static Signature[] getSignature(Context context, String pakeName)
    {
        Signature[] rlt = null;

        PackageInfo packInfo = getPackageInfo(context, pakeName, PackageManager.GET_SIGNATURES);
        if (null != packInfo)
        {
            rlt = packInfo.signatures;
        }

        return rlt;
    }


    /**
     * 获取包名
     */
    public static String getPackage(Context context) {
        return StringUtil.setStringIfEmpty(context.getPackageName());
    }

    /**
     * 获取签名MD5
     */
    public static String getSignatureMd5(Context context, String pakeName) {
        String rlt = null;

        Signature[] signs = getSignature(context, pakeName);
        if (null != signs && signs.length > 0) {

            StringBuffer sb = new StringBuffer();
            for (Signature sig : signs) {
                sb.append(sig.toCharsString());
            }

            rlt = SecUtils.getInformationFingerprintByMD5(sb.toString(), StrCharset.UTF_8);
        }
        return StringUtil.isStringEmpty(rlt) ? null : rlt;
    }

    /**
     * 获取app名称
     */
    public static String getAppName(Context context) {
        if (null == context) {
            return null;
        }

        PackageManager packageManager = context.getPackageManager();
        PackageInfo pakeInfo = getPackageInfo(context, context.getPackageName(), PackageManager
                .GET_UNINSTALLED_PACKAGES);
        if (pakeInfo != null) {
            ApplicationInfo appInfo = pakeInfo.applicationInfo;
            return appInfo.loadLabel(packageManager)
                    .toString();
        }
        return null;
    }


    /**
     * 获取app版本名称
     */
    public static String getAppVersionName(Context context) {
        if (null == context) {
            return null;
        }

        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(), PackageManager
                .GET_UNINSTALLED_PACKAGES);
        return null == packageInfo ? null : packageInfo.versionName;
    }


    /**
     * 获取app版本号
     */
    public static  int getAppVersionCode(Context context, String pakeName) {
        if (null == context) {
            return 0;
        }

        PackageInfo packageInfo = getPackageInfo(context, pakeName, PackageManager
                .GET_UNINSTALLED_PACKAGES);
        return null == packageInfo ? 0 : packageInfo.versionCode;
    }

    /**
     * 判断apk是否存在
     */
    public static boolean isApkExist(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        return packageInfo != null;
    }


    /**
     * 获取meta的值
     */
    public static String getMetaData(Context context, String key) {

        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName(), PackageManager.GET_META_DATA);

        ApplicationInfo appInfo = packageInfo.applicationInfo;
        Bundle metaData = (null != appInfo) ? appInfo.metaData : null;

        return (null != metaData && !StringUtil.isStringEmpty(key)) ? String.valueOf(metaData.get(key)) : null;

    }

    /**
     * 通过包名启动apk
     */
    public static boolean startApk(Context context, String pakeName) {
        boolean rlt = false;

        if (isApkExist(context, pakeName)) {
            PackageManager pkgMgr = context.getPackageManager();
            if (null != pkgMgr) {
                Intent intent = pkgMgr.getLaunchIntentForPackage(pakeName);
                if (null != intent) {
                    context.startActivity(intent);
                    rlt = true;
                }
            }
        }

        return rlt;
    }



    /**
     * 通过包名停止apk
     */
    public static boolean stopApk(Context context, String pakeName) {
        boolean rlt = false;
        // 应用存在,且关闭的不是自己
        if (isApkExist(context, pakeName) && !context.getPackageName().equalsIgnoreCase(pakeName)) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (null != manager) {
                manager.killBackgroundProcesses(pakeName);
                rlt = true;
            }
        }

        return rlt;
    }


    /**
     * 获取已安装的所有apk的信息
     */
    public static List<ApkInfo> getInstallApkInfo(Context context) {
        ArrayList<ApkInfo> appList = new ArrayList<>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            ApkInfo tmpInfo = new ApkInfo();
            tmpInfo.apkName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            tmpInfo.pkgName = packageInfo.packageName;
            tmpInfo.apkVerName = packageInfo.versionName;
            tmpInfo.verCode = packageInfo.versionCode;
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);//如果非系统应用，则添加至appList
            }
        }
        return appList;
    }


    /**
     * 获取栈中的所有acitivity
     */
    public static List<ActivityManager.RunningTaskInfo> getRuningTaskInfo(Context context) {
        List<ActivityManager.RunningTaskInfo> rlt = null;

        if (null == context) {
            return rlt;
        }

        ActivityManager aManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (null != aManager) {
            rlt = aManager.getRunningTasks(100);
        }

        return rlt;
    }

    /**
     * 判断该acitivty是否在栈顶
     */
    public static boolean isAppRunningTopActivity(Context context, String activityName) {
        boolean rlt = false;

        if (null == context || TextUtils.isEmpty(activityName)) {
            return rlt;
        }

        List<ActivityManager.RunningTaskInfo> runningTasks = getRuningTaskInfo(context);
        if (null != runningTasks && !runningTasks.isEmpty()) {
            String topClsName = runningTasks.get(0).topActivity.getClassName();
            LoggerUtil.e("topClsName:" + topClsName);
            rlt = !TextUtils.isEmpty(topClsName) && topClsName.equals(activityName);
        }

        return rlt;
    }







    /**
     * 判断该是否为系统apk
     */
    public static boolean isSystemApp(Context context, String pakeName) {
        PackageInfo pi = getPackageInfo(context, pakeName, 0);
        if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用的启动页className
     */
    public static String getAppStartActivity(Context context, String pakeName) {
        if (pakeName.isEmpty()) {
            return "";
        }

        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(pakeName);
        if (intent != null) {
            ComponentName componentName = intent.getComponent();
            String name = componentName.getClassName();
            return name;
        }
        return "";


    }

}
