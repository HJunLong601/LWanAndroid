package com.hjl.commonlib.network.okhttp;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装了请求参数 Map<String,String>
 */
public class RequestParams {

    private Map<String,String> params = new TreeMap<>();
    private Map<String,File> fileMap = new ConcurrentHashMap<>();


    public RequestParams() {
    }

    public RequestParams(Map<String,String> source) {

        if (source != null){
            params.putAll(source);
        }
    }

    public void add(String key,String value){
        params.put(key,value);
    }

    public void add(String key,File file){
        fileMap.put(key,file);
    }

    public boolean hasParams(){
        if (fileMap.size() > 0 || params.size() > 0){
            return true;
        }
        return false;
    }

    public Map<String, File> getFileMap() {
        return fileMap;
    }
    public Map<String, String> getParams() {
        return params;
    }

}
