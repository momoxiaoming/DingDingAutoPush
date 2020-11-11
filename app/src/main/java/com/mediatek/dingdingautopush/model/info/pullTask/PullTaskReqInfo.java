package com.mediatek.dingdingautopush.model.info.pullTask;

import com.mediatek.dingdingautopush.model.info.base.BaseReqInfo;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class PullTaskReqInfo extends BaseReqInfo
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
