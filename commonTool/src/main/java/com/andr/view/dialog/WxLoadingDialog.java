package com.andr.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.andr.common.tool.R;

/**
 * <pre>
 *     author: momoxiaoming
 *     blog  : http://blog.momoxiaoming.com
 *     time  : 2019/2/25
 *     desc  : new class
 * </pre>
 */
public class WxLoadingDialog extends Dialog
{

    private String content="加载中...";


    public WxLoadingDialog(@NonNull Context context) {
        super(context, R.style.wxLoadingDialog);
        initView();
    }


    public void initView() {
        setContentView(R.layout.wx_dialog_content);
        TextView  msgText=findViewById(R.id.tvcontent);
        msgText.setText(content);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha=0.8f;
        getWindow().setAttributes(attributes);
        setCancelable(false);


    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(WxLoadingDialog.this.isShowing())
                    WxLoadingDialog.this.dismiss();
                break;
        }
        return true;
    }


}
