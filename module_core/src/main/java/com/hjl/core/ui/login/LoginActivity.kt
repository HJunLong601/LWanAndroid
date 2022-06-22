package com.hjl.core.ui.login


import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.hjl.commonlib.adapter.LazyFragmentStateAdapter
import com.hjl.core.R
import com.hjl.core.databinding.CoreActivityLoginBinding
import com.hjl.core.viewmodel.UserViewModel
import com.hjl.jetpacklib.mvvm.view.BaseActivity
import com.hjl.module_base.constants.EventKey
import com.jeremyliao.liveeventbus.LiveEventBus

//@Route(path = AConstant.CORE_LOGIN_ACTIVITY)
class LoginActivity : BaseActivity<CoreActivityLoginBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.core_activity_login
    }

    override fun initView() {

        val fragments = arrayListOf<Fragment>(LoginFragment(), RegisterFragment())
        binding.loginVp.adapter = LazyFragmentStateAdapter(this,fragments)
        binding.loginVp.offscreenPageLimit = fragments.size
        binding.loginVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }


    override fun initData() {
        val model = ViewModelProvider(this)[UserViewModel::class.java]

        model.userstatus.observe(this, Observer {
            when(it){
                UserViewModel.LOGIN_SUCC -> {
                    LiveEventBus.get<String>(EventKey.LOGIN_STATE_CHANGE).post("update")
                    finish()
                }
                UserViewModel.REGISTER_SUCC -> {
                    binding.loginVp.currentItem = 0
                }
            }
        })
    }

    fun switchItem(int: Int){
        binding.loginVp.currentItem = int
    }

    override fun getStatusBarColor(): Int {
        return R.color.common_base_theme_color
    }

    companion object{

        fun startLoginActivity(context: Context){
            val intent = Intent(context,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
//            context.overridePendingTransition(R.anim.core_anim_bottom_to_top,R.anim.core_anim_to_invisiable)
        }
    }

    override fun initLoad() {

    }

}
