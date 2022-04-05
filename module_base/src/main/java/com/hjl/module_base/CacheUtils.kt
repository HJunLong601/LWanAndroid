package com.hjl.module_base

import com.hjl.commonlib.base.BaseApplication
import com.hjl.module_base.datbase.DataCacheEntity
import com.hjl.module_base.datbase.WanDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Description TODO
 * Author long
 * Date 2020/6/14 23:13
 */
object CacheUtils {

    private val dao = WanDatabase.getInstance(BaseApplication.getApplication()).datacacheDao()
    val TIME_UPDATE_PERIOD = 30 * 60 * 1000L

    @JvmStatic
    fun saveCache(key : String, data : String){

        GlobalScope.launch {
            val entity = dao.findByID(key)

            if (entity != null
                    && !entity.cacheContent.isNullOrEmpty()
                    && (System.currentTimeMillis() - entity.updateTime!!.toLong() < TIME_UPDATE_PERIOD)){
                // do nothing
            }else if (entity != null){
                dao.updateEntity(DataCacheEntity(key,data,System.currentTimeMillis().toString()))
            }else{
                dao.insertEntity(DataCacheEntity(key,data,System.currentTimeMillis().toString()))
            }
        }

    }

    @JvmStatic
    fun getCache(key: String,result : ((content : String)-> Unit)){
        GlobalScope.launch {
            val entity = dao.findByID(key)
            if (entity != null){
                result(entity.cacheContent?:"")
            }else{
                result("")
            }
        }
    }

    @JvmStatic
    fun getCache(key: String) : String{
        return ""
//        val entity = dao.findByID(key)
//        return if (entity != null){
//            entity.cacheContent?:""
//        }else{
//            ""
//        }
    }
}