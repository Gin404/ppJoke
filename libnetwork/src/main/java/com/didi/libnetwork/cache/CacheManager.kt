package com.didi.libnetwork.cache

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * @author: zangjin
 * @date: 2022/4/10
 */
class CacheManager {

    companion object {
        fun <T> save(key: String, body: T) {
            val cache = Cache()
            cache.key = key
            cache.data = toByteArray(body)

            CacheDatabase.database.cacheDao()?.save(cache)
        }

        fun getCache(key: String): Any? {
            val cache = CacheDatabase.database.cacheDao()?.getCache(key)

            cache?.data?.let {
                return toObject(it)
            }
            return null
        }

        fun <T> delete(key: String, body: T) {
            val cache = Cache()
            cache.key = key
            cache.data = toByteArray(body)

            CacheDatabase.database.cacheDao()?.delete(cache)
        }

        private fun <T> toByteArray(body: T): ByteArray {
            ByteArrayOutputStream().use { bos ->
                ObjectOutputStream(bos).use { oos ->
                    oos.writeObject(body)
                    oos.flush()
                    return bos.toByteArray()
                }
            }
        }

        private fun toObject(data: ByteArray): Any? {
            ByteArrayInputStream(data).use { bis ->
                ObjectInputStream(bis).use { ois->
                    return ois.readObject()
                }
            }
        }
    }
}