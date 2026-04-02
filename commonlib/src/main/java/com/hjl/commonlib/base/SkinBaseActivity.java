package com.hjl.commonlib.base;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import skin.support.SkinCompatManager;
import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

/**
 * Shared skin-aware activity base that can be consumed by library modules.
 */
public class SkinBaseActivity extends AppCompatActivity implements SkinObserver {

    private static final List<String> BASE_CLASS_LIST = new ArrayList<>();

    static {
        BASE_CLASS_LIST.add("com.hjl.jetpacklib.mvvm.view.BaseActivity");
        BASE_CLASS_LIST.add("com.hjl.commonlib.base.BaseMultipleActivity");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context baseContext = ActivityDelegateRegistry.attachBaseContext(newBase);
        super.attachBaseContext(baseContext != null ? baseContext : newBase);
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityDelegateRegistry.onRequestPermissionsResult(this);
    }

    @Override
    public void updateSkin(SkinObservable observable, Object o) {
        for (String baseCls : BASE_CLASS_LIST) {
            try {
                Class<?> aClass = Class.forName(baseCls);
                Method setStatusBar = aClass.getDeclaredMethod("setStatusBar");
                setStatusBar.setAccessible(true);
                setStatusBar.invoke(this);
            } catch (Exception ignore) {
            }
        }
    }
}
