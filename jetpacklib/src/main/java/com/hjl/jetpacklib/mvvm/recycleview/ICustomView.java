package com.hjl.jetpacklib.mvvm.recycleview;

import com.hjl.jetpacklib.mvvm.BaseViewModel;

/**
 * Description TODO
 * Author long
 * Date 2020/3/30 17:09
 */
public interface ICustomView<S extends BaseViewModel> {

    void setData(S data);

}
