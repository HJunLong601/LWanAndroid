package com.hjl.core.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * author: long
 * description please add a description here
 * Date: 2021/5/26
 */


interface ViewBindingHolder<B : ViewBinding> {
    var binding: B?
    // Only valid between onCreateView and onDestroyView.
    fun requireBinding() = checkNotNull(binding)
    fun requireBinding(lambda: (B) -> Unit) {
        binding?.let {
            lambda(it)
        }}
    /**
     * Make sure to use this with Fragment.viewLifecycleOwner
     */
    fun registerBinding(binding: B, lifecycleOwner: LifecycleOwner) {
        this.binding = binding
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onDestroy(owner: LifecycleOwner?) {
                owner?.lifecycle?.removeObserver(this)
                this@ViewBindingHolder.binding = null
            }
        })
    }
}
interface DataBindingHolder<B : ViewDataBinding> : ViewBindingHolder<B>

interface DefaultLifecycleObserver : LifecycleObserver{
    fun onDestroy(owner: LifecycleOwner?)
}