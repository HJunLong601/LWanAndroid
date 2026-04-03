package com.hjl.module_base.bus

import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * Author : Codex
 * Description : 仅用于应用进程内分发事件，避免三方事件总线在高版本系统上注册广播导致启动崩溃
 * Date : 2026/4/3
 */
object AppLiveEventBus {

    private val channels = ConcurrentHashMap<String, EventChannel<Any>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): EventChannel<T> {
        return channels.getOrPut(key) { EventChannel() } as EventChannel<T>
    }
}

/**
 * Author : Codex
 * Description : 非粘性事件通道，新观察者不会收到历史事件
 * Date : 2026/4/3
 */
class EventChannel<T> : MutableLiveData<T>() {

    private val version = AtomicInteger(-1)

    @MainThread
    override fun setValue(value: T) {
        version.incrementAndGet()
        super.setValue(value)
    }

    override fun postValue(value: T) {
        version.incrementAndGet()
        super.postValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = VersionObserver(
            version = version,
            delegate = observer,
            lastVersion = version.get()
        )
        super.observe(owner, wrapper)
    }

    fun post(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setValue(value)
        } else {
            postValue(value)
        }
    }

    private class VersionObserver<T>(
        private val version: AtomicInteger,
        private val delegate: Observer<in T>,
        private var lastVersion: Int
    ) : Observer<T> {

        override fun onChanged(value: T) {
            val currentVersion = version.get()
            if (currentVersion <= lastVersion) {
                return
            }
            lastVersion = currentVersion
            delegate.onChanged(value)
        }
    }
}
