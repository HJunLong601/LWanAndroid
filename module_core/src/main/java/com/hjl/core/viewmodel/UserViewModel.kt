package com.hjl.core.viewmodel


import androidx.lifecycle.MutableLiveData
import com.hjl.commonlib.base.BaseApplication
import com.hjl.commonlib.utils.JsonUtils
import com.hjl.commonlib.utils.LogUtils
import com.hjl.commonlib.utils.ToastUtil
import com.hjl.core.R
import com.hjl.core.repository.UserRepository
import com.hjl.core.utils.SpUtils
import com.hjl.jetpacklib.mvvm.BaseViewModel

/**
 * Author : long
 * Description :
 * Date : 2020/8/16
 */
class UserViewModel : BaseViewModel(){

    private val repository = UserRepository()

    val userstatus = MutableLiveData(0)

    val loginAccountFocus = MutableLiveData<Boolean>()
    val loginPasswordFocus = MutableLiveData<Boolean>()

    val registerAccountFocus = MutableLiveData<Boolean>()
    val registerPasswordFocus = MutableLiveData<Boolean>()
    val registerConfirmFocus = MutableLiveData<Boolean>()


    fun login(username:String,password:String){
        
        launch({
            val userBean = repository.login(username, password)
            val userInfo = JsonUtils.toJSONString(userBean)
            SpUtils.saveUserInfo(userInfo)
            userstatus.postValue(LOGIN_SUCC)
        },{
            LogUtils.e(TAG,it.errorMessage)
            ToastUtil.show(it.errorMessage)
        })
    }

    fun register(username:String,password:String,repassword:String){
        launch({
            val userBean = repository.register(username, password, repassword)
//            SpUtils.saveUserInfo(userBean)
            userstatus.postValue(REGISTER_SUCC)
            ToastUtil.show(BaseApplication.getApplication().getString(R.string.rgstrd_sccssflly))
        },{
            LogUtils.e(TAG,it.errorMessage)
            ToastUtil.show(it.errorMessage)
        })
    }

    companion object{
        const val LOGIN_SUCC = 1
        const val REGISTER_SUCC = 2
    }

}