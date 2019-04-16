package com.mediatek.dingdingautopush.model.manager;

import android.app.Activity;
import android.widget.Toast;

import com.andr.common.tool.apk.ApkUtil;
import com.andr.common.tool.log.LoggerUtil;
import com.andr.view.dialog.LoadingDialogManager;
import com.mediatek.dingdingautopush.model.DataCenter;
import com.mediatek.dingdingautopush.model.info.appUpdate.AppUpdateReqInfo;
import com.mediatek.dingdingautopush.model.info.appUpdate.AppUpdateResInfo;
import com.mediatek.dingdingautopush.model.info.appUpdate.AppUpdateTask;
import com.mediatek.dingdingautopush.model.info.pullTask.PullTask;
import com.mediatek.dingdingautopush.model.info.pullTask.PullTaskReqInfo;
import com.mediatek.dingdingautopush.model.info.pullTask.PullTaskResInfo;
import com.mediatek.dingdingautopush.model.info.regDev.RegDevReqInfo;
import com.mediatek.dingdingautopush.model.info.regDev.RegDevResInfo;
import com.mediatek.dingdingautopush.model.info.regDev.RegDevTask;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTask;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskReqInfo;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskResInfo;
import com.mediatek.dingdingautopush.model.storge.local.StorageManager;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class DataManager implements DataSource
{

    private static DataManager mIntence;

    public static DataManager getInstance()
    {
        if (null == mIntence)
        {
            synchronized (DataManager.class)
            {
                if (null == mIntence)
                {
                    mIntence = new DataManager();
                }
            }
        }
        return mIntence;
    }

    @Override
    public void regToken(DataCallBack<RegDevResInfo> dataCallBack)
    {

        RegDevReqInfo reqInfo = new RegDevReqInfo();
        new RegDevTask(reqInfo, dataCallBack);
    }

    @Override
    public void pullTask(DataCallBack<PullTaskResInfo> dataCallBack)
    {
        PullTaskReqInfo pullTaskReqInfo = new PullTaskReqInfo();
        pullTaskReqInfo.setDevToken(StorageManager.readToken());
        new PullTask().doTask(pullTaskReqInfo, dataCallBack);

    }


    @Override
    public void appUpdate(final Activity activity)
    {

        AppUpdateReqInfo reqAppUpdateInfo = new AppUpdateReqInfo();

        reqAppUpdateInfo.setApp_ver(""+ApkUtil.getInstance().getAppVersionCode(DataCenter.getInstance().getContext(),DataCenter.getInstance().getContext().getPackageName()));
        reqAppUpdateInfo.setDev_token(StorageManager.readToken());


        final LoadingDialogManager dialogManager = new LoadingDialogManager(activity);
        dialogManager.show();
        new AppUpdateTask(reqAppUpdateInfo, new DataCallBack<AppUpdateResInfo>()
        {
            @Override
            public void onSucess(AppUpdateResInfo resInfo)
            {
                dialogManager.dismiss();
                AppUpdateTask.downApkFile(activity, resInfo);
            }

            @Override
            public void onFailed(String err)
            {
                dialogManager.dismiss();

                Toast.makeText(activity, err + "", Toast.LENGTH_SHORT).show();
            }


        });

    }

    @Override
    public void submtTask(SubmtTaskReqInfo taskReqInfo, DataCallBack<SubmtTaskResInfo> dataCallBack)
    {
        if (taskReqInfo != null)
        {
            LoggerUtil.d("开始提交任务");
            new SubmtTask(taskReqInfo, dataCallBack);
        }
    }


}
