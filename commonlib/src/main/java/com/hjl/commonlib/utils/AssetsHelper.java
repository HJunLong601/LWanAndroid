package com.hjl.commonlib.utils;

import static com.hjl.commonlib.utils.FileIOUtils.getFileMD5;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hjl.commonlib.base.BaseApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Description: 拷贝asset 目录下的资源文件，支持拷贝压缩包，拷贝完成后删除压缩文件
 * Author: junlong.huang
 * CreateTime: 2022/11/7
 */
public class AssetsHelper {

    public static final String TAG = "AssetsHelper";
    private static final int BUFFER_SIZE = 4096;

    private static String dirPath = "";
    private static String cacheDirPath = "";


    private static ConcurrentHashMap<String, String> fileMD5Map = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> assetsFileMD5Map = new ConcurrentHashMap<>(); // 读取assets下的map

    private static volatile boolean hasInitMap = false;


    private synchronized static void initMD5Map() {

        if (hasInitMap) return;

        Context context = BaseApplication.getApplication();
        if (context == null) {
            Log.e(TAG, "context is null,please init sdk first");
            return;
        }

        hasInitMap = true;

        File file = new File(getResourceDir(context), "res_md5_map"); // 当前资源的映射表
        File cacheFile = new File(getCacheDir(context), "res_md5_map"); // assets目录下的原始映射表

        FileIOUtils.createOrExistsFile(file);

        // 读取asset目录下的原始映射表，供比较
        InputStream inputStream = openAssets(context, "res_md5_map");
        copyDestFileWithRetry(inputStream, cacheFile);

        BufferedReader fileMapReader = null;
        BufferedReader assetsFileMapReader = null;
        try {
            fileMapReader = new BufferedReader(new FileReader(file));
            assetsFileMapReader = new BufferedReader(new FileReader(cacheFile));

            // 加载最新的映射表
            String line = "";
            while ((line = fileMapReader.readLine()) != null) {
                String[] split = line.split("=");
                if (split.length != 2) {
                    Log.e(TAG, "read error md5 map :" + line);
                    continue;
                }
                fileMD5Map.put(split[0], split[1]);
            }

            // 加载原始映射表
            String l = "";
            while ((l = assetsFileMapReader.readLine()) != null) {
                String[] split = l.split("=");
                if (split.length != 2) {
                    Log.e(TAG, "read error md5 cache map :" + l);
                    continue;
                }
                assetsFileMD5Map.put(split[0], split[1]);
            }

            Log.i(TAG, "loaded fileMD5Map :" + fileMD5Map.size());
            Log.i(TAG, "loaded assetsFileMD5Map :" + assetsFileMD5Map.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(fileMapReader, assetsFileMapReader);
        }

    }

    /**
     * 更新拷贝的资源映射
     */
    public static void updateMapFile() {

        if (fileMD5Map == null || fileMD5Map.size() == 0) return;

        Context context = BaseApplication.getApplication();
        if (context == null) {
            Log.e(TAG, "updateMapFile error , context is null");
            return;
        }
        Log.i(TAG, "updateMapFile");
        File file = new File(getResourceDir(context), "res_md5_map");
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (String key : fileMD5Map.keySet()) {
                bufferedWriter.write(key + "=" + fileMD5Map.get(key));
                bufferedWriter.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取资源目录
     *
     * @param context
     * @return
     */
    private static String getResourceDir(Context context) {

        if (!TextUtils.isEmpty(dirPath)) return dirPath;

        if (context == null) {
            return null;
        }

        File file = context.getFilesDir();
        if (file == null) {
            file = new File("/data/data/" + context.getPackageName() + "/files");
        }
        dirPath = file.getAbsolutePath();
        return dirPath;
    }

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    private static String getCacheDir(Context context) {

        if (!TextUtils.isEmpty(cacheDirPath)) return cacheDirPath;

        if (context == null) {
            return null;
        }

        File file = context.getCacheDir();
        if (file == null) {
            file = new File("/data/data/" + context.getPackageName() + "/cache");
        }
        cacheDirPath = file.getAbsolutePath();
        return cacheDirPath;
    }


    private static InputStream openAssets(Context context, String resName) {
        return openAssets(context, resName, true);
    }

    private static InputStream openAssets(Context context, String resName, boolean printError) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(resName);
        } catch (IOException e) {
            if (printError) {
                android.util.Log.e(TAG, "file " + resName + " not found in assets folder, Did you forget add it?");
                e.printStackTrace();
            }
        }
        return inputStream;
    }

    /**
     * 拷贝assets目录下文件夹到指定目录
     *
     * @param context    上下文
     * @param assetsPath assets中文件夹或文件名
     * @param savePath   目标路径
     * @return 0：成功，-1：失败
     */

    public static int copyFilesFromAssets(Context context, String assetsPath, String savePath) {
        if (context == null) {
            return -1;
        }
        initMD5Map();
        Log.i(TAG, "copyFilesFromAssets " + assetsPath + " ---> " + savePath);
        try {
            String[] fileNames = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, assetsPath + "/" + fileName, savePath + "/" + fileName);
                }
            } else {
                copyResource(context, assetsPath, true, null, savePath);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 从assets目录中拷贝资源文件到指定目录下，如果是zip文件则解压
     *
     * @param context       Android环境句柄
     * @param resName       资源名
     * @param resMd5sumName 资源对应的md5名
     * @param isMD5         是否进行MD5校验,如果校验和相同则忽略拷贝和解压
     * @param destPath      目标文件
     * @return -1 拷贝失败; 0 MD5相同,略过拷贝; 1 拷贝成功
     */
    public static synchronized int copyResource(Context context, String resName, boolean isMD5, String resMd5sumName, String destPath) {
        if (context == null) {
            return -1;
        }
        Log.i(TAG, "start copyResource :" + resName);

        File destFile;
        if (TextUtils.isEmpty(destPath)) {
            destFile = new File(getResourceDir(context), resName);
        } else {
            destFile = new File(destPath);
        }

        InputStream resIs = null;

        try {
            resIs = openAssets(context, resName);
            if (resIs == null) {
                android.util.Log.e(TAG, resName + " is null");
                return -1;
            }


            // md5校验文件先处理: 是否为md5校验文件;md5文件不存在才进行拷贝
            if (resName.endsWith("md5sum")) {
                if (!destFile.exists()) copyDestFileWithRetry(resIs, destFile);
                return 1;
            }

            String assetsMD5 = "";
            int resLen = resIs.available();

            // 获取assets资源的md5值
            if (isMD5) {
                assetsMD5 = getAssetsMD5(context, resName, resMd5sumName, resIs);
            }

            // 判断是否为压缩文件 是的话校验md5值
            if (isMD5 && !isNeedCopyZip(resName, assetsMD5)) {
                Log.i(TAG, "copy " + resName + ": zip file md5 match,skip copy");
                return 0;
            }

            // 判断文件大小、md5 值是否相同
            if (isMD5 && !isNeedCopy(destFile, assetsMD5, resName, resLen)) {
                Log.i(TAG, resName + " exists,skip copy");
                return 0;
            }

            // 开始拷贝文件
            if (copyDestFileWithRetry(resIs, destFile)) {

                if (isZipFile(destFile)) {
                    boolean result = unZip(destFile, destFile.getParentFile());
                    Log.i(TAG, destFile + " unzip result:" + result);

                    if (isMD5 && result) makeMD5(resName, destFile);

                    // 如果校验MD5，删除原压缩包文件，仅保留md5校验文件
                    if (result && isMD5 && destFile.delete()) {
                        Log.i(TAG, "unzip done,delete original file :" + destFile);
                    }
                } else {
                    if (isMD5) makeMD5(resName, destFile);
                }
                return 1;
            } else {
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 最后才关闭输入流
            CloseUtils.closeIO(resIs);
        }


        return 1;
    }

    /**
     * 获取Asset资源的MD5值
     * 1. 如果给定的resMd5sumName不为空，直接拷贝并且读取值
     * 2. 如果assets下的映射表包含该资源，直接读取
     * 3. 如果都不满足，则会从输入流中读取（大文件耗时）
     */
    private static String getAssetsMD5(Context context, String resName, String resMd5sumName, InputStream resIs) {
        String result = "";

        if (!TextUtils.isEmpty(resMd5sumName)) {
            result = getAssetsMD5WithName(context, resMd5sumName, resIs);
        } else if (assetsFileMD5Map.containsKey(resName)) {
            result = assetsFileMD5Map.get(resName);
        } else {
            result = realGetAssetsMD5(resIs);
        }

        return result;
    }

    /**
     * 读取 resMd5sumName 兼容外部传入自定义md5文件的情况
     *
     * @param context
     * @param resMd5sumName assets目录下的资源路径
     * @param resIs
     * @return
     */
    private static String getAssetsMD5WithName(Context context, String resMd5sumName, InputStream resIs) {
        String result = getMD5FromRes(context, resMd5sumName);

        if (TextUtils.isEmpty(result)) {
            result = realGetAssetsMD5(resIs);
        }
        return result;
    }

    private static String realGetAssetsMD5(InputStream inputStream) {
        return bytesToHex(getMD5ByteArray(inputStream));
    }

    private static boolean checkMD5(InputStream inputStream, File file) {
        try {
            return realGetAssetsMD5(inputStream).equals(bytesToHex(getFileMD5(file)));
        } catch (Exception ignore) {
        }

        return false;
    }

    /**
     * 从Assets目录下读取MD5校验值
     *
     * @param context
     * @param resMd5sumName
     * @return
     */
    private static String getMD5FromRes(Context context, String resMd5sumName) {
        try {
            InputStream resMd5Is = openAssets(context, resMd5sumName);
            if (resMd5Is == null) return null;

            File cacheFile = new File(getCacheDir(context), resMd5sumName);
            if (!copyDestFileWithRetry(resMd5Is, cacheFile)) {
                return null;
            }
            CloseUtils.closeIO(resMd5Is);
            String result = FileIOUtils.readFile2String(cacheFile);
            cacheFile.delete();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean isNeedCopy(File destFile, String assetsMD5, String resName, int resLen) {
        // 不存在或者长度不一致，需要重新拷贝
        if (!destFile.exists() || destFile.length() != resLen) return true;

        // 长度一致，比较MD5是否相等
        // 读取存储中的MD5校验文件，对比内容，如果相等则不需要拷贝
        try {
            String md5 = fileMD5Map.get(resName);
            if (assetsMD5 != null && assetsMD5.equals(md5)) return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private static boolean isNeedCopyZip(String resName, String assetsMD5) {
        // 压缩文件
        if (resName.endsWith("rar") || resName.endsWith("zip")) {
            try {
                // 读取映射表中md5值，如果不存在则需要重新拷贝，存在则对比是否相等
                if (!fileMD5Map.containsKey(resName)) return true;

                String fileMd5 = fileMD5Map.get(resName);
                if (fileMd5 != null && fileMd5.equalsIgnoreCase(assetsMD5)) return false;
            } catch (Exception ignore) {
                return true;
            }

        }

        return true;
    }


    /**
     * 带重试的文件拷贝
     *
     * @param inputStream
     * @param destFile
     * @return
     */
    private static boolean copyDestFileWithRetry(InputStream inputStream, File destFile) {
        //保存文件，增加 5 次重试机制
        boolean ret = false;
        for (int i = 0; i < 5; i++) {
            ret = copyDestFile(inputStream, destFile);
            if (ret) {
                CloseUtils.closeIO(inputStream); // 成功就关闭IO流，太早关闭会导致重试失效，太晚会导致内存占用过高
                break;
            } else {
                Log.e(TAG, "retry save:" + destFile.getAbsolutePath() + ",retry count : " + i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 保存文件 不关闭输入流
     *
     * @param inputStream
     * @param destFile
     * @return
     */
    private static boolean copyDestFile(InputStream inputStream, File destFile) {
        Log.i(TAG, "copyDestFile: " + destFile);
        FileIOUtils.createOrExistsFile(destFile);
        BufferedOutputStream bos = null;
        try {
            inputStream.reset();
            int realLen = inputStream.available();
            bos = new BufferedOutputStream(new FileOutputStream(destFile), 16 * 1024);

            byte[] data = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = inputStream.read(data)) != -1) {
                bos.write(data, 0, len);
            }
            bos.flush();
            if (realLen != destFile.length()) {
                Log.e(TAG, destFile + ",destFile.len:" + destFile.length() + ",realLen=" + realLen);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.closeIO(bos);
        }
    }

    /**
     * 解压文件到目标目录
     * 注意压缩文件应为UTF-8格式编码，否则会报错（设置字符集需要添加Jar/API > 24）
     * windows下可以使用7zip进行压缩，添加参数 cu=on；或者使用脚本压缩
     *
     * @param zipfileName
     * @param destDir
     * @return
     */
    private static boolean unZip(File zipfileName, File destDir) {
        Log.i(TAG, "start unzip");
        long startTimes = System.currentTimeMillis();

        byte[] data = new byte[BUFFER_SIZE];

        BufferedInputStream bis = null;
        OutputStream outputStream = null;
        try {
            ZipFile zipFile = new ZipFile(zipfileName);
            Enumeration<? extends ZipEntry> emu = zipFile.entries();
            while (emu.hasMoreElements()) {
                ZipEntry entry = emu.nextElement();
                if (entry.isDirectory()) {
                    new File(destDir, entry.getName()).mkdirs();
                    continue;
                }
//                Log.d(TAG, "unzip: " + entry.getName());
                bis = new BufferedInputStream(zipFile.getInputStream(entry));
                File destItemFile = new File(destDir,
                        entry.getName());
                outputStream = new FileOutputStream(destItemFile);
                BufferedOutputStream bos = new BufferedOutputStream(outputStream, 16 * 1024);
                int readSize;
                while ((readSize = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                    bos.write(data, 0, readSize);
                }
                bos.flush();
                CloseUtils.closeIO(bos, outputStream, bis);
            }
            Log.i(TAG, "unzip cost :" + (System.currentTimeMillis() - startTimes));
            return true;
        } catch (IOException e) {
            Log.e(TAG, "unzip file error,please make sure the zip file is encoded by UTF-8");
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(outputStream, bis);
        }

        return false;
    }

    /**
     * 将md5信息更新到 fileMD5Map
     *
     * @param destFile
     */
    private static void makeMD5(String resName, File destFile) {
        Log.i(TAG, "add md5 info :" + resName);
        String md5 = bytesToHex(getFileMD5(destFile));
        fileMD5Map.put(resName, md5);
    }

    /**
     * 获取输入流的MD5值
     *
     * @param in
     * @return
     */
    private static byte[] getMD5ByteArray(InputStream in) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[BUFFER_SIZE];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, length);
            }
            return messagedigest.digest();
        } catch (NoSuchAlgorithmException nsaex) {
            nsaex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 将byte[]数据转成String
     *
     * @param bytes 数据流
     * @return string对象
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte[] MAGIC = {'P', 'K', 0x3, 0x4};

    /**
     * 检测是否是zip文件
     *
     * @param f 文件
     * @return 是否是zip文件
     */
    public static boolean isZipFile(File f) {
        boolean isZip = true;
        byte[] buffer = new byte[MAGIC.length];
        try {
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            raf.readFully(buffer);
            for (int i = 0; i < MAGIC.length; i++) {
                if (buffer[i] != MAGIC[i]) {
                    isZip = false;
                    break;
                }
            }

        } catch (Throwable e) {
            isZip = false;
        }
        return isZip;
    }
}
