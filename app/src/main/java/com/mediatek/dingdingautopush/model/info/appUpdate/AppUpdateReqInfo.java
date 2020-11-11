package com.mediatek.dingdingautopush.model.info.appUpdate;

import com.mediatek.dingdingautopush.model.info.base.BaseReqInfo;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class AppUpdateReqInfo extends BaseReqInfo
{
    private String dev_token;
    private String app_ver;




    public String getDev_token()
    {
        return dev_token;
    }

    public void setDev_token(String dev_token)
    {
        this.dev_token = dev_token;
    }

    public String getApp_ver()
    {
        return app_ver;
    }

    public void setApp_ver(String app_ver)
    {
        this.app_ver = app_ver;
    }
}
