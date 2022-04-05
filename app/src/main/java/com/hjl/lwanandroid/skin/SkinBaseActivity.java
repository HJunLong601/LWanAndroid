package com.hjl.lwanandroid.skin;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;
import skin.support.SkinCompatManager;
import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

/**
 * author: long
 * description please add a description here
 * Date: 2021/12/17
 */
public class SkinBaseActivity extends AppCompatActivity implements SkinObserver {

    @NonNull
    @NotNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SkinCompatManager.getInstance().addObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinCompatManager.getInstance().deleteObserver(this);
    }

    private static List<String> baseClsList = new ArrayList<>();

    static {
        baseClsList.add("com.hjl.jetpacklib.mvvm.view.BaseActivity");
        baseClsList.add("com.hjl.commonlib.base.BaseMultipleActivity");
    }

    @Override
    public void updateSkin(SkinObservable observable, Object o) {

        for (String baseCls : baseClsList) {
            try {
                Class aClass = Class.forName(baseCls);
                Method setStatusBar = aClass.getDeclaredMethod("setStatusBar");
                setStatusBar.setAccessible(true);
                setStatusBar.invoke(this);
            } catch (Exception ignore) {
            }
        }

    }
}
