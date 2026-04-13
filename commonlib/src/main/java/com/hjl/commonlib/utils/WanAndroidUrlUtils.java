package com.hjl.commonlib.utils;

import android.net.Uri;
import android.text.TextUtils;

/**
 * 玩安卓域名工具。
 * 按官方公告统一优先使用主站域名，避免 www 域名在部分网络环境下解析失败。
 */
public final class WanAndroidUrlUtils {

    public static final String PRIMARY_HOST = "wanandroid.com";
    public static final String LEGACY_HOST = "www.wanandroid.com";
    public static final String PRIMARY_BASE_URL = "https://" + PRIMARY_HOST + "/";

    private WanAndroidUrlUtils() {
    }

    public static String normalizeWanAndroidUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        try {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            if (!LEGACY_HOST.equalsIgnoreCase(host)) {
                return url;
            }
            return uri.buildUpon()
                    .authority(PRIMARY_HOST)
                    .build()
                    .toString();
        } catch (Exception ignore) {
            return url;
        }
    }
}
