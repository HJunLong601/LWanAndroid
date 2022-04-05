package com.hjl.module_base.datbase

import androidx.room.*

@Dao
interface DataCacheDao {

    @Query("SELECT * FROM dataCache")
    fun getAll() : List<DataCacheEntity>

    @Query("SELECT * FROM dataCache WHERE cacheKey = :key")
    fun findByID(key : String) : DataCacheEntity?

    @Insert
    fun insertEntity(entity: DataCacheEntity)

    @Insert
    fun insertAll(vararg entities : DataCacheEntity)

    @Delete
    fun deleteEntity(entity: DataCacheEntity)

    @Update
    fun updateEntity(vararg entity: DataCacheEntity)


}