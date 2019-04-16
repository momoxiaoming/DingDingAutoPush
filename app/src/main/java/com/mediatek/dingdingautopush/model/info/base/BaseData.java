package com.mediatek.dingdingautopush.model.info.base;

import com.andr.common.tool.json.GsonUtil;
import com.andr.common.tool.util.ReflectUtil;
import com.andr.common.tool.util.StringUtil;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/15
 *     desc  : 所有实体类的超类
 * </pre>
 */
public class BaseData
{

    public String toJson()
    {
        return GsonUtil.toJsonStringWithGson(this);
    }


    /**
     * 检查参数是否不为空且服务要求
     *
     * @param filedNm 可变长度,变量名
     * @return
     */
    public boolean isReady(String... filedNm)
    {
        if (filedNm != null)
        {
            for (String item : filedNm)
            {
                Object obj = ReflectUtil.getFieldValue(this, this.getClass().getName(), item);
                if (obj == null)
                {
                    return false;
                } else
                {
                    if (obj instanceof String)
                    {
                        if (StringUtil.isStringEmpty((String) obj))
                            return false;
                    }

                }

            }


        }


        return true;
    }
}
