package com.didi.libnetwork.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.didi.libcommon.utils.AppGlobals

/**
 * @author: zangjin
 * @date: 2022/4/9
 */
@Database(entities = [Cache::class], version = 1)
abstract class CacheDatabase: RoomDatabase() {
    companion object {
        //内存数据库
        //只存在于内存中，也就是说进程被杀之后，数据随之丢失
        //Room.inMemoryDatabaseBuilder()

        //数据库创建和打开后的回调
        //.addCallback()
        //设置查询的线程池
        //.setQueryExecutor()
        //.openHelperFactory()
        //room的日志模式
        //.setJournalMode()
        //数据库升级异常之后的回滚
        //.fallbackToDestructiveMigration()
        //数据库升级异常后根据指定版本进行回滚
        //.fallbackToDestructiveMigrationFrom()
        // .addMigrations(CacheDatabase.sMigration)
        var database =
            Room.databaseBuilder(AppGlobals.getApplication(), CacheDatabase::class.java, "ppjoke_cache")
                .allowMainThreadQueries().build()
    }


    abstract fun cacheDao(): CacheDao?
}