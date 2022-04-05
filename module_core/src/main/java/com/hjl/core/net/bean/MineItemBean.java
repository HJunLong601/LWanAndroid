package com.hjl.core.net.bean;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

/**
 * Author : long
 * Description : MineFragment - RecyclerView Item Bean
 * Date : 2020/8/29
 */
public class MineItemBean {

    String itemName;
    String action;
    @DrawableRes
    int iconRes;

    public MineItemBean(String itemName, String action, int iconRes) {
        this.itemName = itemName;
        this.action = action;
        this.iconRes = iconRes;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
