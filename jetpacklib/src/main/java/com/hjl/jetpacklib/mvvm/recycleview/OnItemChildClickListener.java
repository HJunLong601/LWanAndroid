package com.hjl.jetpacklib.mvvm.recycleview;

import android.view.View;

public interface OnItemChildClickListener<T> {
    void onItemChildClick(int position, View view, T bean);
}
