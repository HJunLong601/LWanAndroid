package com.hjl.commonlib.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.jakewharton.rxbinding3.widget.RxTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AndroidUtils {

    // -------------------------------ApplicationInfo-------------------------------
    public static String getAppName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? 0 : packageInfo.versionCode;
    }

    /**
     * 获取apk版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo == null ? "0" : packageInfo.versionName;
    }

    private static int version;

    public static int getApkVersion(Context context) {
        if (version == 0) {
            String versionName = getVersionName(context);
            try {
                version = Integer.parseInt(versionName.replace(".", "").trim());
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return getVersionCode(context);
            }
        }
        return version;
    }

    private static String mPackageName;

    public static String getPackageName(Context context) {
        if (TextUtils.isEmpty(mPackageName)) {
            PackageInfo packageInfo = getPackageInfo(context);
            mPackageName = packageInfo == null ? "" : packageInfo.packageName;
        }
        return mPackageName;
    }

    public static Drawable getAppIcon(Context context) {
        return context.getApplicationInfo().loadIcon(context.getPackageManager());
    }

    /**
     * 判断是否允许debug
     */
    public static boolean isApkDebugable(Context context) {
        boolean debugable = false;
        try {
            ApplicationInfo info = context.getApplicationInfo();
            debugable = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return debugable;
    }

    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return "";
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return "";
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getMetaInApplication(Context context, String key) {
        String value = "";
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (info != null) {
            Bundle bundle = info.metaData;
            try {
                value = bundle.getString(key);
            } catch (Exception ignored) {
            }
            try {
                if (TextUtils.isEmpty(value)) {
                    value = String.valueOf(bundle.getInt(key));
                }
            } catch (Exception ignored) {
            }
        }

        return value;
    }

    public static int getTargetSdkVersion(Context context) {
        return context.getApplicationInfo().targetSdkVersion;
    }

    // ----------------------------telephony_information----------------------------
    public static String getImei(Context context) {
        TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            imei = localTelephonyManager.getDeviceId();
        }
        if (TextUtils.isEmpty(imei)) {
            imei = getMacAddress(context);
        }
        return imei;
    }

    public static String getImsi(Context context) {
        TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String str = null;
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            str = localTelephonyManager.getSubscriberId();
        }
        return str;
    }

    public static String getIccid(Context context) {
        TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String str = null;
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            str = localTelephonyManager.getSimSerialNumber();
        }
        return str;
    }

    /**
     * 获取手机网络运营商信息，若为wifi网络，就是wifi网络运营商和sim运营商不一定一致
     */
    public static String getNetWorkCarrier(Context context) {
        TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String str = null;
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            str = localTelephonyManager.getNetworkOperator();
        }
        return str;
    }

    public static String getNetWorkCarrierName(Context context) {
        TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String str = null;
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            str = localTelephonyManager.getNetworkOperatorName();
        }
        return str;
    }

    /**
     * 获取手机卡运营商信息，若短信扣费使用该接口
     */
    public static String getCarrier(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return tm.getSimOperator();
        }
        return null;
    }

    /**
     * 获取设备SD卡路径
     * <p>
     * 一般是/storage/emulated/0/
     * </p>
     *
     * @return SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getFreeBytes(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0l;
        }
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        try {
            StatFs stat = new StatFs(filePath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                long availableBlocks = (long) stat.getAvailableBlocksLong();
                return stat.getBlockSizeLong() * availableBlocks;
            } else {
                long availableBlocks = (long) stat.getAvailableBlocks();
                return stat.getBlockSize() * availableBlocks;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 获取设备总内存大小
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalMemory(Context context) {
        long total = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            MemoryInfo memInfo = new MemoryInfo();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            am.getMemoryInfo(memInfo);
            if (memInfo != null) {
                total = memInfo.totalMem / (1024 * 1024);
            }
        } else {
            String str1 = "/proc/meminfo";// 系统内存信息文件
            String str2;
            String[] arrayOfString;
            long initial_memory = 0;
            try {
                FileReader localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
                arrayOfString = str2.split("\\s+");
                for (String num : arrayOfString) {
                    LogUtils.i(str2, num + "\t");
                }
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue();// 获得系统总内存，单位是KB，乘以1024转换为Byte
                localBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            total = initial_memory / 1024;
        }
        return total;
    }

    /**
     * 获取当前可用内存大小
     *
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / (1024 * 1024);
    }

    public static String getCarrierName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperatorName = null;
        // 优先getSimOperator获得运营商信息
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            simOperatorName = tm.getSimOperatorName();
        }
        return simOperatorName;
    }

    public static Map<String, Object> getGSMCellLocationInfo(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = manager.getNetworkOperator();
        if (!TextUtils.isEmpty(operator) && !"null".equals(operator) && operator.length() > 3) {
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                map.put("mcc", operator.substring(0, 3));
                map.put("mnc", operator.substring(3));
                GsmCellLocation location = (GsmCellLocation) manager.getCellLocation();
                map.put("lac", location.getLac());
                map.put("cId", location.getCid());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                CdmaCellLocation location1 = (CdmaCellLocation) manager.getCellLocation();
                map.put("nId", location1.getNetworkId());
                map.put("bId", location1.getBaseStationId());
                map.put("sId", location1.getSystemId());
            } catch (Exception e) {
//                e.printStackTrace();
            }
            return map;
        }
        return null;
    }

    // ----------------------------network_connections----------------------------
    public static String getNetworkTypeName(Context context) {
        if (isWifiConnected(context)) {
            return "WIFI";
        }
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null ? info.getTypeName() : null;
    }

    public static String getNetworkSubtypeName(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null ? info.getSubtypeName() : null;
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context.getApplicationContext());
        return info != null ? info.isConnected() : false;
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static String getMacAddress(Context context) {
        String macAddress = "";
        WifiInfo info = getWifiInfo(context);
        if (info != null) {
            macAddress = info.getMacAddress();
            if (!TextUtils.isEmpty(macAddress) && macAddress.equals("02:00:00:00:00:00")) {
                macAddress = getMacAddr();
            }
        }
        return macAddress;
    }

    /**
     * android6.0以上获取mac错误的兼容方法
     *
     * @return
     */
    private static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "02:00:00:00:00:00";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString().toLowerCase();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    @SuppressWarnings("deprecation")
    public static String getIpAddress(Context context) {
        WifiInfo info = getWifiInfo(context);
        return info != null ? Formatter.formatIpAddress(info.getIpAddress()) : null;
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm != null ? wm.getConnectionInfo() : null;
    }

    // ---------------------------other_system_service----------------------------
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    public static void setPrimaryClip(final Context context, final String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        runOnMainThread(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    if (cm != null) {
                        cm.setPrimaryClip(ClipData.newPlainText("", text));
                    }
                } else {
                    android.text.ClipboardManager cm = (android.text.ClipboardManager) context
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    if (cm != null) {
                        cm.setText(text);
                    }
                }
            }
        });
    }

    public static void vibrate(Context context, long milliseconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(milliseconds);
        }
    }

    public static boolean checkPermission(Context context, String permName) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName,
                context.getPackageName()));
        return permission;
    }

    public static void installApk(Context context, File file) {
        if (file != null && file.exists()) {
            installApk(context, file.getAbsolutePath());
        }
    }

    public static int targetSdkVersion(Context context) {
        return context.getApplicationInfo().targetSdkVersion;
    }

    public static void installApk(Context context, String apkPath) {
        if (TextUtils.isEmpty(apkPath)) {
            return;
        }
        Uri path = Uri.parse(apkPath);
        // If there is no scheme, then it must be a file
        // executable = true;
        if (path.getScheme() == null) {
            path = Uri.fromFile(new File(apkPath));
        }
        Intent launchIntent = new Intent(Intent.ACTION_VIEW);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchIntent.setDataAndType(path, "application/vnd.android.package-archive");
        context.startActivity(launchIntent);
    }

    public static void deleteApk(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    public static boolean isApkInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isApkInstalled(Context context, String packageName, int versionCode) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infos = pm.getInstalledPackages(0);
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).packageName.equals(packageName) && infos.get(i).versionCode == versionCode) {
                return true;
            }
        }
        return false;
    }

    public static void launchApk(Context context, String packageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void launchApkWithoutIcon(Context context, String packageName, String defaultActivity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, defaultActivity));
        intent.setAction("android.intent.action.MAIN");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void gotoBrowser(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void gotoDial(Context context, String tel) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public static Location getLastLocation(Context context){
        // 获取位置服务
        String serviceName = Context.LOCATION_SERVICE;
        // 调用getSystemService()方法来获取LocationManager对象
        LocationManager locationManager = (LocationManager) context.getSystemService(serviceName);
        // 指定LocationManager的定位方法
        String provider = LocationManager.GPS_PROVIDER;
        // 调用getLastKnownLocation()方法获取当前的位置信息
        return locationManager.getLastKnownLocation(provider);
    }


    public static void gotoMMS(Context context, String address) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("address", address);
            intent.setType("vnd.android-dir/mms-sms");
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void gotoImageCapture(Activity activity, File file, int requestCode) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("camerasensortype", 1);
            intent.putExtra("autofocus", true);
            intent.putExtra("fullScreen", false);
            intent.putExtra("showActionIcons", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void gotoImageCapture(Activity activity, Uri uri, int requestCode) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("camerasensortype", 1);
            intent.putExtra("autofocus", true);
            intent.putExtra("fullScreen", false);
            intent.putExtra("showActionIcons", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void gotoImageCrop(Activity activity, Uri data, int ratio, int width, int requestCode) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(data, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", ratio);
            intent.putExtra("aspectY", ratio);
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", width);
            intent.putExtra("return-data", true);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void gotoImagePick(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public static void gotoQQ(Activity activity) {
        String[] packageNames = new String[]{"com.tencent.mobileqq", "com.tencent.minihd.qq", "com.tencent.hd.qq"};
        for (int i = 0; i < packageNames.length; i++) {
            Intent launchQQIntent = activity.getPackageManager().getLaunchIntentForPackage(packageNames[i]);
            if (launchQQIntent != null) {
                activity.startActivity(launchQQIntent);
                break;
            }
        }
    }

    public static void runOnMainThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            // Run this function on the main thread.
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public static AlertDialog showCustomDialog(Context context, int layoutResID, int cancelResID) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setContentView(layoutResID);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (cancelResID != 0) {
            dialog.findViewById(cancelResID).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        return dialog;
    }

    public static void showAlertDialog(Context context, int titleId, int messageId, final DialogInterface.OnClickListener positiveBtn, DialogInterface.OnClickListener nativeBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messageId);
        builder.setTitle(titleId);
        builder.setPositiveButton("确认", positiveBtn);
        builder.setNegativeButton("取消", nativeBtn);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 设置屏幕保持高亮
     *
     * @param context
     */
    public static void keepBright(final Context context) {
        try {
            runOnMainThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消屏幕高亮,恢复默认状态
     *
     * @param context
     */
    public static void cleanBright(final Context context) {
        try {
            runOnMainThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static String getDisplayMetrics(Context mContext) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        return metrics.widthPixels + "*" + metrics.heightPixels;
    }

    static class ContactInfo {
        String name;
        String number;
    }

    /**
     * 获取通讯录信息
     *
     * @param context
     * @param param
     * @return
     * @throws JSONException
     */
    public static String getContactInfo(Context context, String param) {
        int moreinfo = 1;// 默认值为1表示没有下一页联系人
        List<ContactInfo> infos = new ArrayList<ContactInfo>();// 通讯录数组
        JSONObject jsonObject = new JSONObject();// 传给游戏的通讯录数据
        try {
            JSONObject gameobjson = new JSONObject(param);// 游戏传过来的json数据
            JSONArray jsonArray = new JSONArray();// 通讯录数组
            JSONObject extrajson = new JSONObject();// 额外信息
            int page = gameobjson.getInt("page");
            int amount = gameobjson.getInt("amount");
            if (checkPermission(context, "android.permission.READ_CONTACTS")) {
                if (page > 0) {
                    infos = getPhoneContacts(context, page, amount);
                    moreinfo = moreinfo(context, page, amount);
                }
            } else {
                extrajson.put("msg", "No permission");
            }

            for (ContactInfo contactInfo : infos) {
                JSONObject infoobjson = new JSONObject();// 单个联系人
                infoobjson.put("name", contactInfo.name);
                infoobjson.put("number", contactInfo.number);
                jsonArray.put(infoobjson);
            }
            jsonObject.put("page", page);
            jsonObject.put("amount", infos.size());
            jsonObject.put("contactinfo", jsonArray);
            jsonObject.put("more", moreinfo);

            if (page <= 0) {
                extrajson.put("msg", "page can not be 0 or smaller");
            }
            if (infos.size() <= 0) {
                extrajson.put("msg", "no data");
            }
            jsonObject.put("extra", extrajson);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * @param mContext
     * @param page
     * @param amount
     * @return moreinfo 1：下一页没有联系人 2：表示下一页还有联系人
     */
    public static int moreinfo(Context mContext, int page, int amount) {
        int moreinfo = 1;
        ContentResolver resolver = mContext.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID}, null, null,
                "phonebook_label limit " + 1 + " offset " + amount * page);
        // limit语句对有的手机没用（eg:oppo手机）
        if (phoneCursor.getCount() > amount) {
            phoneCursor.moveToPosition(amount * page - 1);
        }
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                moreinfo = 2;
            }
            phoneCursor.close();
        }
        return moreinfo;
    }

    /**
     * 手机通讯录分页联系人信息
     *
     * @param mContext
     * @param page
     * @param amount
     * @return
     */
    private static List<ContactInfo> getPhoneContacts(Context mContext, int page, int amount) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        if (page <= 0)
            return infos;
        ContentResolver resolver = mContext.getContentResolver();
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1};
        Cursor phoneCursor = resolver.query(uri, projection, null, null,
                "phonebook_label limit " + amount + " offset " + amount * (page - 1));// 获取手机联系人按姓氏排名
        // limit语句对有的手机没用（eg:oppo手机）
        int i = 0;
        if (phoneCursor.getCount() > amount) {
            phoneCursor.moveToPosition(amount * (page - 1) - 1);
        }
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext() && i < amount) {
                i++;
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(1).replace(" ", "");// 手机号去空
                // 得到联系人名称
                String contactName = phoneCursor.getString(0);
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.name = contactName;
                contactInfo.number = phoneNumber;
                infos.add(contactInfo);
            }
            phoneCursor.close();
        }
        return infos;
    }

    /**
     * 校验手机号
     *
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone) {
        /**
         * 10 *中国移动：ChinaMobile 11
         * *134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188 12
         */
        String CM = "^((\\+86)|(86))?1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$";
        /**
         * 15 *中国联通：ChinaUnicom 16 *130,131,132,152,155,156,185,186 17
         */
        String CU = "^((\\+86)|(86))?1(3[0-2]|5[256]|8[56])\\d{8}$";
        /**
         * 20 *中国电信：ChinaTelecom 21 *133,1349,153,180,189 22
         */
        String CT = "^((\\+86)|(86))?1((33|53|8[09])[0-9]|349)\\d{7}$";
        /**
         * 25 *大陆地区固话及小灵通 26 *区号：010,020,021,022,023,024,025,027,028,029 27
         * *号码：七位或八位 28
         */
        String PHS = "^0(10|2[0-5789]|\\d{3})\\d{7,8}$";
        Pattern pcm = Pattern.compile(CM);
        Pattern pcu = Pattern.compile(CU);
        Pattern pct = Pattern.compile(CT);
        Pattern phs = Pattern.compile(PHS);
        Matcher mcm = pcm.matcher(phone);
        Matcher mcu = pcu.matcher(phone);
        Matcher mct = pct.matcher(phone);
        Matcher mhs = phs.matcher(phone);

        return mcm.matches() || mcu.matches() || mct.matches() || mhs.matches();

    }

    /**
     * 隐藏软键盘
     *
     * @param v
     */
    public static void hideSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float dip) {
        final float scale = getDensity(context);
        return (int) (dip * scale + 0.5f);
    }

    public static float px2dip(Context context, float px) {
        final float scale = getDensity(context);
        return (px / scale + 0.5f);
    }

    public static boolean wecharInstalled(Context context) {
        return AndroidUtils.isApkInstalled(context, "com.tencent.mm");
    }

    /**
     * 获取IP地址
     *
     * @param useIPv4 是否用IPv4
     * @return IP地址
     */
    public static String getIPAddress(final boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取屏幕形状，是否有圆角或小刘海(vivo设备特有接口)
     *
     * @return 整型
     * 0:正常屏幕
     * 1:圆角屏幕
     * 2:小刘海屏幕
     * 3:有圆角并且带有小刘海
     */
    public static int getScreenShape(Context context) {
        // vivo 相关
        String VIVO_BRAND_TAG = "vivo";
        int VIVO_IS_ROUNDED_CORNERS = 0x00000020;
        int VIVO_IS_HAD_GROOVE = 0x00000008;

        // oppo 相关
        String OPPO_BRAND_TAG = "oppo";
        String OPPO_SCREEN_FEATURE_PACKAGE = "com.oppo.feature.screen.heteromorphism";

        //天珑 相关
        String SUGAR_BRAND_TAG = "sugar";
        String SUGAR_CLASS_NAME = "android.os.SystemProperties";
        String SUGAR_METHOD_NAME = "getBoolean";
        String SUGAR_API_ATTRIBUTE = "ro.tcf.support.tscreen";

        // 小米 相关
        String XIAOMI_BRAND_TAG = "xiaomi";
        String XIAOMI_CLASS_NAME = "ro.miui.notch.SystemProperties";
        String XIAOMI_METHOD_NAME = "getInt";
        String XIAOMI_API_ATTRIBUTE = "ro.miui.notch";

        // is?1:0
        int roundedCornersScreen = 0;
        // is?2:0
        int grooveScreen = 0;

        String device = Build.MANUFACTURER;

        if (device.toLowerCase().contains(VIVO_BRAND_TAG)) {
            try {
                Class<?> class1 = Class.forName("android.util.FtFeature");
                Object object = class1.newInstance();
                Method feature = class1.getMethod("isFeatureSupport", int.class);
                if ((boolean) feature.invoke(object, VIVO_IS_ROUNDED_CORNERS)) {
                    roundedCornersScreen = 1;
                }
                if ((boolean) feature.invoke(object, VIVO_IS_HAD_GROOVE)) {
                    grooveScreen = 2;
                }
            } catch (Exception ignore) {
//                ignore.printStackTrace();
                return roundedCornersScreen | grooveScreen;
            }
        } else if (device.toLowerCase().contains(OPPO_BRAND_TAG)) {
            boolean oppoScreenFeature = context.getApplicationContext().getPackageManager().
                    hasSystemFeature(OPPO_SCREEN_FEATURE_PACKAGE);
            if (oppoScreenFeature) {
                grooveScreen = 2;
            }
        } else if (device.toLowerCase().contains(SUGAR_BRAND_TAG)) {
            try {
                Class<?> class1 = Class.forName(SUGAR_CLASS_NAME);
                Class<?>[] parameterTypes = {String.class, boolean.class};
                Object[] args = {SUGAR_API_ATTRIBUTE, false};
                Method api = class1.getMethod(SUGAR_METHOD_NAME, parameterTypes);
                if ((boolean) api.invoke(null, args)) {
                    // 天珑的设备不区分圆角屏，默认是刘海屏就都是圆角屏
                    roundedCornersScreen = 1;
                    grooveScreen = 2;
                }
            } catch (Exception ignore) {
//                e.printStackTrace();
                return roundedCornersScreen | grooveScreen;
            }
        } else if (device.toLowerCase().contains(XIAOMI_BRAND_TAG)) {
            try {
                Class<?> class1 = Class.forName(XIAOMI_CLASS_NAME);
                Class<?>[] parameterTypes = {String.class, int.class};
                Object[] args = {XIAOMI_API_ATTRIBUTE, 0};
                Method api = class1.getMethod(XIAOMI_METHOD_NAME, parameterTypes);
                Object object = class1.newInstance();
                if (1 == (int) api.invoke(object, args)) {
                    // 小米的设备不区分圆角屏，默认是刘海屏就都是圆角屏
                    roundedCornersScreen = 1;
                    grooveScreen = 2;
                }

            } catch (Exception ignore) {
//                e.printStackTrace();
                return roundedCornersScreen | grooveScreen;
            }
        }

        return roundedCornersScreen | grooveScreen;
    }


    /**
     * 获取屏幕形状，是否有圆角或小刘海
     *
     * @return 整型
     * 0X000000:正常屏幕
     * 0X000001:圆角屏幕
     * 0X000010:刘海屏幕
     * 0X000100:挖空屏幕
     * 0X001000:折叠屏幕
     */
    public static int getScreenShapeNew(Context context) {
        // vivo 相关
        final String VIVO_BRAND_TAG = "vivo";
        int VIVO_IS_ROUNDED_CORNERS = 0x00000020;
        int VIVO_IS_HAD_NOTCH = 0x00000008;

        // oppo 相关
        final String OPPO_BRAND_TAG = "oppo";
        String OPPO_SCREEN_FEATURE_PACKAGE = "com.oppo.feature.screen.heteromorphism";

        //天珑 相关
        final String SUGAR_BRAND_TAG = "sugar";
        String SUGAR_CLASS_NAME = "android.os.SystemProperties";
        String SUGAR_METHOD_NAME = "getBoolean";
        String SUGAR_API_ATTRIBUTE = "ro.tcf.support.tscreen";

        // 小米 相关
        final String XIAOMI_BRAND_TAG = "xiaomi";
        String XIAOMI_CLASS_NAME = "ro.miui.notch.SystemProperties";
        String XIAOMI_METHOD_NAME = "getInt";
        String XIAOMI_API_ATTRIBUTE = "ro.miui.notch";

        // 华为
        final String HUAWEI_BRAND_TAG = "huawei";

        // 普通屏幕
        int screen = 0X000000;
        // 圆角屏
        final int roundedCornersScreen = 0X000001;
        // 刘海屏
        final int notchScreen = 0X000010;
        // 挖孔屏
        final int diggingScreen = 0X000100;
        // 折叠屏
        final int foldableScreen = 0X001000;

        String deviceBrand = Build.MANUFACTURER;

        switch (deviceBrand.toLowerCase()) {
            case VIVO_BRAND_TAG:
                try {
                    Class<?> class1 = Class.forName("android.util.FtFeature");
                    Object object = class1.newInstance();
                    Method feature = class1.getMethod("isFeatureSupport", int.class);
                    if ((boolean) feature.invoke(object, VIVO_IS_ROUNDED_CORNERS)) {
                        screen |= roundedCornersScreen;
                    }
                    if ((boolean) feature.invoke(object, VIVO_IS_HAD_NOTCH)) {
                        screen |= notchScreen;
                    }
                } catch (Exception ignore) {
                }
                break;
            case OPPO_BRAND_TAG:
                boolean oppoScreenFeature = context.getApplicationContext().getPackageManager().
                        hasSystemFeature(OPPO_SCREEN_FEATURE_PACKAGE);
                if (oppoScreenFeature) {
                    screen |= notchScreen;
                }
                break;
            case SUGAR_BRAND_TAG:
                try {
                    Class<?> class1 = Class.forName(SUGAR_CLASS_NAME);
                    Class<?>[] parameterTypes = {String.class, boolean.class};
                    Object[] args = {SUGAR_API_ATTRIBUTE, false};
                    Method api = class1.getMethod(SUGAR_METHOD_NAME, parameterTypes);
                    if ((boolean) api.invoke(null, args)) {
                        // 天珑的设备不区分圆角屏，默认是刘海屏就都是圆角屏
                        screen |= roundedCornersScreen;
                        screen |= notchScreen;
                    }
                } catch (Exception ignore) {
                }
                break;
            case XIAOMI_BRAND_TAG:
                try {
                    Class<?> class1 = Class.forName(XIAOMI_CLASS_NAME);
                    Class<?>[] parameterTypes = {String.class, int.class};
                    Object[] args = {XIAOMI_API_ATTRIBUTE, 0};
                    Method api = class1.getMethod(XIAOMI_METHOD_NAME, parameterTypes);
                    Object object = class1.newInstance();
                    if (1 == (int) api.invoke(object, args)) {
                        // 小米的设备不区分圆角屏，默认是刘海屏就都是圆角屏
                        screen |= roundedCornersScreen;
                        screen |= notchScreen;
                    }
                } catch (Exception ignore) {
                }
                break;
            case HUAWEI_BRAND_TAG:
                String deviceModel = Build.MODEL.toLowerCase();
                if (deviceModel.contains("vce-al00") || deviceModel.contains("vce-tl00") || deviceModel.contains("vce-l22") ||
                        deviceModel.contains("pct-al10") || deviceModel.contains("pct-tl10") || deviceModel.contains("pct-l29")) {
                    screen |= roundedCornersScreen;
                    screen |= diggingScreen;
                } else if (("RLI-AN00".equalsIgnoreCase(Build.MODEL) || "RLI-N29".equalsIgnoreCase(Build.MODEL)
                        || "TAH-AN00".equalsIgnoreCase(Build.MODEL) || "TAH-N29".equalsIgnoreCase(Build.MODEL))) {
                    screen |= foldableScreen;
                } else {
                    try {
                        ClassLoader classLoader = context.getClassLoader();
                        Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
                        Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
                        if ((boolean) get.invoke(HwNotchSizeUtil)) {
                            screen |= notchScreen;
                        }
                    } catch (Exception ignore) {
                    }
                }
                break;
            default:
                break;
        }

        if (context instanceof Activity) {
            try {
                Rect rect = getInvalidInsetArea(context);
                if (rect.width() == 0 || rect.height() == 0) {
                    return screen;
                }
                // 存在不能显示区域
                int[] screenArea = getScreenWH((Activity) context);
                // 区域在正中间则是刘海 在两边则是挖空屏
                if (rect.left < screenArea[1]/2 && rect.left + rect.width() > screenArea[1] / 2) {
                    screen = notchScreen;
                } else {
                    screen = diggingScreen;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return screen;
    }

    /**
     * 获取屏幕中不可使用的区域
     * 返回一个json string
     *
     * @return json string
     * left
     * top
     * right
     * bottom
     */
    public static Rect getInvalidInsetArea(Context context) {

        Rect invalidRect = null;

        try {
            if (Build.VERSION.SDK_INT >= 28 && context instanceof Activity && ((Activity) context).getWindow().getDecorView().getRootWindowInsets() != null) {
                WindowInsets windowInsets = ((Activity) context).getWindow().getDecorView().getRootWindowInsets();
                Method method1 = windowInsets.getClass().getDeclaredMethod("getDisplayCutout");
                if (method1 != null) {
                    Object displayCutout = method1.invoke(windowInsets);
                    if (displayCutout != null) {
                        Method boundingRects = displayCutout.getClass().getDeclaredMethod("getBoundingRects");
                        List rectList = (List) boundingRects.invoke(displayCutout);
                        if(rectList.size()>0){
                            // 目前只关注顶部
                            invalidRect = (Rect) rectList.get(0);
                        }
                    }
                }
            }
        } catch (Exception ignore) {
        }

        return invalidRect == null?new Rect(0,0,0,0):invalidRect;
    }


    /**
     * 设备的屏幕大小(默认屏幕高度比宽度大)
     *
     * @param activity
     * @return
     */
    public static int[] getScreenWH(Activity activity) {
        int realWidth, realHeight;
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 17) {
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(dm);
            realWidth = dm.widthPixels;  // 屏幕宽
            realHeight = dm.heightPixels;  // 屏幕高
        } else {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c;
            try {
                c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            realWidth = dm.widthPixels;
            realHeight = dm.heightPixels;
        }
        if (realWidth > realHeight) {
            return new int[]{realWidth, realHeight};
        } else {
            return new int[]{realHeight, realWidth};
        }
    }

    public static int getStatusBarSize(Context context){
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }




}
