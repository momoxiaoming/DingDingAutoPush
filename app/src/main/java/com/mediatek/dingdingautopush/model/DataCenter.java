package com.mediatek.dingdingautopush.model;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class DataCenter
{
    private Context context;

    @SuppressLint("StaticFieldLeak")
    private static DataCenter mIntence;

    private boolean isRun;



    public static DataCenter getInstance()
    {
        if (null == mIntence)
        {
            synchronized (DataCenter.class)
            {
                if (null == mIntence)
                {
                    mIntence = new DataCenter();
                }
            }
        }
        return mIntence;
    }


    public Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }


    public boolean isRun()
    {
        return isRun;
    }

    public void setRun(boolean run)
    {
        isRun = run;
    }
}
