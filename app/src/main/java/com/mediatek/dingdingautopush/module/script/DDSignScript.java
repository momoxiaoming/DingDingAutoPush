package com.mediatek.dingdingautopush.module.script;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.andr.common.tool.log.LoggerUtil;
import com.auto.assist.accessibility.api.UiApi;
import com.mediatek.dingdingautopush.util.GlobalConfig;

import java.net.URLEncoder;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class DDSignScript
{

    private String taskId;
    private String desc;
    private String type;
    private boolean state = false;

    private Context context;


    public DDSignScript(Context context,String taskId, String type)
    {
        this.taskId = taskId;
        this.type = type;
        this.context=context;
    }


    public void doAction(final ScriptCallBack callBack)
    {

        int i = 0;
        while (i < 2)   //执行三次
        {
            doTask();

            if (state)
            {
                UiApi.backToDesk();
                callBack.onResult(true, taskId, desc);
            }

            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            i++;
        }

        UiApi.backToDesk();
        callBack.onResult(false, taskId, desc);


    }

    private void doTask()
    {
        UiApi.backToDesk();

        doDingdingPage();

        if (findControtPage())
        {
            try
            {
                Thread.sleep(2000);   //等待
                clckIknowDilog();
                signTask();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }


        } else
        {
            desc = "未找到公司选择窗节点";
        }


    }


    private void signTask()
    {
        switch (type)
        {
            case "1000": //更新任务
                break;

            case "1002": //上班签到
                up_sign();
                break;
            case "1003"://下班签到
                down_sign();
                break;
            case "1004"://早退下班签到
                leave_down_sign();
                break;
            case "1005"://更新下班卡
                update_down_sign();

                break;
        }
    }

    private void leave_down_sign()
    {
        boolean isrlt = UiApi.clickNodeByDesWithTimeOut(10000, "下班打卡");
        if (isrlt)
        {
            //
            //确定早退
            UiApi.clickNodeByTextWithTimeOut(5000, "确定");

            if (alertResult())
            {
                state = true;
                LoggerUtil.d("打卡成功");

            } else
            {
                desc = "未找到成功窗节点";
                LoggerUtil.d("未找到成功窗节点");
                state = false;

            }
        } else
        {
            state = false;
            desc = "未找到下班打卡按钮";
            LoggerUtil.d("未找到下班打卡按钮");
        }
    }

    private void update_down_sign()
    {
        boolean isrlt = UiApi.clickNodeByDesWithTimeOut(10000, "下班打卡");
        if (isrlt)
        {
            //
            //确定早退
            UiApi.clickNodeByTextWithTimeOut(5000, "确定");

            if (alertResult())
            {
                state = true;

            } else
            {
                desc = "未找到成功窗节点";
                LoggerUtil.d("未找到成功窗节点");
                state = false;

            }
        } else
        {
            state = false;
            desc = "未找到下班打卡按钮";
            LoggerUtil.d("未找到下班打卡按钮");
        }
    }


    //上班打卡
    private void up_sign()
    {
        LoggerUtil.d("正在查找并上班打卡按钮..");
        boolean isrlt = UiApi.clickNodeByDesWithTimeOut(5000, "上班打卡");
        if (isrlt)
        {
            if (alertResult())
            {
                state = true;

            } else
            {
                desc = "未找到成功窗节点";
                LoggerUtil.d("未找到成功窗节点,可能未到下班时间");
                state = false;

            }
        } else
        {
            state = false;
            desc = "未找到上班打卡按钮";
            LoggerUtil.d("未找到上班打卡按钮");
        }
    }

    //xia班打卡
    private void down_sign()
    {
        boolean isrlt = UiApi.clickNodeByDesWithTimeOut(5000, "下班打卡");
        if (isrlt)
        {
            if (alertResult())
            {
                state = true;

            } else
            {
                desc = "未找到成功窗节点";
                LoggerUtil.d("未找到成功窗节点");
                state = false;

            }
        } else
        {
            state = false;
            desc = "未找到下班打卡按钮";
            LoggerUtil.d("未找到下班打卡按钮");
        }
    }


    //点击掉我知道了的弹窗
    private void clckIknowDilog()
    {
        UiApi.clickNodeByTextWithTimeOut(5000, "我知道了");
    }


    private boolean alertResult()
    {
        String page = "{" + "'maxMustMills':5000," + "'maxOptionMills':5000," + "'must':{'desc':['我知道了']}" + "}";
        return UiApi.isMyNeedPage(page);
    }


    private boolean findControtPage()
    {

        return UiApi.clickNodeByTextWithTimeOut(20000, GlobalConfig.DOCOMPENT_NAME);

    }

    private void doDingdingPage()
    {
        String url = "dingtalk://dingtalkclient/page/link?url=" + URLEncoder.encode("https://attend.dingtalk.com/attend/index.html");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}
