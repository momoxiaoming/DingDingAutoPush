package com.mediatek.dingdingautopush.model.info.regDev;

import com.andr.common.tool.json.GsonUtil;
import com.mediatek.dingdingautopush.model.info.base.BaseIntfTask;
import com.mediatek.dingdingautopush.model.manager.DataSource;
import com.mediatek.dingdingautopush.model.storge.local.StorageManager;
import com.mediatek.dingdingautopush.util.GlobalConfig;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class RegDevTask extends BaseIntfTask
{


    public RegDevTask(RegDevReqInfo reqInfo, final DataSource.DataCallBack<RegDevResInfo> callBack)
    {
        super(GlobalConfig.REQ_REGIEST_URL, reqInfo, RegDevResInfo.class, new DataSource.DataCallBack<RegDevResInfo>()
        {
            @Override
            public void onSucess(RegDevResInfo regDevResInfo)
            {
                //保存
                String resData = regDevResInfo.getResData();

                if (resData != null)
                {
                    RegDevResInfo.DataInfo dataInfo= GsonUtil.parseJsonWithGson(resData,RegDevResInfo.DataInfo.class);
                    if(dataInfo!=null){
                        String token = dataInfo.getDevToken();
                        if(StorageManager.saveToken(token)){
                            callBack.onSucess(regDevResInfo);
                        }else{
                            callBack.onFailed("设备码写入失败");

                        }
                       return;

                    }

                }
                callBack.onFailed("响应数据参数不全");
            }

            @Override
            public void onFailed(String err)
            {
                callBack.onFailed(err);

            }
        });


    }
}
