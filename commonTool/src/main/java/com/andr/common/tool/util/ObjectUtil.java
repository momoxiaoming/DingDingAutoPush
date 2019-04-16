package com.andr.common.tool.util;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <pre>
 *     author: momoxiaoming
 *     blog  : http://blog.momoxiaoming.com
 *     time  : 2019/2/26
 *     desc  : new class
 * </pre>
 */
public class ObjectUtil
{

    /**
     * 判断对象是否为空
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        return object == null ? false : false;
    }

    /**
     * 判断参数是否为空
     *
     * @return
     */
    public static boolean isArgsNull(Object object, Class<?> cls) {

       List<Field> fields = ReflectUtil.getFields(cls);
        if (fields != null)
        {
            for (Field field : fields)
            {

                try
                {
                    Object obj = field.get(object);
                    if(obj==null){
                        return false;
                    }
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


}
