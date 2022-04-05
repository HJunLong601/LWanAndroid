package com.hjl.jetpacklib.mvvm.recycleview;

import android.view.View;

public interface OnItemChildLongClickListener<T>{
    void onItemChildLongClick(int position, View view, T bean);
}
