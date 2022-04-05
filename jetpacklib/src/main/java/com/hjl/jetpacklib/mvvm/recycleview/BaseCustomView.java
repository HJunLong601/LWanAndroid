package com.hjl.jetpacklib.mvvm.recycleview;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;



import com.hjl.jetpacklib.mvvm.BaseViewModel;

/**
 * Description TODO
 * Author long
 * Date 2020/3/30 17:26
 */
public abstract class BaseCustomView<T extends ViewDataBinding,S extends BaseViewModel> extends LinearLayout implements ICustomView<S> {

    protected T dataBinding;
    protected S viewModel;

    public BaseCustomView(Context context) {
        super(context);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public View getRootView(){
        return dataBinding.getRoot();
    }

    public void init(){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (setViewLayoutId() != 0){
            dataBinding = DataBindingUtil.inflate(inflater,setViewLayoutId(),this,false);

            addView(dataBinding.getRoot());
        }

    }

    @Override
    public void setData(S data) {
        viewModel = data;
        setDataToView(data);
        if (dataBinding != null){
            dataBinding.executePendingBindings();
        }
        onDataUpdated();
    }

    public abstract int setViewLayoutId();

    public abstract void setDataToView(S data);

    public void onDataUpdated(){}
}
