package com.hjl.commonlib.base;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

/**
 * author: long
 * description please add a description here
 * Date: 2021/12/17
 */
public class ResourceManager implements IResourceAcquirer {


    public static ResourceManager getInstance() {
        return ResourceManager.ResourceManagerHolder.sInstance;
    }

    private static class ResourceManagerHolder {
        private static final ResourceManager sInstance = new ResourceManager();
    }

    public void setiResourceAcquirer(IResourceAcquirer iResourceAcquirer) {
        this.iResourceAcquirer = iResourceAcquirer;
    }

    private IResourceAcquirer iResourceAcquirer = new DefaultResourceImpl();


    @Override
    public int getColor(Context context, int resId) {
        return iResourceAcquirer.getColor(context, resId);
    }

    @Override
    public Drawable getDrawable(Context context, int resId) {
        return iResourceAcquirer.getDrawable(context, resId);
    }

    class DefaultResourceImpl implements IResourceAcquirer {
        @Override
        public int getColor(Context context, int resId) {
            return context.getResources().getColor(resId);
        }

        @Override
        public Drawable getDrawable(Context context, int resId) {
            return ResourcesCompat.getDrawable(context.getResources(),resId,null);
        }
    }

}
