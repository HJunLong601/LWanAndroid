package com.hjl.module_base.datbase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataCache")
data class DataCacheEntity(
        @PrimaryKey val cacheKey : String,
        @ColumnInfo(name = "cacheContent") val cacheContent : String?,
        @ColumnInfo(name = "updateTime") val updateTime : String?)