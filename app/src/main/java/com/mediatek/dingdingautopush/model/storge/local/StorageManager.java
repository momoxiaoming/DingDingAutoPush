package com.mediatek.dingdingautopush.model.storge.local;

import com.andr.common.tool.file.FileUtils;
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
        FileUtils.writeStrToFile(token, GlobalConfig.CONFIG_FILE_PATH, false);

    }

    public static String readToken()
    {
        return FileUtils.readFile(GlobalConfig.CONFIG_FILE_PATH);

    }

    public static void cleanTaskData()
    {
        FileUtils.deleteFile(GlobalConfig.TASK_DATA_FILE_PATH);
    }

    public static void setTaskData(SubmtTaskReqInfo info)
    {
        if (info != null)
        {
            FileUtils.writeStrToFile(info.toJson(), GlobalConfig.TASK_DATA_FILE_PATH, false);

        }

    }

    public static SubmtTaskReqInfo getTaskData()
    {
        String readRlt = FileUtils.readFile(GlobalConfig.TASK_DATA_FILE_PATH);
        if (!StringUtil.isStringEmpty(readRlt))
        {
            return GsonUtil.parseJsonWithGson(readRlt, SubmtTaskReqInfo.class);
        }

        return null;
    }
}
