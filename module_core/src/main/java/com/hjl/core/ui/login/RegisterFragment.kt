package com.hjl.core.ui.login


import androidx.lifecycle.ViewModelProvider
import com.hjl.commonlib.base.BaseApplication
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.databinding.CoreFragmentRegisterBinding
import com.hjl.core.viewmodel.UserViewModel
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2

/**
 * author: long
 * description Register Fragment
 * Date: 2020/8/11
 */
class RegisterFragment : BaseMVVMFragment2<CoreFragmentRegisterBinding, UserViewModel>() {


    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_register
    }

    override fun initData() {

    }

    override fun initViewModel() {
        // 共用LoginActivity创建的VM
        viewModel = ViewModelProvider(activity!!).get(UserViewModel::class.java)
    }

    override fun initView() {

        binding.registerTitleTv.setOnClickListener {
            (activity as LoginActivity).switchItem(0)
        }

//        binding.registerAccountEt.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus){
//                binding.registerAccountIv.setImageResource(R.drawable.core_icon_login_blue)
//                binding.registerAccountLine.background = resources.getDrawable(R.color.common_base_theme_color)
//            }else{
//                register_account_iv.setImageResource(R.drawable.core_icon_login_gray)
//                register_account_line.background = resources.getDrawable(R.color.common_divider_line_color)
//            }
//
//        }
//        register_password_et.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus){
//                register_password_iv.setImageResource(R.drawable.core_icon_password_blue)
//                register_password_line.background = resources.getDrawable(R.color.common_base_theme_color)
//            }else{
//                register_password_iv.setImageResource(R.drawable.core_icon_password_gray)
//                register_password_line.background = resources.getDrawable(R.color.common_divider_line_color)
//            }
//        }
//        register_confirm_pass_et.setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus){
//                register_confirm_pass_iv.setImageResource(R.drawable.core_icon_password_blue)
//                register_confirm_pass_line.background = resources.getDrawable(R.color.common_base_theme_color)
//            }else{
//                register_confirm_pass_iv.setImageResource(R.drawable.core_icon_password_gray)
//                register_confirm_pass_line.background = resources.getDrawable(R.color.common_divider_line_color)
//            }
//        }

        binding.registerConfirmBtn.setOnClickListener {
            if (binding.registerAccountEt.text!!.isNotEmpty() && binding.registerPasswordEt.text!!.isNotEmpty()
                    && binding.registerConfirmPassEt.text!!.isNotEmpty()){
                viewModel.register(
                    binding.registerAccountEt.text.toString(),
                    binding.registerPasswordEt.text.toString(),
                    binding.registerConfirmPassEt.text.toString()
                )
            }else{
                ToastUtil.show(
                    BaseApplication.getApplication().getString(R.string.pls_fll_n_yr_ccnt_n)
                )
            }
        }
    }

    override fun loadData() {

    }


}