package com.hjl.commonlib.network;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.hjl.commonlib.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by long on 2019/10/12
 */
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetWorkState";
    private List<NetWorkUtils.OnNetworkChangeListener> observers = new ArrayList<>();

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.d(TAG, "onLost: 无网络连接");
        for (NetWorkUtils.OnNetworkChangeListener listener : observers){
            listener.onNetworkUnavailable();
        }
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d(TAG, "onCapabilitiesChanged: 网络类型为wifi");
                for (NetWorkUtils.OnNetworkChangeListener listener : observers){
                    listener.onNetworkAvailable(NetWorkUtils.NetState.WIFI);
                }
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d(TAG, "onCapabilitiesChanged: 蜂窝网络");
                for (NetWorkUtils.OnNetworkChangeListener listener : observers){
                    listener.onNetworkAvailable(NetWorkUtils.NetState.MOBILE);
                }
            } else {
                Log.d(TAG, "onCapabilitiesChanged: 其他网络");
            }
        }
    }

    public void addNetworkChangeListener(NetWorkUtils.OnNetworkChangeListener listener){
        observers.add(listener);
    }
}
