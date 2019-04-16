package com.mediatek.dingdingautopush.model.storge.local;

import com.andr.common.tool.file.FileUtil;
import com.andr.common.tool.json.GsonUtil;
import com.andr.common.tool.util.StringUtil;
import com.mediatek.dingdingautopush.model.info.submtTask.SubmtTaskReqInfo;
import com.mediatek.dingdingautopush.util.GlobalConfig;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class StorageManager
{
    public static void saveToken(String token)
    {
        FileUtil.getInstance().writeStrToFile(token, GlobalConfig.CONFIG_FILE_PATH, false);

    }

    public static String readToken()
    {
        return FileUtil.getInstance().readFile(GlobalConfig.CONFIG_FILE_PATH);

    }

    public static void cleanTaskData()
    {
        FileUtil.getInstance().deleteFile(GlobalConfig.TASK_DATA_FILE_PATH);
    }

    public static void setTaskData(SubmtTaskReqInfo info)
    {
        if (info != null)
        {
            FileUtil.getInstance().writeStrToFile(info.toJson(), GlobalConfig.TASK_DATA_FILE_PATH, false);

        }

    }

    public static SubmtTaskReqInfo getTaskData()
    {
        String readRlt = FileUtil.getInstance().readFile(GlobalConfig.TASK_DATA_FILE_PATH);
        if (!StringUtil.isStringEmpty(readRlt))
        {
            return GsonUtil.parseJsonWithGson(readRlt, SubmtTaskReqInfo.class);
        }

        return null;
    }
}
