package com.mediatek.dingdingautopush.module.service;

import android.app.Notification;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;

import com.andr.common.tool.log.LoggerUtil;
import com.auto.assist.accessibility.AutoCoreService;
import com.auto.assist.accessibility.util.LogUtil;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskReqInfo;
import com.mediatek.dingdingautopush.model.storge.local.StorageManager;
import com.mediatek.dingdingautopush.util.GlobalConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class MainAccessService extends AutoCoreService
{


    @Override
    public void onAccessEvent(AccessibilityEvent event)
    {
        //实现自己的逻辑
        jumpNot(event);

    }


    public void jumpNot(AccessibilityEvent event)
    {
        //        LogUtil.D("event->"+event);
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED && event.getPackageName().equals(GlobalConfig.dingding_PakeName))
        {

            //获取Parcelable对象
            Parcelable data = event.getParcelableData();
            if (data == null)
            {
                return;
            }
            //判断是否是Notification对象
            if (data instanceof Notification)
            {

                Notification notification = (Notification) data;

                String tikeText = notification.tickerText == null ? "" : notification.tickerText.toString();
                String notTitle = notification.extras.getString("android.title") == null ? "" : notification.extras.getString("android.title");//标题
                String subText = notification.extras.getString("android.subText") == null ? "" : notification.extras.getString("android.subText");//摘要
                String text = notification.extras.getString("android.text") == null ? "" : notification.extras.getString("android.text");  //正文
                String postTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(notification.when));   //通知时间

                LogUtil.D("通知时间-->" + postTime);
                LogUtil.D("通知-->tikeText:" + tikeText);
                LoggerUtil.d("通知-->标题:" + notTitle + "--摘要--" + subText + "--正文--" + text);


                //首先判断通知时间是不是当前时间
                String nowTime = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                //如果是当天
                if (nowTime.equals(postTime))
                {
                    if (text == null)
                        return;
                    if (text.contains("上班打卡成功") ||text.contains("极速打卡成功")|| text.contains("下班打卡成功"))
                    {

                        SubmtTaskReqInfo submtTaskReqInfo = StorageManager.getTaskData();
                        if (submtTaskReqInfo != null)
                        {
                            LoggerUtil.d("保存通知数据...");
                            submtTaskReqInfo.setTaskState("1");
                            submtTaskReqInfo.setTaskDesc(tikeText);
                            StorageManager.setTaskData(submtTaskReqInfo);
                        }

                    }

                }


            }
        }
    }
}
