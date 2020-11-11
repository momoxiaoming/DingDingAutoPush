package com.mediatek.dingdingautopush.model.info.appUpdate;

import com.mediatek.dingdingautopush.model.info.base.BaseData;
import com.mediatek.dingdingautopush.model.info.base.BaseResInfo;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class AppUpdateResInfo extends BaseResInfo
{

    public class DataInfo extends BaseData
    {

        private String apkVer;

        private String apkUrl;


        public String getApkVer()
        {
            return apkVer;
        }

        public String getApkUrl()
        {
            return apkUrl;
        }

        public void setApkVer(String apkVer)
        {
            this.apkVer = apkVer;
        }

        public void setApkUrl(String apkUrl)
        {
            this.apkUrl = apkUrl;
        }
    }


    private String resData;

    public String getResData()
    {
        return resData;
    }

    public void setResData(String resData)
    {
        this.resData = resData;
    }
}
