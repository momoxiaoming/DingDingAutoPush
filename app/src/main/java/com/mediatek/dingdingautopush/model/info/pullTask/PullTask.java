package com.mediatek.dingdingautopush.model.info.pullTask;

import com.andr.common.tool.json.GsonUtil;
import com.andr.common.tool.log.LoggerUtil;
import com.andr.common.tool.net.okhttp.HttpManager;
import com.andr.common.tool.util.StringUtil;
import com.mediatek.dingdingautopush.model.DataCenter;
import com.mediatek.dingdingautopush.model.manager.DataSource;
import com.mediatek.dingdingautopush.util.GlobalConfig;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class PullTask
{

    public void doTask(PullTaskReqInfo reqInfo, DataSource.DataCallBack<PullTaskResInfo> callBack)
    {
        LoggerUtil.d("任务请求数据:"+reqInfo.toJson());
        String resRlt = HttpManager.getInstance(DataCenter.getInstance().getContext()).POST_SYN(GlobalConfig.REQ_TASK_MAIN_URL, reqInfo.toJson());

        if (callBack == null)
        {
            LoggerUtil.e("callback 为空!");
            return;
        }
        if (StringUtil.isStringEmpty(resRlt))
        {
            callBack.onFailed("响应为空!");
        } else
        {
            PullTaskResInfo resInfo= GsonUtil.parseJsonWithGson(resRlt,PullTaskResInfo.class);

            if(resInfo==null){

                callBack.onFailed("数据格式为空!");

            }else {
                LoggerUtil.d("任务响应数据:"+resInfo.toJson());
                callBack.onSucess(resInfo);
            }
        }
    }

}
