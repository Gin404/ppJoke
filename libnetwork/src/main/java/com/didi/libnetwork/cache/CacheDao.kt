package com.didi.libnetwork.cache

import androidx.room.*

/**
 * 用于操作数据库的类
 * @author: zangjin
 * @date: 2022/4/9
 */
@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(cache: Cache): Long

    @Query("select * from cache where 'key' =:key")
    fun getCache(key: String): Cache?

    @Delete
    fun delete(cache: Cache): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(cache: Cache): Int
}