package com.hjl.commonlib.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hjl.commonlib.R;
import com.hjl.commonlib.utils.DensityUtil;



public class BaseTipDialog extends Dialog {

    private View contentView;
    private TextView confirmTv;
    private TextView cancelTv;
    private TextView titleTv;
    private TextView contentTv;
    private TipDialogEnum dialogType;

    public enum TipDialogEnum{
        DIALOG_TIP, // 只有一行提示
        DIALOG_WITH_CONTENT  // 标题 + 内容
    }

    public BaseTipDialog( Context context,TipDialogEnum dialogType) {
        super(context, R.style.BaseDialogStyle);

        contentView = View.inflate(context, R.layout.common_dialog_tip,null);
        setContentView(contentView);

        confirmTv = contentView.findViewById(R.id.common_dialog_confirm);
        cancelTv = contentView.findViewById(R.id.common_dialog_cancel);
        titleTv = contentView.findViewById(R.id.common_dialog_title);
        contentTv = contentView.findViewById(R.id.common_dialog_content);

        cancelTv.setOnClickListener(v -> dismiss());
        this.dialogType = dialogType;
    }

    private void initView(){
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = DensityUtil.dp2px(270);
        lp.height = DensityUtil.dp2px(120);
        if (dialogType == TipDialogEnum.DIALOG_TIP){
            contentTv.setVisibility(View.GONE);
            titleTv.setTextSize(14);
        }else if (dialogType == TipDialogEnum.DIALOG_WITH_CONTENT){

        }

        window.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public BaseTipDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    public BaseTipDialog setContent(String content){
        contentTv.setText(content);
        return this;
    }

    public BaseTipDialog setConfirmText(String text){
        confirmTv.setText(text);
        return this;
    }

    public BaseTipDialog setCancelText(String text){
        cancelTv.setText(text);
        return this;
    }

    public BaseTipDialog setOnCancelClickListener(View.OnClickListener listener){
        cancelTv.setOnClickListener(listener);
        return this;
    }

    public BaseTipDialog setOnConfirmClickListener(View.OnClickListener listener){
        confirmTv.setOnClickListener(listener);
        return this;
    }

    public TextView getTitleTv (){
        return titleTv;
    }

    public TextView getContentTv(){
        return contentTv;
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        cancelTv.setEnabled(flag);
    }
}
