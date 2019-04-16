package com.mediatek.dingdingautopush.module.script;

import android.content.Context;

import com.andr.common.tool.apk.ApkUtil;
import com.andr.common.tool.log.LoggerUtil;
import com.auto.assist.accessibility.api.UiApi;
import com.mediatek.dingdingautopush.model.DataCenter;
import com.mediatek.dingdingautopush.util.GlobalConfig;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class DDLoginScript
{
    private Context context = DataCenter.getInstance().getContext();

    private String loginAccount;
    private String loginPwd;
    private String taskId;
    private String desc;


    public DDLoginScript(String loginAccount, String taskId,String loginPwd)
    {
        this.loginAccount = loginAccount;
        this.loginPwd = loginPwd;
        this.taskId=taskId;

    }


    public void doAction(ScriptCallBack callBack)
    {

        int i = 0;
        while (i < 3)   //执行三次
        {
            if (doTask())
            {
                callBack.onResult(true,taskId, desc);
                return;
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
        callBack.onResult(false,taskId, desc);

    }

    private boolean doTask()
    {
        UiApi.backToDesk();

        ApkUtil.getInstance().startApk(context, GlobalConfig.dingding_PakeName);

        if (!isManPage())
        {
            if (isLoginPage())
            {
                doLogin();
                if (isManPage())
                {
                    //登录成功
                    LoggerUtil.d("登录成功");
                    desc = "登录成功";

                    return true;

                }
                desc = "登录之后未找到主页";
                return false;
            } else
            {
                LoggerUtil.d("不在主页,也不在登录页");
                desc = "不在主页,也不在登录页";
                return false;
            }
        } else
        {
            LoggerUtil.d("已在主页,无需登录");
            desc = "已在主页,无需登录";
            return true;
        }

    }

    /**
     * 判断是否在钉钉主页
     *
     * @return
     */
    private boolean isManPage()
    {
        String mainPage = "{" + "'maxMustMills':5000," + "'maxOptionMills':5000," + "'must':{'id':['com.alibaba.android.rimet:id/home_bottom_tab_button_message','com.alibaba.android.rimet:id/home_bottom_tab_button_mine']}" + "}";

        return UiApi.isMyNeedPage(mainPage);
    }

    private boolean isLoginPage()
    {
        String page = "{" + "'maxMustMills':5000," + "'maxOptionMills':5000," + "'must':{'id':['com.alibaba.android.rimet:id/et_pwd_login','com.alibaba.android.rimet:id/et_phone_input']}" + "}";
        return UiApi.isMyNeedPage(page);
    }


    private void doLogin()
    {

        UiApi.findNodeByIdAndInput(5000, "com.alibaba.android.rimet:id/et_phone_input", loginAccount);
        UiApi.findNodeByIdAndInput(5000, "com.alibaba.android.rimet:id/et_pwd_login", loginPwd);
        UiApi.clickNodeByIdWithTimeOut(5000, "com.alibaba.android.rimet:id/btn_next");

    }
}
