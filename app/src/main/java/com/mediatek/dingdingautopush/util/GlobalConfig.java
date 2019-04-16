package com.mediatek.dingdingautopush.util;

import com.andr.common.tool.file.FileUtil;

import java.io.File;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class GlobalConfig
{
    public static final String DOCOMPENT_NAME       = "深圳市火星网络有限公司";// 要打卡的公司名

    public static final String dingding_PakeName    = "com.alibaba.android.rimet";

    public static final String MAIN_PATH            = FileUtil.getInstance().getExStoragePath()+ File.separator+"autodingding";
    public static final String CONFIG_PATH          = MAIN_PATH+ File.separator+"cfg";
    public static final String DOWN_FILE_PATH       = MAIN_PATH+ File.separator+"file";
    public static final String CONFIG_FILE_PATH     = CONFIG_PATH+ File.separator+"autodd.cfg";
    public static final String TASK_DATA_FILE_PATH  = CONFIG_PATH+ File.separator+"taskData.cfg";
    public static final String LOG_FILE_PATH        = FileUtil.getInstance().getExStoragePath()+ File.separator+"logger";


    public static final String MAIN_URL             = "http://api.momoxiaoming.com:9102/ddPush_andr/";
    public static final String REQ_TASK_MAIN_URL    = MAIN_URL + "taskQury/";
    public static final String REQ_REGIEST_URL      = MAIN_URL + "regDev/";
    public static final String UPDATE_APK_URL       = MAIN_URL + "updateApk/";
    public static final String REQ_SUBMIT_URL       = MAIN_URL + "taskSubmt/";
}
