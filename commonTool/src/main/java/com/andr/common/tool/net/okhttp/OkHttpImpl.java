package com.andr.common.tool.net.okhttp;

import android.os.Handler;

import com.andr.common.tool.net.okhttp.NetCallBack.FileStateCallBack;
import com.andr.common.tool.net.okhttp.NetCallBack.ReqCallBack;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static com.andr.common.tool.net.okhttp.HttpManager.MEDIA_TYPE_JSON;
import static com.andr.common.tool.net.okhttp.HttpManager.MEDIA_TYPE_STREAM;
import static java.lang.String.valueOf;


/**
 * http请求管理器
 * Created by zhangxiaoming on 2019/1/3.
 */

public class OkHttpImpl
{

    // 这个需要和服务端保持一致
    private static final String TAG = OkHttpImpl.class.getSimpleName();
    private static volatile OkHttpImpl mInstance;//单利引用
    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler okHttpHandler;//全局处理子线程和M主线程通信

    private HashMap<String, String> mHeaderMap = null;

    /**
     * 请求的类型
     */
    enum ReqType
    {
        TYPE_GET, TYPE_POST_JSON,
    }


    /**
     * 初始化RequestManager
     */


    public void initData(OkHttpClient client, Handler handler)
    {
        this.mOkHttpClient = client;
        this.okHttpHandler = handler;

    }

    /**
     * 设置头信息
     *
     * @param map
     */
    public void setHeaders(HashMap<String, String> map)
    {

        this.mHeaderMap = map;
    }


    /**
     * 获取单例引用
     *
     * @return
     */
    public static OkHttpImpl getInstance()
    {
        OkHttpImpl inst = mInstance;
        if (inst == null)
        {
            synchronized (OkHttpImpl.class)
            {
                inst = mInstance;
                if (inst == null)
                {
                    inst = new OkHttpImpl();
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders()
    {
        Request.Builder builder = new Request.Builder();
        if (mHeaderMap != null)
        {
            for (String key : mHeaderMap.keySet())
            {
                builder.addHeader(key, mHeaderMap.get(key));
            }
        }

        return builder;
    }


    /**
     * okHttp同步请求统一入口
     *
     * @param actionUrl  接口地址
     * @param reqType    请求类型
     * @param jsonParams 请求参数 json 字符串
     */
    public String requestSyn(String actionUrl, ReqType reqType, String jsonParams)
    {
        String res = null;
        switch (reqType)
        {
            case TYPE_GET:
                res = requestGetBySyn(actionUrl, new Gson().fromJson(jsonParams, HashMap.class));
                break;
            case TYPE_POST_JSON:
                res = requestPostBySyn(actionUrl, jsonParams);
                break;

        }
        return res;
    }

    /**
     * okHttp异步请求统一入口
     *
     * @param actionUrl 接口地址
     * @param reqType   请求类型
     * @param reqParams 请求参数
     * @param callBack  请求返回数据回调
     **/
    public void requestAsyn(String actionUrl, ReqType reqType, String reqParams, NetCallBack.ReqCallBack callBack)
    {
        switch (reqType)
        {
            case TYPE_GET:
                requestGetByAsyn(actionUrl, new Gson().fromJson(reqParams, HashMap.class), callBack);
                break;
            case TYPE_POST_JSON:
                requestPostByAsyn(actionUrl, reqParams, callBack);
                break;

        }
    }

    /**
     * 文件下载
     *
     * @param url               下载路径
     * @param filePath          保存的路径
     * @param fileName          保存的文件名
     * @param FileStateCallBack 回调
     */
    public void downFileForAsy(final String url, final String filePath, final String fileName, final NetCallBack.FileStateCallBack FileStateCallBack)
    {
        try
        {
            Request request = addHeaders().url(url).build();
            Call call = mOkHttpClient.newCall(request);

            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    // 下载失败
                    errorCallBack("接口访问失败", FileStateCallBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {

                    if (!response.isSuccessful())
                    {
                        errorCallBack("请求失败", FileStateCallBack);
                        return;
                    }

                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录
                    File file = null;

                    try
                    {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        file = new File(filePath, fileName);
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1)
                        {
                            fos.write(buf, 0, len);
                            sum += len;
                            // 下载中
                            progressCallBack(total, sum, FileStateCallBack);
                        }
                        fos.flush();
                        // 下载完成
                        finshCallBack(file, FileStateCallBack);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        errorCallBack("文件下载异常", FileStateCallBack);
                        if (file != null)
                        {
                            file.delete();
                        }
                    } finally
                    {
                        try
                        {
                            if (is != null)
                                is.close();
                        } catch (IOException e)
                        {
                        }
                        try
                        {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e)
                        {
                        }
                    }


                }

            });
        } catch (Exception e)
        {
            e.printStackTrace();
            errorCallBack("接口请求出错", FileStateCallBack);

        }


    }

    /**
     * 带进度的文件上传
     *
     * @param file
     * @param uplodUrl
     * @param callBack
     */
    public void upLoadFile(String params, final File file, String uplodUrl, final NetCallBack.FileStateCallBack callBack)
    {
        if (file == null)
        {

            callBack.onError("上传的文件为空!");
            return;
        }

        try
        {
            HashMap map = null;
            if (params != null && !"".equals(params))
            {
                map = new Gson().fromJson(params, HashMap.class);
            }

            RequestBody fileBody = createProgressRequestBody(map, MEDIA_TYPE_STREAM, file, callBack);
            final Request request = addHeaders().url(uplodUrl).post(fileBody).build();

            //创建一个Call
            Call call = mOkHttpClient.newCall(request);

            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    errorCallBack("上传失败", callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    if (response.isSuccessful())
                    {
                        finshCallBack(file, callBack);
                    } else
                    {
                        errorCallBack("响应错误", callBack);

                    }
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();

            errorCallBack("接口访问失败", callBack);

        }
    }




    /*---------------------------请求相关逻辑--------------------------------------*/

    /**
     * okHttp get同步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    private String requestGetBySyn(String actionUrl, HashMap<String, String> paramsMap)
    {
        StringBuilder tempParams = new StringBuilder();
        try
        {
            //处理参数
            int pos = 0;
            for (String key : paramsMap.keySet())
            {
                if (pos > 0)
                {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s?%s", actionUrl, tempParams.toString());
            //创建一个请求
            Request request = addHeaders().url(requestUrl).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            final Response response = call.execute();
            if (response.isSuccessful())
            {
                return response.body().string();

            }

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    /**
     * okHttp post同步请求
     *
     * @param actionUrl 接口地址
     * @param reqParams 请求参数
     */
    private String requestPostBySyn(String actionUrl, String reqParams)
    {
        try
        {
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, reqParams);
            //创建一个请求
            final Request request = addHeaders().url(actionUrl).post(body).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful())
            {
                //获取返回数据 可以是String，bytes ,byteStream
                return response.body().string();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * okHttp post异步请求
     *
     * @param actionUrl 接口地址
     * @param params    请求参数
     * @param callBack  请求返回数据回调
     * @return
     */
    private void requestPostByAsyn(String actionUrl, String params, final ReqCallBack callBack)
    {
        try
        {

            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            final Request request = addHeaders().url(actionUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    failedCallBack("访问失败" + e.getMessage(), callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    if (response.isSuccessful())
                    {
                        resSucHandle(response, callBack);
                    } else
                    {
                        failedCallBack("响应错误,状态码"+response.code(), callBack);
                    }
                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
            failedCallBack("接口请求异常", callBack);

        }

    }


    //--------异步处理代码-----


    /**
     * okHttp get异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @return
     */
    private void requestGetByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack callBack)
    {
        StringBuilder tempParams = new StringBuilder();
        try
        {
            int pos = 0;
            for (String key : paramsMap.keySet())
            {
                if (pos > 0)
                {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = String.format("%s?%s", actionUrl, tempParams.toString());
            final Request request = addHeaders().url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    failedCallBack("访问失败", callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    if (response.isSuccessful())
                    {
                        resSucHandle(response, callBack);
                    } else
                    {
                        failedCallBack("响应错误", callBack);
                    }
                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
            failedCallBack("接口访问失败", callBack);

        }

    }

    private void resSucHandle(Response response, ReqCallBack callBack) throws IOException
    {
        String result = null;
        ResponseBody responseBody = response.body();
        if (responseBody != null)
        {
            result = responseBody.string();
        }
        successCallBack(result, callBack);
    }


    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @param <T>
     * @return
     */
    public <T> RequestBody createProgressRequestBody(HashMap<String, String> map, final MediaType contentType, final File file, final FileStateCallBack callBack)
    {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (map != null)
        {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet())
            {
                builder.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }

        // MediaType.parse() 里面是上传的文件类型。
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        builder.addFormDataPart("fileName", file.getName(), new RequestBody()
        {
            @Override
            public MediaType contentType()
            {
                return contentType;
            }

            @Override
            public long contentLength()
            {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink)
            {
                Source source;
                try
                {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; )
                    {
                        sink.write(buf, readCount);
                        current += readCount;
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        return builder.build();
    }


    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     */
    private void successCallBack(final String result, final ReqCallBack callBack)
    {
        okHttpHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callBack != null)
                {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     */
    private void failedCallBack(final String errorMsg, final NetCallBack.ReqCallBack callBack)
    {
        okHttpHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callBack != null)
                {
                    callBack.onReqFailed(errorMsg==null?"":errorMsg);
                }
            }
        });
    }


    private void finshCallBack(final File file, final NetCallBack.FileStateCallBack FileStateCallBack)
    {
        // 下载失败
        okHttpHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (FileStateCallBack != null)
                {
                    FileStateCallBack.onFinish(file);
                }
            }
        });
    }

    private void progressCallBack(final long fileLenth, final long current, final NetCallBack.FileStateCallBack FileStateCallBack)
    {
        // 下载失败
        okHttpHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (FileStateCallBack != null)
                {
                    FileStateCallBack.onProgress(fileLenth, current);
                }
            }
        });
    }


    private void errorCallBack(final String msg, final NetCallBack.FileStateCallBack FileStateCallBack)
    {
        // 下载失败
        okHttpHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (FileStateCallBack != null)
                {
                    FileStateCallBack.onError(msg==null?"":msg);
                }
            }
        });
    }
}
