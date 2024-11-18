package com.hjl.core


import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hjl.commonlib.adapter.LazyFragmentPagerAdapter
import com.hjl.commonlib.base.BaseApplication
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
//        binding.activityMainVp.offscreenPageLimit = fragments.size
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
            showRequestReasonDialog(
                deniedList,
                BaseApplication.getApplication().getString(R.string.ths_prmssn_s_t_nsr_),
                BaseApplication.getApplication().getString(R.string.i_understand_now),
                BaseApplication.getApplication().getString(R.string.cancel)
            )
        }.onForwardToSettings { deniedList ->
            showForwardToSettingsDialog(
                deniedList,
                BaseApplication.getApplication().getString(R.string.y_nd_t_mnlly_nbl_pr),
                BaseApplication.getApplication().getString(R.string.i_understand_now),
                BaseApplication.getApplication().getString(R.string.cancel)
            )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                LogUtils.e(TAG, "所有申请的权限都已通过:$grantedList")
            } else {
                LogUtils.e(TAG, "权限被拒绝:$deniedList")
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.i("onCreate")
    }

    override fun getStatusBarColor(): Int {
        return R.color.common_base_theme_color
    }
}
