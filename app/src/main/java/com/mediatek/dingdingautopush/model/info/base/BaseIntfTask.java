package com.mediatek.dingdingautopush.model.info.base;

import com.andr.common.tool.json.GsonUtil;
import com.andr.common.tool.log.LoggerUtil;
import com.andr.common.tool.net.okhttp.HttpManager;
import com.andr.common.tool.net.okhttp.NetCallBack;
import com.andr.common.tool.util.StringUtil;
import com.mediatek.dingdingautopush.model.DataCenter;
import com.mediatek.dingdingautopush.model.manager.DataSource;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class BaseIntfTask
{

    public <S extends BaseResInfo> BaseIntfTask(String url, BaseReqInfo reqInfo, final Class<S> cls, final DataSource.DataCallBack<S> callBack)
    {
        if (callBack == null)
        {
            LoggerUtil.e("callback 为空!");
            return;
        }

        if (reqInfo == null)
        {
            callBack.onFailed("请求参数为空!");
            return;
        }

        LoggerUtil.d("请求数据:"+reqInfo.toJson());
        HttpManager.getInstance(DataCenter.getInstance().getContext()).POST_ASY(url, reqInfo.toJson(), new NetCallBack.ReqCallBack()
        {
            @Override
            public void onReqSuccess(String result)
            {
                if (StringUtil.isStringEmpty(result))
                {
                    callBack.onFailed("响应数据为空!");

                } else
                {
                    S rlt = GsonUtil.parseJsonWithGson(result, cls);
                    if (rlt == null)
                    {
                        callBack.onFailed("响应数据格式错误,无法转为" + cls.getName());
                    } else if (rlt.isReady("resData"))
                    {

                        callBack.onSucess(rlt);
                    } else if (rlt.isReady("resCode", "resMsg"))
                    {
                        callBack.onFailed("" + rlt.getResMsg());

                    } else
                    {
                        callBack.onFailed("无响应数据");
                    }
                }


            }

            @Override
            public void onReqFailed(String errorMsg)
            {
                callBack.onFailed(errorMsg);
            }
        });


    }
}
