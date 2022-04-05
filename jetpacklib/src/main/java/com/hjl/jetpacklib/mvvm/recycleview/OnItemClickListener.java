package com.hjl.jetpacklib.mvvm.recycleview;

import android.view.View;

/**
 * author: long
 * description please add a description here
 * Date: 2021/10/22
 */
public interface OnItemClickListener<T> {
    void onItemClick(int position, View view, T bean);
}
