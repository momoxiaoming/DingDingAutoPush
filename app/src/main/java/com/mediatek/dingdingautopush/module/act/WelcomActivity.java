package com.mediatek.dingdingautopush.module.act;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.andr.common.tool.file.FileUtil;
import com.andr.common.tool.log.LoggerUtil;
import com.andr.view.activity.BasePermissionActivity;
import com.mediatek.dingdingautopush.R;
import com.mediatek.dingdingautopush.util.GlobalConfig;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : new class
 * </pre>
 */
public class WelcomActivity extends BasePermissionActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weclome_activity);
        String[] permission = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermission(permission, 0x0001);

    }

    @Override
    public void permissionSuccess(int requestCode)
    {
        if (requestCode == 0x0001)
        {
            initData();

            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    startActivity(new Intent(WelcomActivity.this, MainActivity.class));
                    finish();
                }
            }, 2000);
        }
    }


    @Override
    public void permissionFail(int requestCode)
    {

    }

    private void initData()
    {

        FileUtil.getInstance().makeDirs(GlobalConfig.MAIN_PATH);
        FileUtil.getInstance().makeDirs(GlobalConfig.CONFIG_PATH);
        FileUtil.getInstance().makeDirs(GlobalConfig.DOWN_FILE_PATH);

        FileUtil.getInstance().deleteDirContent(GlobalConfig.DOWN_FILE_PATH);
        FileUtil.getInstance().deleteDirContent(GlobalConfig.LOG_FILE_PATH);

        LoggerUtil.initLogger("dingdingAssist",false,true);


    }
}
