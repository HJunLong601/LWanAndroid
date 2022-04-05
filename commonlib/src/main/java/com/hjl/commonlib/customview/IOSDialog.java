package com.hjl.commonlib.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hjl.commonlib.R;


import java.util.ArrayList;
import java.util.List;

public class IOSDialog extends Dialog {

    private View contentView;
    private RecyclerView optionRv;
    private TextView cancelBtn;
    private TextView titleTv;
    private List<String> options = new ArrayList();
    private OptionsAdapter adapter;

    public IOSDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentView = View.inflate(getContext(), R.layout.common_ios_dialog_layout, null);
        setContentView(contentView);
        optionRv = contentView.findViewById(R.id.ios_dialog_lv);
        cancelBtn = contentView.findViewById(R.id.ios_dialog_cancel_btn);
        titleTv = contentView.findViewById(R.id.ios_dialog_title);

        adapter = new OptionsAdapter(R.layout.common_item_base_tv,options);
        optionRv.setAdapter(adapter);
        optionRv.setLayoutManager(new LinearLayoutManager(getContext()));
        cancelBtn.setOnClickListener((v -> {
            dismiss();
        }));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.IOSDialogStyle);
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    public IOSDialog setTitleVisibility(boolean visible){
        titleTv.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public IOSDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    public IOSDialog setTitleColor(int color){
        titleTv.setTextColor(color);
        return this;
    }

    public IOSDialog addOption(String option){
        options.add(option);
        return this;
    }

    public IOSDialog setButtonText(String text){
        cancelBtn.setText(text);
        return this;
    }

    public IOSDialog setItemTextColor(int color){
        adapter.textColor = color;
        return this;
    }

    public IOSDialog setButtonTextColor(int color){
        cancelBtn.setTextColor(color);
        return this;
    }

    public IOSDialog setOnItemClickListener(OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
        return this;
    }

    @Override
    public void show() {
        adapter.notifyDataSetChanged();
        super.show();
    }

    class OptionsAdapter extends BaseQuickAdapter<String, BaseViewHolder>{

        private List<String> datalist;
        public int textColor = -1;

        public OptionsAdapter(int layoutResId, List<String> data) {
            super(layoutResId, data);
            datalist = data;
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.item_tv,item);
            if (textColor != -1){
                helper.setTextColor(R.id.item_tv,textColor);
            }
            if (datalist.size() == 1){
                if (titleTv.getVisibility() == View.VISIBLE){
                    helper.setBackgroundResource(R.id.item_layout,R.drawable.common_bg_bottom_ioswhite_8t);
                }else {
                    helper.setVisible(R.id.item_lineview,false);
                    helper.setBackgroundResource(R.id.item_layout,R.drawable.common_bg_ios_white_8t);
                }
            }else if (helper.getAdapterPosition() == 0){ //第一个 item
                if (titleTv.getVisibility() == View.VISIBLE){
                    helper.setBackgroundResource(R.id.item_layout,R.color.ios_white);
                }else {
                    helper.setBackgroundResource(R.id.item_layout,R.drawable.common_bg_top_ioswhite_8t);
                    helper.setVisible(R.id.item_lineview,false);
                }
            }else if (helper.getAdapterPosition() == datalist.size() - 1 ){ //最后一个 item
                helper.setBackgroundResource(R.id.item_layout,R.drawable.common_bg_bottom_ioswhite_8t);
            }

        }
    }

}
