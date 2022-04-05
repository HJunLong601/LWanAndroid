package com.hjl.core.ui.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.databinding.CoreFragmentLoginBinding

import com.hjl.core.repository.UserRepository
import com.hjl.core.viewmodel.UserViewModel
import com.hjl.jetpacklib.mvvm.view.BaseMVVMFragment2



/**
 * author: long
 * description Login Fragment
 * Date: 2020/8/11
 */
class LoginFragment : BaseMVVMFragment2<CoreFragmentLoginBinding, UserViewModel>(){

    override fun initLayoutResID(): Int {
        return R.layout.core_fragment_login
    }

    override fun initData() {

    }

    override fun initViewModel() {
        // 共用LoginActivity创建的VM
        viewModel = ViewModelProvider(activity!!).get(UserViewModel::class.java)
    }

    override fun initView() {

        binding.loginTitleTv.setOnClickListener {
            (activity as LoginActivity).switchItem(1)
        }

        binding.loginAccountEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.loginAccountIv.setImageResource(R.drawable.core_icon_login_blue)
                binding.loginAccountLine.background = resources.getDrawable(R.color.common_base_theme_color)
            }else{
                binding.loginAccountIv.setImageResource(R.drawable.core_icon_login_gray)
                binding.loginAccountLine.background = resources.getDrawable(R.color.common_divider_line_color)
            }
        }
        binding.loginPasswordEt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.loginPasswordIv.setImageResource(R.drawable.core_icon_password_blue)
                binding.loginPasswordLine.background = resources.getDrawable(R.color.common_base_theme_color)
            }else{
                binding.loginPasswordIv.setImageResource(R.drawable.core_icon_password_gray)
                binding.loginPasswordLine.background = resources.getDrawable(R.color.common_divider_line_color)
            }
        }

        binding.loginConfirmBtn.setOnClickListener {

            if (binding.loginAccountEt.text.toString().isNotEmpty() && binding.loginPasswordEt.text.toString().isNotEmpty()){
                viewModel.login(binding.loginAccountEt.text.toString(),binding.loginPasswordEt.text.toString())
            }else{
                ToastUtil.show("请填写账号和密码噢")
            }

        }
    }

    override fun loadData() {

    }
}