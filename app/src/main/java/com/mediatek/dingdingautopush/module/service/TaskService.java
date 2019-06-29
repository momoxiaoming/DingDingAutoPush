package com.mediatek.dingdingautopush.module.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.andr.common.tool.apk.AppUtils;
import com.andr.common.tool.json.GsonUtil;
import com.andr.common.tool.log.LoggerUtil;
import com.andr.common.tool.util.StringUtil;
import com.mediatek.dingdingautopush.model.DataCenter;
import com.mediatek.dingdingautopush.model.info.pullTask.LoginDataInfo;
import com.mediatek.dingdingautopush.model.info.pullTask.PullTaskResInfo;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskReqInfo;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskResInfo;
import com.mediatek.dingdingautopush.model.manager.DataManager;
import com.mediatek.dingdingautopush.model.manager.DataSource;
import com.mediatek.dingdingautopush.model.storge.local.StorageManager;
import com.mediatek.dingdingautopush.module.script.DDLoginScript;
import com.mediatek.dingdingautopush.module.script.DDSignScript;
import com.mediatek.dingdingautopush.module.script.ScriptCallBack;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class TaskService extends Service implements ScriptCallBack
{

    private boolean isRun = true;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        LoggerUtil.d("服务启动.当前进程:"+android.os.Process.myPid());
        startWork();

    }

    @Override
    public boolean stopService(Intent name)
    {


        isRun = false;
        DataCenter.getInstance().setRun(false);

        return super.stopService(name);
    }

    public void startWork()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                LoggerUtil.d("已启动");
                DataCenter.getInstance().setRun(true);


                while (isRun)
                {
                    LoggerUtil.d("监测当前线程: "+Thread.currentThread());
                    DataManager.getInstance().pullTask(new DataSource.DataCallBack<PullTaskResInfo>()
                    {
                        @Override
                        public void onSucess(PullTaskResInfo pullTaskResInfo)
                        {
                            doTask(pullTaskResInfo);
                        }

                        @Override
                        public void onFailed(String err)
                        {
                            LoggerUtil.e("无任务:" + err);
                        }
                    });

                    try
                    {
                        Thread.sleep(5000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }).start();


    }

    private void doTask(PullTaskResInfo pullTaskResInfo)
    {

        String resData = pullTaskResInfo.getResData();

        if (StringUtil.isStringEmpty(resData))
        {
            return;
        }
        LoggerUtil.d("resData" + resData);
        PullTaskResInfo.DataInfo dataInfo = GsonUtil.parseJsonWithGson(resData, PullTaskResInfo.DataInfo.class);
        if (dataInfo == null)
        {
            LoggerUtil.d("任务datainfo 为空");
            return;
        }

        if (!dataInfo.isReady("taskId", "taskType", "taskData"))
        {
            LoggerUtil.d("任务数据不全");
            return;
        }
        SubmtTaskReqInfo submtTaskReqInfo = new SubmtTaskReqInfo();
        submtTaskReqInfo.setTaskId(dataInfo.getTaskId());
        StorageManager.setTaskData(submtTaskReqInfo);

        int taskType = Integer.valueOf(dataInfo.getTaskType());

        try
        {
            switch (taskType)
            {
                case 1000: //更新任务
                    break;
                case 1001: //设备登录
                    doLoginAction(dataInfo);
                    break;
                case 1002: //上班签到
                    doSignAction(dataInfo);
                    break;
                case 1003://下班签到
                    doSignAction(dataInfo);
                    break;
                case 1004://早退下班签到
                    doSignAction(dataInfo);
                    break;
                case 1005://更新下班卡
                    doSignAction(dataInfo);
                    break;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void submtTask(boolean taskState, String taskId, String taskDesc)
    {

        SubmtTaskReqInfo notInfo = StorageManager.getTaskData();
        if (notInfo != null&&"1".equals(notInfo.getTaskState()))
        {
            LoggerUtil.d("监测到有通知结果,有限通知结果提交");
            submt(notInfo);

        } else
        {
            SubmtTaskReqInfo reqInfo = new SubmtTaskReqInfo();
            reqInfo.setTaskDesc(taskDesc);
            reqInfo.setTaskId(taskId);
            reqInfo.setTaskState(taskState ? "1" : "2");
            submt(reqInfo);

        }


    }

    private void submt(final SubmtTaskReqInfo reqInfo)
    {

        DataManager.getInstance().submtTask(reqInfo, new DataSource.DataCallBack<SubmtTaskResInfo>()
        {
            @Override
            public void onSucess(SubmtTaskResInfo submtTaskResInfo)
            {
                LoggerUtil.d("任务提交成功");
                StorageManager.cleanTaskData();

                //回到自己程序
                AppUtils.startApk(DataCenter.getInstance().getContext(),DataCenter.getInstance().getContext().getPackageName());
            }

            @Override
            public void onFailed(String err)
            {
                LoggerUtil.d("提交失败:"+err);
                AppUtils.startApk(DataCenter.getInstance().getContext(),DataCenter.getInstance().getContext().getPackageName());
            }
        });
    }

    private void doSignAction(PullTaskResInfo.DataInfo dataInfo)
    {
        LoginDataInfo taskData = dataInfo.getTaskData();
        if (taskData == null)
        {
            LoggerUtil.d("打卡任务数据为空");
            return;
        }



        DDSignScript ddSignScript = new DDSignScript(getApplicationContext(),dataInfo.getTaskId(), dataInfo.getTaskType());
        ddSignScript.doAction(this);

    }

    private void doLoginAction(PullTaskResInfo.DataInfo dataInfo) throws Exception
    {
        LoginDataInfo taskData = dataInfo.getTaskData();
        if (taskData == null)
        {
            LoggerUtil.d("登录数据为空");
            return;
        }


        DDLoginScript ddLoginScript = new DDLoginScript(taskData.getAccount(), dataInfo.getTaskId(), taskData.getPwd());
        ddLoginScript.doAction(this);


    }


    @Override
    public void onResult(boolean state, String taskId, String desc)
    {
        submtTask(state, taskId, desc);
    }
}
