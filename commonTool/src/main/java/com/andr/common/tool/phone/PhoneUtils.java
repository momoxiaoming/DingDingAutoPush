package com.andr.common.tool.phone;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.andr.common.tool.util.PermissionsUtil;
import com.andr.common.tool.util.StringUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoming on 2018/9/18.
 */

public  class PhoneUtils  {

    private static final String TEL_MANAGER = "android.telephony.TelephonyManager";
    private static final String GET_SUB_IMEI = "getDeviceIdGemini";


    /**
     * 获取android id
     */
    @SuppressLint("HardwareIds")
    public static String getAndId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取Imei
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImei(Context context) {
        String imei = null;

        if (PermissionsUtil.hasPermissions(context, new String[]{"android.permission.READ_PHONE_STATE"})) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                imei = tm.getDeviceId();
            }
        }
        return StringUtil.setStringIfEmpty(imei);
    }

    /**
     * 获取imsi
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImsi(Context context) {
        String imsi = null;

        if (PermissionsUtil.hasPermissions(context, new String[]{"android.permission.READ_PHONE_STATE"})) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (tm != null)// && isSimReady(context))
            {
                imsi = tm.getSubscriberId();
            }
        }
        return StringUtil.setStringIfEmpty(imsi);
    }

    /**
     * 获取机型
     */
    public static String getMode() {
        return StringUtil.setStringIfEmpty(Build.MODEL);
    }


    public static String getBrand() {
        return StringUtil.setStringIfEmpty(Build.BRAND);
    }


    public static String getGsmVersionRilImpl() {
        return getSystemPropertie("gsm.version.ril-impl");
    }


    /**
     * 获取系统配置文件
     */
    public static String getSystemPropertie(String propertie) {
        String value = "unknown";

        value = System.getProperty(propertie);
        if (null == value) {

            try {
                Class<?> classType = Class.forName("android.os.SystemProperties");
                Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});

                value = (String) getMethod.invoke(classType, new Object[]{propertie});



            } catch (Exception exp) {
            }
        }

        return StringUtil.setStringIfEmpty(value);
    }

    /**
     * 获取硬件名称
     */
    public static String getHardwareName() {
        return StringUtil.setStringIfEmpty(getSystemPropertie("ro.hardware"));
    }


    /**
     * 获取系统版本
     */
    public static int getSysSdkVer() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取手机分辨率
     */
    public static String getScreenSize(Context context) {
        if (null != context) {
            WindowManager wndManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wndManager.getDefaultDisplay();
            if (null != display) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                return displayMetrics.widthPixels + "_" + displayMetrics.heightPixels;
            }
        }
        return null;
    }



    public static String getDevicesSerialNumber() {
        return getSystemPropertie("ro.serialno");
    }



    public static String getNetWorkOperator(Context context) {
        String rlt = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            rlt = tm.getNetworkOperator();
        }
        return StringUtil.setStringIfEmpty(rlt);
    }

    /**
     * 获取iccId
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIccid(Context context) {
        String rlt = null;

        if (PermissionsUtil.hasPermissions(context, new String[]{"android.permission.READ_PHONE_STATE"})) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                rlt = tm.getSimSerialNumber();
            }
        }
        return (StringUtil.isValidate(rlt) ? rlt : "");
    }

    /**
     * 是否是联发科cpu
     */
    public static boolean isMtkPlatform(Context context) {
        boolean ret = false;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> mLoadClass = Class.forName(TEL_MANAGER);
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getImei = mLoadClass.getMethod(GET_SUB_IMEI, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = 0;
            getImei.invoke(telephonyManager, obParameter);
            ret = true;
        } catch (Exception e) {
            ret = false;
        }
        return ret;
    }





    /**
     * 获取内置sd卡大小
     */
    public static String getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获取外置sd卡大小
     */
    public static long getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 判断是否在桌面
     */
    public static boolean isHome(Context context) {
        if(null != context)
        {
            List<String> packages = new ArrayList<String>();
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo info : resolveInfo)
            {
                packages.add(info.activityInfo.packageName);
                System.out.println(info.activityInfo.packageName);
            }
            List<String> homes = packages;
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
            if(null != homes && homes.size() > 0 && null != rti && rti.size() > 0)
            {
                return homes.contains(rti.get(0).topActivity.getPackageName());
            }
        }
        return false;
    }
}
