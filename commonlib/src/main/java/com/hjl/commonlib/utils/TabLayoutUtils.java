package com.hjl.commonlib.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.tabs.TabLayout;
import com.hjl.commonlib.R;
import com.hjl.commonlib.base.BaseApplication;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * <p>   </p>
 * create by zw at 2018/12/17 0017
 */
public class TabLayoutUtils {


    /**
     * 设置tab竖直线
     *
     * @param tabLayout
     */
    public static void setVerticalTabIndicator(@NotNull TabLayout tabLayout,int padding) {
        LinearLayout tab = (LinearLayout) tabLayout.getChildAt(0);
      //  tab.setBackgroundColor(context.getResources().getColor(R.color.common_white));
        tab.setDividerPadding(DensityUtil.dp2px(padding));
        tab.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        tab.setDividerDrawable(ResourcesCompat.getDrawable(
                BaseApplication.getApplication().getResources(),
                R.drawable.common_tab_vertical_divider,null
        ));
    }

    public static void setVerticalTabIndicator(@NotNull TabLayout tabLayout) {
        setVerticalTabIndicator(tabLayout,5);
    }

    /**
     * 设置tablayout指示器的长短
     * 需要在onDestroy销毁监听
     *
     * @param tab
     */
    public static ViewTreeObserver.OnGlobalLayoutListener showTabTextAdapteIndicator(final TabLayout tab) {
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Class<?> tabLayout = tab.getClass();
                Field tabStrip = null;
                try {
                    tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if (tabStrip != null) {
                    tabStrip.setAccessible(true);
                    LinearLayout ll_tab = null;
                    try {
                        ll_tab = (LinearLayout) tabStrip.get(tab);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return;
                    }
                    int maxLen = 0;
                    int maxTextSize = 0;
                    int tabCount = ll_tab.getChildCount();
                    for (int i = 0; i < tabCount; i++) {
                        View child = ll_tab.getChildAt(i);
                        child.setPadding(0, 0, 0, 0);
                        if (child instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) child;
                            for (int j = 0; j < ll_tab.getChildCount(); j++) {
                                if (viewGroup.getChildAt(j) instanceof TextView) {
                                    TextView tabTextView = (TextView) viewGroup.getChildAt(j);
                                    int length = tabTextView.getText().length();
                                    maxTextSize = (int) tabTextView.getTextSize() > maxTextSize ? (int) tabTextView.getTextSize() : maxTextSize;
                                    maxLen = length > maxLen ? length : maxLen;
                                }
                            }
                        }
                        int margin = (tab.getWidth() / tabCount - (maxTextSize + DensityUtil.dp2px(2)) * maxLen) / 2 - DensityUtil.dp2px(2);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        params.leftMargin = margin;
                        params.rightMargin = margin;
                        child.setLayoutParams(params);
                        child.invalidate();
                    }
                }
            }
        };
        tab.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        return onGlobalLayoutListener;
    }

    public static void removeTabGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener layoutListener, TabLayout tabLayout) {
        if (layoutListener != null) {
            tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
        }
    }

    public static void setupTabTextSize(TabLayout tabLayout, int normal, int selected){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                title.setTextSize(30);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView title = (TextView)(((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                title.setTextSize(10);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
