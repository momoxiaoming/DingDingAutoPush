package com.andr.common.tool.apk;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

 class ApkInfo
{
    public String pkgName;
    public int verCode;
    public String apkName;
    public String apkVerName;

    public ApkInfo()
    {

    }

    public ApkInfo(Context context)
    {
        this.pkgName = context.getPackageName();
        this.verCode = AppUtils.getAppVersionCode(context, pkgName);
        this.apkName = AppUtils.getAppName(context);
        this.apkVerName = AppUtils.getAppVersionName(context);
    }

    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put("pkgName", pkgName);
            json.put("apkName", apkName);
            json.put("verName", apkVerName);
            json.put("verCode", verCode);
        } catch (JSONException e)
        {
            return null;
        }
        return json;
    }

    @Override
    public String toString()
    {
        return "ApkInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", verCode=" + verCode +
                ", apkName='" + apkName + '\'' +
                ", apkVerName='" + apkVerName + '\'' +
                '}';
    }
}