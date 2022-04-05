package com.hjl.lwanandroid.skin;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.hjl.commonlib.base.IResourceAcquirer;

import skin.support.content.res.SkinCompatResources;

/**
 * author: long
 * description please add a description here
 * Date: 2021/12/17
 */
public class SkinResourceAcquirer implements IResourceAcquirer {
    @Override
    public int getColor(Context context, int resId) {
        return SkinCompatResources.getColor(context, resId);
    }

    @Override
    public Drawable getDrawable(Context context, int resId) {
        return SkinCompatResources.getDrawable(context, resId);
    }
}
