package com.mediatek.dingdingautopush.model.info.submtTask;

import com.mediatek.dingdingautopush.model.info.base.BaseIntfTask;
import com.mediatek.dingdingautopush.model.manager.DataSource;
import com.mediatek.dingdingautopush.util.GlobalConfig;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class SubmtTask extends BaseIntfTask
{
    public  SubmtTask( SubmtTaskReqInfo reqInfo,  DataSource.DataCallBack<SubmtTaskResInfo> callBack)
    {
        super(GlobalConfig.REQ_SUBMIT_URL, reqInfo, SubmtTaskResInfo.class, callBack);
    }

//    public void doTask(SubmtTaskReqInfo reqInfo, DataSource.DataCallBack<SubmtTaskResInfo> callBack)
//    {
//        String resRlt = HttpManager.getInstance(DataCenter.getInstance().getContext()).POST_SYN(GlobalConfig.REQ_SUBMIT_URL, reqInfo.toJson());
//
//        if (callBack == null)
//        {
//            LoggerUtil.e("callback 为空!");
//            return;
//        }
//        if (StringUtil.isStringEmpty(resRlt))
//        {
//            callBack.onFailed("响应为空!");
//        } else
//        {
//            SubmtTaskResInfo resInfo= GsonUtil.parseJsonWithGson(resRlt,SubmtTaskResInfo.class);
//
//            if(resInfo==null){
//
//                callBack.onFailed("数据格式为空!");
//
//            }else {
//
//                callBack.onSucess(resInfo);
//            }
//        }
//    }
}
