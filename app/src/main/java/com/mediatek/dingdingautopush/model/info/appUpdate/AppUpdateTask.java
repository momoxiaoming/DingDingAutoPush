package com.mediatek.dingdingautopush.model.info.appUpdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.andr.common.tool.apk.AppUtils;
import com.andr.common.tool.encode.MD5Util;
import com.andr.common.tool.json.GsonUtil;
import com.andr.common.tool.net.okhttp.HttpManager;
import com.andr.common.tool.net.okhttp.NetCallBack;
import com.mediatek.dingdingautopush.model.info.base.BaseIntfTask;
import com.mediatek.dingdingautopush.model.manager.DataSource;
import com.mediatek.dingdingautopush.util.GlobalConfig;
import com.mediatek.dingdingautopush.util.ToolUtil;

import java.io.File;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class AppUpdateTask extends BaseIntfTask
{


    public AppUpdateTask(AppUpdateReqInfo reqInfo, DataSource.DataCallBack<AppUpdateResInfo> callBack)
    {
        super(GlobalConfig.UPDATE_APK_URL, reqInfo, AppUpdateResInfo.class, callBack);


    }

    /**
     * 开始下载
     *
     * @param info
     */
    public static void downApkFile(final Activity activity, final AppUpdateResInfo info)
    {


        String resData = info.getResData();
        AppUpdateResInfo.DataInfo dataInfo = GsonUtil.parseJsonWithGson(resData, AppUpdateResInfo.DataInfo.class);
        if (dataInfo != null && dataInfo.isReady("apkVer", "apkUrl"))
        {
            String url = dataInfo.getApkUrl();
            String appVer = dataInfo.getApkVer();
            String appV = "" + AppUtils.getAppVersionCode(activity, activity.getPackageName());
            if (Integer.valueOf(appVer) <= Integer.valueOf(appV))
            {
                Toast.makeText(activity, "无需更新", Toast.LENGTH_SHORT).show();
                return;
            }

            final ProgressDialog updateDialog = new ProgressDialog(activity);
            updateDialog.setMessage("正在下载..");
            updateDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            updateDialog.setCanceledOnTouchOutside(false);
            updateDialog.setCancelable(false);
            updateDialog.show();


            HttpManager.getInstance(activity).FILE_ASY_DOWN(url, GlobalConfig.DOWN_FILE_PATH, MD5Util.getMD5String(url) + ".apk", new NetCallBack.FileStateCallBack()
            {
                @Override
                public void onFinish(final File file)
                {
                    updateDialog.dismiss();

                    if (file == null)
                    {
                        Toast.makeText(activity, "文件校验失败,请重新下载", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        ToolUtil.InstallApp(file, activity);
                    }
                }

                @Override
                public void onProgress(long fileLenth, long current)
                {
                    updateDialog.setMax((int) fileLenth);
                    updateDialog.setProgress((int) current);
                }

                @Override
                public void onError(String msg)
                {
                    updateDialog.dismiss();
                    Toast.makeText(activity, ""+msg, Toast.LENGTH_SHORT).show();
                }
            });


        } else
        {
            Toast.makeText(activity, "下载信息不全,无法下载", Toast.LENGTH_SHORT).show();

        }


    }
}
