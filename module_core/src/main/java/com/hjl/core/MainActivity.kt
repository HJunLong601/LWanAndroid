package com.hjl.core

import android.Manifest
import androidx.fragment.app.Fragment
import com.hjl.commonlib.adapter.LazyFragmentPagerAdapter
import com.hjl.commonlib.utils.LogUtils
import com.hjl.core.databinding.CoreActivityMainBinding
import com.hjl.core.ui.main.MainFragment
import com.hjl.core.ui.minus.MinusFragment

import com.hjl.jetpacklib.mvvm.view.BaseActivity
import com.permissionx.guolindev.PermissionX


class MainActivity : BaseActivity<CoreActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.core_activity_main
    }

    override fun initData() {

    }

    override fun initView() {
        val fragments = arrayListOf<Fragment>(MinusFragment(),MainFragment())
        val adapter = LazyFragmentPagerAdapter(supportFragmentManager, fragments)
        binding.activityMainVp.adapter = adapter
        binding.activityMainVp.offscreenPageLimit = fragments.size
        binding.activityMainVp.setCurrentItem(1,false)

    }

    override fun initLoad() {
        PermissionX.init(this).permissions(
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION).onExplainRequestReason { deniedList  ->
            showRequestReasonDialog(deniedList,"该权限为保证相关功能的正常使用,如果拒绝可能导致功能不完善甚至崩溃！","我已明白", "取消")
        }.onForwardToSettings { deniedList ->
            showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白", "取消")
        }.request{ allGranted, grantedList, deniedList ->
            if (allGranted) {
                LogUtils.e(TAG,"所有申请的权限都已通过:$grantedList")
            } else {
                LogUtils.e(TAG,"权限被拒绝:$deniedList")
            }

        }
    }

    override fun getStatusBarColor(): Int {
        return R.color.common_base_theme_color
    }
}
