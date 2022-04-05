package com.hjl.commonlib.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hjl.commonlib.R;
import com.hjl.commonlib.base.ResourceManager;
import com.hjl.commonlib.utils.DensityUtil;


public class BaseMarkDialog extends Dialog {

    private View contentView;
    private ClearEditText clearEditText;
    private TextView confirmTv;
    private TextView cancelTv;
    private TextView titleTv;
    private TextView contentTv;
    private MarkDialogEnum dialogType;

    public enum MarkDialogEnum{
        DIALOG_MARK, // 标题 + 输入
        DIALOG_WITH_CONTENT, // 标题 + 内容 + 输入
        DIALOG_SMALL_MARK    // 标题 + 小输入框
    }

    public BaseMarkDialog( Context context,MarkDialogEnum dialogType) {
        super(context, R.style.BaseDialogStyle);

        contentView = View.inflate(context,R.layout.common_dialog_mark_content,null);
        setContentView(contentView);

        clearEditText = contentView.findViewById(R.id.common_dialog_mark); // 默认限制输入30
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
        if (dialogType == MarkDialogEnum.DIALOG_MARK){
            lp.width = DensityUtil.dp2px(270);
            // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            // lp.width  = 400;
            lp.height = DensityUtil.dp2px(184);
            contentTv.setVisibility(View.GONE);
        }else if (dialogType == MarkDialogEnum.DIALOG_WITH_CONTENT){
            lp.width = DensityUtil.dp2px(270);
            lp.height = DensityUtil.dp2px(204);
        }else if (dialogType == MarkDialogEnum.DIALOG_SMALL_MARK) {
            lp.width = DensityUtil.dp2px(270);
            lp.height = DensityUtil.dp2px(148);
            contentTv.setVisibility(View.GONE);
            titleTv.setGravity(Gravity.CENTER);
            titleTv.setTextColor(ResourceManager.getInstance().getColor(getContext(),R.color.common_text_title_gray));
            ViewGroup.LayoutParams params = clearEditText.getLayoutParams();
            params.height = DensityUtil.dp2px(36);
            clearEditText.setLayoutParams(params);
        }

        window.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public interface OnConfirmLister{
        void onClick(String data);
    }

    public BaseMarkDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    public BaseMarkDialog setContent(String content){
        contentTv.setText(content);
        return this;
    }

    public BaseMarkDialog setHint(String hint){
        clearEditText.setHint(hint);
        return this;
    }

    public BaseMarkDialog setEditText(String text){
        clearEditText.setText(text);
        return this;
    }

    public BaseMarkDialog setConfirmText(String text){
        confirmTv.setText(text);
        return this;
    }

    public BaseMarkDialog setCancelText(String text){
        cancelTv.setText(text);
        return this;
    }

    public BaseMarkDialog setMaxEms(int ems){
        clearEditText.setMaxEms(ems);
        return this;
    }

    public BaseMarkDialog setEditTextFilter(InputFilter[] filter){
        clearEditText.setFilters(filter);
        return this;
    }

    public String getEditText(){
        return clearEditText.getText().toString();
    }

    public BaseMarkDialog setOnConfirmListener(OnConfirmLister listener){
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(getEditText());
            }
        });
        return this;
    }

    public BaseMarkDialog setOnCancelClickListener(View.OnClickListener listener){
        cancelTv.setOnClickListener(listener);
        return this;
    }




}
