package com.mediatek.dingdingautopush.model.info.base;

import com.andr.common.tool.apk.ApkUtil;
import com.andr.common.tool.phone.PhoneUtil;
import com.mediatek.dingdingautopush.model.DataCenter;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class DevInfo
{

    private String dev_andId;
    private String dev_imei;
    private String dev_isRt;
    private String dev_name;
    private String dev_sdk;
    private String app_ver;


    public String getDev_andId()
    {
        return dev_andId;
    }

    public void setDev_andId(String dev_andId)
    {
        this.dev_andId = dev_andId;
    }

    public String getDev_imei()
    {
        return dev_imei;
    }

    public void setDev_imei(String dev_imei)
    {
        this.dev_imei = dev_imei;
    }

    public String getDev_isRt()
    {
        return dev_isRt;
    }

    public void setDev_isRt(String dev_isRt)
    {
        this.dev_isRt = dev_isRt;
    }

    public String getDev_name()
    {
        return dev_name;
    }

    public void setDev_name(String dev_name)
    {
        this.dev_name = dev_name;
    }

    public String getDev_sdk()
    {
        return dev_sdk;
    }

    public void setDev_sdk(String dev_sdk)
    {
        this.dev_sdk = dev_sdk;
    }

    public String getApp_ver()
    {
        return app_ver;
    }

    public void setApp_ver(String app_ver)
    {
        this.app_ver = app_ver;
    }

    public DevInfo()
    {
        this.dev_andId= PhoneUtil.getInstance().getAndId(DataCenter.getInstance().getContext());
        this.dev_imei= PhoneUtil.getInstance().getImei(DataCenter.getInstance().getContext());
        this.dev_isRt= PhoneUtil.getInstance().isRoot() ?"0":"1";
        this.dev_name= PhoneUtil.getInstance().getMode();
        this.dev_sdk= PhoneUtil.getInstance().getSysSdkVer()+"";
        this.app_ver= ApkUtil.getInstance().getAppVersionName(DataCenter.getInstance().getContext());

    }
}
