package com.mediatek.dingdingautopush.model.info.regDev;

import com.mediatek.dingdingautopush.model.info.base.BaseResInfo;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class RegDevResInfo extends BaseResInfo
{
    public class DataInfo
    {

        private String devToken;

        public String getDevToken()
        {
            return devToken;
        }

        public void setDevToken(String devToken)
        {
            this.devToken = devToken;
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
