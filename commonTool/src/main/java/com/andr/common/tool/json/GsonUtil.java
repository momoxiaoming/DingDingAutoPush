package com.andr.common.tool.json;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

public final class GsonUtil
{
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type)
    {
        T rlt = null;
        try
        {
            if (!TextUtils.isEmpty(jsonData) && null != type)
            {
                Gson gson = new Gson();
                rlt = gson.fromJson(jsonData, type);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return rlt;

    }

    public static JSONObject toJsonWithGson(Object src) throws Exception
    {
        JSONObject rlt = null;

        try
        {
            if (null != src)
            {
                String JsonString = toJsonStringWithGson(src);
                rlt = new JSONObject(JsonString);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return rlt;
    }

    public static String toJsonStringWithGson(Object src)
    {
        String rlt = null;
        try
        {
            if (null != src)
            {
                Gson gson = new Gson();
                rlt = gson.toJson(src);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return rlt;
    }
}
