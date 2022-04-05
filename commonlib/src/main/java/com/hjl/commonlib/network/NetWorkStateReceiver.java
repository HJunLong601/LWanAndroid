package com.hjl.commonlib.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.hjl.commonlib.utils.LogUtils;
import com.hjl.commonlib.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by long on 2019/10/12
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetWorkState";
    private boolean NetAvailable;
    public List<NetWorkUtils.OnNetworkChangeListener> observers = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        //wifi开关监听
        isOpenWifi(intent);
        //是否连接wifi
        isConnectionWifi(intent);
        //监听网络连接设置
        isConnection(intent, context);
    }

    private void isOpenWifi(Intent intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                //Wifi关闭
                case WifiManager.WIFI_STATE_DISABLED:
                    LogUtils.e(TAG, "wifiState:wifi关闭！");
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    LogUtils.e(TAG, "wifiState:wifi打开！");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 连接有用的wifi（有效无线路由）
     * WifiManager.WIFI_STATE_DISABLING与WIFI_STATE_DISABLED的时候，根本不会接到这个广播
     */
    private void isConnectionWifi(Intent intent) {
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                //wifi连接
                if (isConnected) {
                    LogUtils.e(TAG, "isConnected:isWifi:true");
                }
            }
        }
    }

    /**
     * 监听网络连接的设置，包括wifi和移动数据的打开和关闭。(推荐)
     * 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
     * isConnected()	判断网络连接是否存在
     * isAvailable()	判断网络连接（注：isConnected为true，不代表isAvailable为true）
     * getDetailedState()	（详细）报告当前网络状态 getState()报告当前网络状态
     * getExtrInfo()	报告关于网络状态的额外信息，由较低的网络层提供的
     * getType()	获取当前网络的类型 和ConnectivityManager.TYPE_**对比
     * getTypeName()	获取当前网络的类型名如 “WIFI” or “MOBILE”
     */
    private void isConnection(Intent intent, Context context) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) {

                // connected to the internet
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        LogUtils.e(TAG, "当前WiFi连接可用 ");
                        for (NetWorkUtils.OnNetworkChangeListener listener : observers){
                            listener.onNetworkAvailable(NetWorkUtils.NetState.WIFI);
                        }
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        LogUtils.e(TAG, "当前移动网络连接可用 ");
                        for (NetWorkUtils.OnNetworkChangeListener listener : observers){
                            listener.onNetworkAvailable(NetWorkUtils.NetState.MOBILE);
                        }
                    }
                    NetAvailable = true;
                } else {
                    LogUtils.e(TAG, "当前没有网络连接呢，请确保你已经打开网络 ");
                    NetAvailable = false;
                    for (NetWorkUtils.OnNetworkChangeListener listener : observers){
                        listener.onNetworkUnavailable();
                    }
                }
                LogUtils.d(TAG, "TypeName：" + activeNetwork.getTypeName());
                LogUtils.d(TAG, "SubtypeName：" + activeNetwork.getSubtypeName());
                LogUtils.d(TAG, "State：" + activeNetwork.getState());
                LogUtils.d(TAG, "DetailedState："
                        + activeNetwork.getDetailedState().name());
                LogUtils.d(TAG, "ExtraInfo：" + activeNetwork.getExtraInfo());
                LogUtils.d(TAG, "Type：" + activeNetwork.getType());

            } else {   // not connected to the internet
                LogUtils.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                NetAvailable = false;
                for (NetWorkUtils.OnNetworkChangeListener listener : observers){
                    listener.onNetworkUnavailable();
                }
            }
        }
    }

    public void addNetworkChangeListener(NetWorkUtils.OnNetworkChangeListener listener){
        observers.add(listener);
    }


}
