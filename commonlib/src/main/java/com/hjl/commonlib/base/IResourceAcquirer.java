package com.hjl.commonlib.base;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * author: long
 * description please add a description here
 * Date: 2021/12/17
 */
public interface IResourceAcquirer {

    int getColor(Context context, int resId);

    Drawable getDrawable(Context context, int resId);



}
