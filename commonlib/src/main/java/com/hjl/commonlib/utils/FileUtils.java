package com.hjl.commonlib.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.hjl.commonlib.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileUtils {

    /**
     * SD卡 根目录  /storage/emulated/0
     */
    public final static String SD_ROOT_PATH = getSDRootPath();

    /**
     * SD卡 根目录 存储的目录
     */
    public final static String SD_ROOT_CACHE_PATH = SD_ROOT_PATH + "/MyPlayer";

    /**
     * SD卡 根目录 图片存储的目录
     */
    public final static String SD_ROOT_IMAGE = SD_ROOT_CACHE_PATH + "/image";

    /**
     * SD卡 根目录 视频存储的目录
     */
    public final static String SD_ROOT_VIDEO = SD_ROOT_CACHE_PATH + "/video";

    /**
     * 保存文件目录  /storage/emulated/0/Android/data/com.xxx/files
     */

    public final static String SD_FILES_PATH = getSDFilesPath();

    /**
     * 保存文件目录 /storage/emulated/0/Android/data/com.xxx/files/image
     */

    public final static String SD_FILES_IMAGE = getSDFilesPath() + File.separator + "image";

    /**
     * 缓存存储 根目录 /storage/emulated/0/Android/data/com.xxx/cache  若无外部存储 则为 /data/data/0/com.example.filetest/cache
     */
    public final static String SD_CACHE_PATH = getSDCachePath();


    /**
     * 缓存存储 图片存储目录
     */
    public final static String SD_CACHE_IMAGE = SD_CACHE_PATH + "/image";

    /**
     * 缓存存储 视频存储目录
     */
    public final static String SD_CACHE_VIDEO = SD_CACHE_PATH + "/video";


    private static String TAG = "FileUtils";

    /**
     * 内部存储 data/data/com.xxx/cache
     * @return
     */

    public static String getInternalStorageCachePath(){
        File ISCacheFile = BaseApplication.getApplication().getCacheDir();
        if (ISCacheFile != null){
            return ISCacheFile.getAbsolutePath();
        }else {
            return "";
        }
    }

    /**
     * SD卡 /storage/emulated/0/Android/data/com.xxx/cache
     * @return
     */

    public static String getSDCachePath(){

        File sdFile = null ;
        if (Environment.isExternalStorageEmulated()){
            sdFile = BaseApplication.getApplication().getExternalCacheDir();
        }
        if (sdFile != null){
            return sdFile.getAbsolutePath();
        }else {
            return getInternalStorageCachePath();
        }
    }

    /**
     * SD卡 /storage/emulated/0/Android/data/com.xxx/files
     * @return
     */

    public static String getSDFilesPath(){

        File sdFile = null ;
        if (Environment.isExternalStorageEmulated()){
            sdFile = BaseApplication.getApplication().getExternalFilesDir("");
        }
        if (sdFile != null){
            return sdFile.getAbsolutePath();
        }else {
            return getInternalStorageCachePath();
        }
    }

    /**
     * SD卡根目录
     * @return
     */
    public static String getSDRootPath(){

        File sdFile = null ;
        if (Environment.isExternalStorageEmulated()){
            sdFile = Environment.getExternalStorageDirectory();
        }
        if (sdFile != null){
            return sdFile.getAbsolutePath();
        }else {
            return "";
        }
    }

    /**
     * 保存图片到文件
     */

    public static void saveDrawableInFile(Drawable drawable,File file) throws FileNotFoundException {

        Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        savaBitmapInFile(bitmap,file);


    }

    public static void savaBitmapInFile(Bitmap bitmap,File file) throws FileNotFoundException {

        if (ContextCompat.checkSelfPermission(
                BaseApplication.getApplication().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ){
            Log.e(TAG,"没有外部存储权限");
        }


        try {
            if (!file.exists()){
                File parentFile = file.getParentFile();
                if (!parentFile.exists()){
                    if (!parentFile.mkdirs() && !parentFile.exists()) {
                        throw new Exception("文件夹创建失败！");
                    }
                }

                if (!file.createNewFile()){
                    throw new Exception("文件创建失败！");
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show("文件保存失败：" + e.getMessage());
        }
    }

    /***
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {//简单地拦截一下
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,BaseApplication.getApplication().getPackageName(), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static boolean deleteFile(String filePath,Context context){

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Boolean result = file.delete();
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        return result;
    }

}
