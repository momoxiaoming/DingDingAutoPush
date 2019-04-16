package com.mediatek.dingdingautopush.model.manager;

import android.app.Activity;

import com.mediatek.dingdingautopush.model.info.base.BaseResInfo;
import com.mediatek.dingdingautopush.model.info.pullTask.PullTaskResInfo;
import com.mediatek.dingdingautopush.model.info.regDev.RegDevResInfo;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskReqInfo;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskResInfo;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public interface DataSource
{


    interface DataCallBack<T extends BaseResInfo>
    {
        void onSucess(T t);

        void onFailed(String err);
    }


    /**
     * 注册token
     */
    void regToken(DataCallBack<RegDevResInfo> dataCallBack);


    /**
     * 任务拉取
     */
    void pullTask(DataCallBack<PullTaskResInfo> dataCallBack);

    /**
     * apk更新
     * @param activity
     */
    void appUpdate(final Activity activity);

    /**
     * 提交任务
     */
    void submtTask(SubmtTaskReqInfo taskReqInfo,DataCallBack<SubmtTaskResInfo> dataCallBack);

}
