package com.mediatek.dingdingautopush.module.act;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andr.common.tool.apk.AppUtils;
import com.andr.common.tool.util.StringUtil;
import com.andr.view.dialog.LoadingDialogManager;
import com.mediatek.dingdingautopush.R;
import com.mediatek.dingdingautopush.model.DataCenter;
import com.mediatek.dingdingautopush.model.info.regDev.RegDevResInfo;
import com.mediatek.dingdingautopush.model.manager.DataManager;
import com.mediatek.dingdingautopush.model.manager.DataSource;
import com.mediatek.dingdingautopush.model.storge.local.StorageManager;
import com.mediatek.dingdingautopush.module.service.MainAccessService;
import com.mediatek.dingdingautopush.module.service.TaskService;
import com.mediatek.dingdingautopush.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{


    @BindView(R.id.version_text)
    TextView versionText;
    @BindView(R.id.token_text)
    TextView tokenText;
    @BindView(R.id.run_text)
    TextView runText;
    @BindView(R.id.root_text)
    TextView rootText;
    @BindView(R.id.regiest_btn)
    Button regiestBtn;
    @BindView(R.id.start_btn)
    Button startBtn;
    @BindView(R.id.stop_btn)
    Button stopBtn;
    @BindView(R.id.update_btn)
    Button updateBtn;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sp = this.getSharedPreferences("frimName.db", Activity.MODE_PRIVATE);

        initView();
        updateApk();

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        updateData();
    }

    private void updateData()
    {
        if (!StringUtil.isStringEmpty(StorageManager.readToken()))
        {
            tokenText.setText(StorageManager.readToken());
            regiestBtn.setText("重新注册");
        }
        versionText.setText("版本: " + AppUtils.getAppVersionName(this));

        runText.setText(DataCenter.getInstance().isRun() ? "运行中" : "未运行");
    }

    private void initView()
    {


        regiestBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            moveTaskToBack(false);

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
    //    public boolean onKeyDown(int keyCode, KeyEvent event) {
    //        if(keyCode == KeyEvent.KEYCODE_BACK){
    //            Intent i = new Intent(Intent.ACTION_MAIN);
    //            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //            i.addCategory(Intent.CATEGORY_HOME);
    //            startActivity(i);
    //            return true;
    //        }
    //        return super.onKeyDown(keyCode, event);
    //    }


    @Override
    public void onClick(View view)
    {

        if (view == regiestBtn)
        {
            devReg();
        } else if (view == stopBtn)
        {
            //            stopWork();
            stopWork();
        } else if (view == updateBtn)
        {
            updateApk();


        } else if (view == startBtn)
        {
            startWork();
        }
    }

    private void doDingdingPage()
    {
        String url = "dingtalk://dingtalkclient/action/switchtab?index=2&corpid=ding0e200a493d49234135c2f4657eb6378f&appid=158";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void updateApk()
    {
        DataManager.getInstance().appUpdate(this);
    }

    private void stopWork()
    {
        stopService(new Intent(this, TaskService.class));
        Toast.makeText(this,"已停止",Toast.LENGTH_SHORT).show();
    }

    private void startWork()
    {
        if (ToolUtil.isSettingOpen(MainAccessService.class, this))
        {
            startService(new Intent(this, TaskService.class));
            moveTaskToBack(false);
            //            onBackPressed();
        } else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("请开启辅助功能权限");
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    ToolUtil.jumpToSetting(MainActivity.this);
                }
            });
            builder.create().show();
        }

    }

    private void devReg()
    {
        final LoadingDialogManager dialogManager = new LoadingDialogManager(this);
        dialogManager.show();
        DataManager.getInstance().regToken(new DataSource.DataCallBack<RegDevResInfo>()
        {
            @Override
            public void onSucess(RegDevResInfo regDevResInfo)
            {
                dialogManager.dismiss();
                Toast.makeText(MainActivity.this, regDevResInfo.getResMsg()+":"+StorageManager.readToken(), Toast.LENGTH_SHORT).show();
                updateData();
            }

            @Override
            public void onFailed(String err)
            {
                dialogManager.dismiss();
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
