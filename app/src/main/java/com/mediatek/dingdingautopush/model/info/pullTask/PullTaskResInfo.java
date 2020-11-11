package com.mediatek.dingdingautopush.model.info.pullTask;

import com.mediatek.dingdingautopush.model.info.base.BaseData;
import com.mediatek.dingdingautopush.model.info.base.BaseResInfo;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class PullTaskResInfo extends BaseResInfo
{




    public class DataInfo extends BaseData
    {
        private String taskId;
        private LoginDataInfo taskData;
        private String taskType;
        private String company; //公司名称.新增字段

        public String getTaskId()
        {
            return taskId;
        }

        public void setTaskId(String taskId)
        {
            this.taskId = taskId;
        }

        public LoginDataInfo getTaskData()
        {
            return taskData;
        }

        public void setTaskData(LoginDataInfo taskData)
        {
            this.taskData = taskData;
        }

        public String getTaskType()
        {
            return taskType;
        }

        public void setTaskType(String taskType)
        {
            this.taskType = taskType;
        }

        public String getCompany()
        {
            return company;
        }

        public void setCompany(String company)
        {
            this.company = company;
        }
    }

    private String resData;

    public String getResData()
    {
        return resData;
    }

    public void setResData(String resData)
    {
        this.resData = resData;
    }
}
