package com.hjl.module_base.router

import com.hjl.commonlib.utils.ToastUtil
import com.therouter.TheRouter

/**
 * 路由统一入口，减少业务层对具体路由框架的直接依赖。
 */
object RouterNavigator {

    fun open(path: String): Boolean {
        return runCatching {
            TheRouter.build(path).navigation()
        }.isSuccess.also { success ->
            if (!success) {
                ToastUtil.show("页面跳转失败")
            }
        }
    }

    fun openSkin(): Boolean {
        return open(RouterName.SKIN_SKIN_ACTIVITY)
    }

    fun openLanguageSetting(): Boolean {
        return open(RouterName.LANGUAGE_SETTING)
    }
}
