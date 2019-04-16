package com.andr.common.tool.cmd;

import com.andr.common.tool.file.FileUtils;
import com.andr.common.tool.log.LoggerUtil;
import com.andr.common.tool.phone.PhoneUtils;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/16
 *     desc  : new class
 * </pre>
 */
public  class AdbUtils
{
    /**
     * 通过adb 启动某个activity
     */
    public static boolean startApk(String pakeName, String activity) {
        if (pakeName.isEmpty() || activity.isEmpty()) {
            return false;
        }
        String cmd = "am start " + pakeName + "/" + activity;
        if (!isRoot()) {
            LoggerUtil.e("手机没有root!!");
            return false;
        }
        int code = CmdUtils.execRootCmdSilent(cmd);
        if (code != -1) {
            return true;
        }
        return false;
    }

    /**
     * 修改文件路径
     */
    public static boolean chomdFile(String cmd, String path) {
        String command = cmd + " " + path;
        try {
            int res = CmdUtils.execRootCmdSilent(command);

            if (res != -1) {
                return true;
            }
        } catch (Exception e) {
            LoggerUtil.e("权限修改失败");
        }
        return false;
    }


    /**
     * adb安装apk
     * @param filePath
     * @param tag 0是安装到data区,1为安装到系统区
     */
    public static boolean installApk(String filePath, int tag) {
        if (!FileUtils.isFileExist(filePath)) {
            LoggerUtil.e("文件不存在");
            return false;
        }
        if (tag == 0) {
            return installApkForData(filePath);
        } else {
            return installApkForSys(filePath);

        }
    }

    /**
     * adb安装并启动apk
     * @param filePath 文件路径
     * @param tag 0是安装到data区,1为安装到系统区
     */
    public static boolean installApkAndStart(String filePath, String pakeName, String className, int tag) {
        if (!FileUtils.isFileExist(filePath)) {
            LoggerUtil.e("文件不存在");
            return false;
        }
        if (tag == 0) {
            return installApkForDataAndStart(filePath, pakeName, className);
        } else {
            return installApkForSys(filePath);

        }
    }

    private static boolean installApkForData(String filePath) {
        String cmd = "pm install -r " + filePath;
        if (!isRoot()) {
            LoggerUtil.e("手机没有root!!");
            return false;
        }
        String code = "";
        try {
            code = CmdUtils.executeShellCommand(cmd);
            LoggerUtil.d("adb安装输出-" + code);
            if ("Success".equals(code)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean installApkForDataAndStart(String filePath, String pakeName, String className) {
        String cmd = "pm install -r " + filePath + ";am start " + pakeName + "/" + className;
        if (!isRoot()) {
            LoggerUtil.e("手机没有root!!");
            return false;
        }
        int code = CmdUtils.execRootCmdSilent(cmd);
        if (code != -1) {
            return true;
        }
        return false;
    }


    private static boolean installApkForSys(String filePath) {
        //判断版本
        String install_path = "";
        if (PhoneUtils.getSysSdkVer() >= 19) {//4.4
            install_path = "/system/priv-app/";
        } else {
            install_path = "/system/app/";
        }

        //改变文件权限
        String ret_1 = "chmod 777 " + filePath;
        CmdUtils.execRootCmdSilent(ret_1);


        //挂载system 使其可写
        String ret = "mount -o remount,rw /dev/block/stl6 /system";
        CmdUtils.execRootCmdSilent(ret);

        //拷贝文件到相应的系统路径
        String[] fileNames = filePath.split(File.separator);
        String fileName = fileNames[fileNames.length - 1];

        String sysPath = install_path + fileName;

        String ret_2 = "cp " + filePath + " " + sysPath;

        CmdUtils.execRootCmdSilent(ret_2);

        //修改文件权限644
        CmdUtils.execRootCmdSilent("chmod 644 " + sysPath);

        //恢复system 挂载
        CmdUtils.execRootCmdSilent("mount -o remount,ro /dev/block/stl6 /system");


        //判断系统sdk是否大于21, 大于则需要重启
        if (PhoneUtils.getSysSdkVer() >= 21) {
            CmdUtils.execRootCmdSilent("reboot");
        }


        try {
            Thread.sleep(2000);  //休息两秒

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (FileUtils.isFileExist(sysPath)) {
            CmdUtils.execRootCmdSilent("rm -r " + filePath);
            return true;
        } else {
            return false;
        }

    }


    /**
     * adb 卸载apk
     * @param pakeName 包名
     * @return
     */
    public static boolean unInstallApk(String pakeName) {
        String cmd = "pm uninstall " + pakeName;
        if (!isRoot()) {
            LoggerUtil.e("手机没有root!!");
            return false;
        }

        //首先执行卸载
        int code = CmdUtils.execRootCmdSilent(cmd);
        if (code != -1) {
            return true;
        }
        return false;
    }


    /**
     * 是否root
     * @return
     */
    public static boolean isRoot() {
        int ret = CmdUtils.execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        if (ret != -1) {
            return true;
        } else {
            return false;
        }

    }
}



