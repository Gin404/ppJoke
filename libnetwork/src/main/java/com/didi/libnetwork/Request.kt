package com.didi.libnetwork

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import androidx.annotation.IntDef
import androidx.arch.core.executor.ArchTaskExecutor
import com.didi.libnetwork.cache.CacheManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author: zangjin
 * @date: 2022/4/5
 */
abstract class Request<T, R:Request<T, R>>(
        val mUrl: String
    ): Cloneable{
    private val headers = mutableMapOf<String, String>()
    val params = mutableMapOf<String, Any>()

    private lateinit var cacheKey: String
    private var mType: Type? = null
    private var mClass: Class<Any?>? = null

    private var mCacheStrategy: Int = NET_ONLY

    companion object {
        //仅仅访问本地缓存，即便本地缓存不存在，也不会发起网络请求
        const val CACHE_ONLY = 1
        //先访问缓存，同时发起网络请求，成功后缓存到本地
        const val CACHE_FIRST = 2
        //仅仅访问服务器，不做任何存储
        const val NET_ONLY = 3
        //访问网络成功后，访问本地
        const val NET_CACHE = 4
    }

    @IntDef(CACHE_ONLY, CACHE_FIRST, NET_ONLY, NET_CACHE)
    annotation class CacheStrategy

    fun addHeader(key: String, value: String): R {
        headers[key] = value
        return this as R
    }

    fun addParam(key: String, value: Any?): R {
        if (value == null) {
            return this as R
        }

        try {
            if (value.javaClass == String::class.java) {
                params[key] = value
            } else {
                val field = value.javaClass.getField("TYPE")
                val claz = field[null] as Class<*>
                if (claz.isPrimitive) {
                    params[key] = value
                }
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace();
        } catch (e: IllegalAccessException) {
            e.printStackTrace();
        }

        return this as R
    }

    fun cacheKey(key: String): R {
        cacheKey = key
        return this as R
    }

    @SuppressLint("RestrictedApi")
    fun execute(callback: JsonCallback<T>?) {
        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(object : Runnable {
                override fun run() {
                    val response = readCache()
                    callback?.onCacheSuccess(response)
                }

            })
        }

        if (mCacheStrategy != CACHE_ONLY) {
            getCall().enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val response = ApiResponse<T>()
                    response.message = e.message
                    callback?.onError(response)
                }

                override fun onResponse(call: Call, response: Response) {
                    //parseResponse
                }

            })
        }

    }

    /**
     * 同步请求
     */
    fun execute(): ApiResponse<T>? {
        if (mType == null) {
            throw RuntimeException("同步方法,response 返回值 类型必须设置")
        }

        if (mCacheStrategy == CACHE_ONLY) {
            return readCache()
        }

        var result: ApiResponse<T>? = null
        try {
            val response = getCall().execute()
            result = parseResponse(response, null)
        } catch (e: IOException) {
            e.printStackTrace()
            if (result == null) {
                result = ApiResponse()
                result.message = e.message
            }
        }

        return result
    }

    private fun readCache(): ApiResponse<T> {
        val key = if (TextUtils.isEmpty(cacheKey)) {
            generateCacheKey()
        } else {
            cacheKey
        }

        val cache = CacheManager.getCache(key)
        val result = ApiResponse<T>()
        result.status = 304
        result.message = "缓存获取成功"
        result.body = cache as T?
        result.success = true
        return result
    }

    private fun getCall(): Call {
        val builder = okhttp3.Request.Builder()
        addHeaders(builder)
        val request = generateRequest(builder)
        return ApiService.okHttpClient.newCall(request)
    }

    private fun addHeaders(builder: okhttp3.Request.Builder) {
        for (entry in headers.entries) {
            builder.addHeader(entry.key, entry.value)
        }
    }

    fun responseType(type: Type): R {
        mType = type
        return this as R
    }

    fun responseType(clazz: Class<Any?>): R {
        mClass = clazz
        return this as R
    }

    fun cacheStrategy(@CacheStrategy cacheStrategy: Int): R {
        mCacheStrategy = cacheStrategy
        return this as R
    }

    /**
     * 解析响应
     */
    private fun parseResponse(response: Response, callback: JsonCallback<T>?): ApiResponse<T> {
        var message: String? = null

        val status = response.code
        val success = response.isSuccessful

        val result = ApiResponse<T>()
        val converter = ApiService.sConvert
        try {
            val content = response.body.toString()
            if (success) {
                if (callback != null) {
                    val type = callback.javaClass.genericSuperclass as ParameterizedType
                    val argument = type.actualTypeArguments[0]
                    result.body = converter?.convert(content, argument) as T?
                } else if (mType != null){
                    result.body = converter?.convert(content, mType) as T?
                } else {
                    Log.e("request", "parseResponse: 无法解析 ")
                }
            } else {
                message = content
            }
        } catch (e: Exception) {
            message = e.message
            result.success = false
        }

        result.success = success
        result.status = status
        result.message = message

        if (mCacheStrategy != NET_ONLY &&result.success && result.body!=null && result.body is Serializable) {
            saveCache(result.body)
        }
        return result
    }

    private fun saveCache(body: T?) {
        val key = if (TextUtils.isEmpty(cacheKey)) {
            generateCacheKey()
        } else {
            cacheKey
        }

    }

    private fun generateCacheKey(): String {
        cacheKey = UrlCreator.createUrlFromParams(mUrl, params)
        return cacheKey
    }

    abstract fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request
}

abstract class JsonCallback<T> {
    open fun onSuccess(response: ApiResponse<T>?) {

    }

    open fun onError(response: ApiResponse<T>?) {

    }

    open fun onCacheSuccess(response: ApiResponse<T>?) {

    }
}

