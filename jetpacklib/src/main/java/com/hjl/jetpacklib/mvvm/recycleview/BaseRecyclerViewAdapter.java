package com.hjl.jetpacklib.mvvm.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hjl.commonlib.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: long
 * description please add a description here
 * Date: 2020/8/24
 */
public abstract class BaseRecyclerViewAdapter<T,DB extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<DB>> {

    private List<T> dataList = new ArrayList<>();
    private List<Integer> childClickIds = new ArrayList<>(5);
    private List<Integer> childLongClickIds = new ArrayList<>(5);

    protected OnItemClickListener<T> onItemClickListener;
    protected OnItemChildClickListener<T> onItemChildClickListener;
    protected OnItemLongClickListener<T> onItemLongClickListener;
    protected OnItemChildLongClickListener<T> onItemChildLongClickListener;
    protected Context mContext;

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public BaseViewHolder<DB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mContext == null) mContext = parent.getContext();

        DB binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                getLayoutId(),
                parent,
                false
        );
        return new BaseViewHolder<>(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<DB> holder, int position) {
        bindData(holder.mBinding,dataList.get(position),position);
        holder.addChildClick(childClickIds);
        if (onItemClickListener != null){
            holder.setOnItemClickListener(onItemClickListener,dataList.get(position));
        }
        if (onItemChildClickListener != null){
            holder.setOnItemChildClickListener(onItemChildClickListener,dataList.get(position));
        }
        if (onItemChildLongClickListener != null){
            holder.setOnItemChildLongClickListener(onItemChildLongClickListener,dataList.get(position));
        }
        if (onItemLongClickListener != null){
            holder.setOnItemLongClickListener(onItemLongClickListener,dataList.get(position));
        }
    }

    protected void addChildClick(@IdRes int id){
        if (!childClickIds.contains(id)) childClickIds.add(id);
    }

    protected void addChildLongClick(@IdRes int id){
        if (!childLongClickIds.contains(id)) childLongClickIds.add(id);
    }

    public void bindData(DB binding,T data,int position){
        bindData(binding,data);
    }

    public abstract void bindData(DB binding,T data);

    public abstract int getLayoutId();

    public void setNewData(List<T> data){
        BaseApplication.runOnUIThread(() -> {
            dataList.clear();
            dataList.addAll(data);
            notifyDataSetChanged();
        });
    }

    public List<T> getDataList() {
        return dataList;
    }


    /**
     * java 使用到接口的地方 这个接口还是用java来写比较好 否则kotlin代码无法转换闭包会很麻烦
     * kotlin 使用可以使用闭包
     * @param onItemClickListener
     */

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener<T> onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener<T> onItemChildLongClickListener) {
        this.onItemChildLongClickListener = onItemChildLongClickListener;
    }
}
