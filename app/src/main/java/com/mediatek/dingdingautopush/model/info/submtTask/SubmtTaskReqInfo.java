package com.mediatek.dingdingautopush.model.info.submtTask;

import com.mediatek.dingdingautopush.model.info.base.BaseReqInfo;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : new class
 * </pre>
 */
public class SubmtTaskReqInfo extends BaseReqInfo
{
    private String taskId;

    private String taskState;

    private String taskDesc;


    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskState()
    {
        return taskState;
    }

    public void setTaskState(String taskState)
    {
        this.taskState = taskState;
    }

    public String getTaskDesc()
    {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc)
    {
        this.taskDesc = taskDesc;
    }
}
