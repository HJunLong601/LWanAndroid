package com.hjl.module_base.datbase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DataCacheEntity::class],version = 1, exportSchema = false)
abstract class WanDatabase : RoomDatabase(){

    abstract fun datacacheDao() : DataCacheDao


    companion object{

        private var instance : WanDatabase? = null

        fun getInstance(context: Context): WanDatabase {

            synchronized(WanDatabase::class.java){
                if (instance == null){
                    instance = Room.databaseBuilder(
                            context,
                            WanDatabase::class.java,
                            "dataCache").build()
                }
            }

            return instance!!
        }

    }

}