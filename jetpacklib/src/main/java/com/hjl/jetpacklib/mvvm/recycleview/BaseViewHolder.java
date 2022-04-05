package com.hjl.jetpacklib.mvvm.recycleview;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Author long
 * Date 2020/3/30 17:08
 */
public class BaseViewHolder<VD extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private ArrayList<Integer> viewIds = new ArrayList<>(5);
    VD mBinding;

    public BaseViewHolder(@NonNull VD binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public VD getBinding() {
        return mBinding;
    }

    public <T> void setOnItemClickListener(OnItemClickListener<T> onItemClickListener, T bean){
        mBinding.getRoot().setOnClickListener(v -> onItemClickListener.onItemClick(getAbsoluteAdapterPosition(),v,bean));
    }

    public <T> void setOnItemChildClickListener(OnItemChildClickListener<T> onItemChildClickListener, T bean){
        for (int id : viewIds){
            View view = mBinding.getRoot().findViewById(id);
            if (view != null){
                view.setOnClickListener(v -> onItemChildClickListener.onItemChildClick(getAbsoluteAdapterPosition(),v,bean));
            }

        }
    }

    public <T> void setOnItemLongClickListener(OnItemLongClickListener<T> onItemClickListener, T bean){
        mBinding.getRoot().setOnLongClickListener(v -> {
            onItemClickListener.onItemLongClick(getAbsoluteAdapterPosition(),v,bean);
            return true;
        });
    }

    public <T> void setOnItemChildLongClickListener(OnItemChildLongClickListener<T> onItemChildClickListener, T bean){
        for (int id : viewIds){
            View view = mBinding.getRoot().findViewById(id);
            if (view != null){
                view.setOnLongClickListener(v -> {
                    onItemChildClickListener.onItemChildLongClick(getAbsoluteAdapterPosition(),v,bean);
                    return true;
                });
            }

        }
    }

    public void addChildClick(@IdRes int id){
        if (!viewIds.contains(id)){
            viewIds.add(id);
        }
    }

    public void addChildClick(List<Integer> ids){
        viewIds.addAll(ids);
    }


}
