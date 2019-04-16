package com.andr.common.tool.net.okhttp;

import java.io.File;

/**
 * Created by zhangxiaoming on 2019/1/5.
 */

public class NetCallBack
{

    public interface ReqCallBack
    {
        /**
         * 响应成功
         */
        void onReqSuccess(String result);

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }


    public interface FileStateCallBack
    {

        /**
         * 完成
         */
        void onFinish(File file);

        /**
         * 进度
         * @param fileLenth 总长度
         * @param current 当前长度
         */
        void onProgress(long fileLenth, long current);

        /**
         * 失败
         */
        void onError(String msg);
    }
}
