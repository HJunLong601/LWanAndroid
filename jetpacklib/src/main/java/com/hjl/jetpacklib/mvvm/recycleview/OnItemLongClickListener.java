package com.hjl.jetpacklib.mvvm.recycleview;

import android.view.View;

public interface OnItemLongClickListener<T> {
    void onItemLongClick(int position, View view, T bean);
}
