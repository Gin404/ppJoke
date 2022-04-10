package com.didi.libnetwork

import okhttp3.FormBody

/**
 * Post请求
 * @author: zangjin
 * @date: 2022/4/5
 */
class PostRequest<T>(mUrl: String) : Request<T, PostRequest<T>>(mUrl) {
    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        //post请求表单提交
        val bodyBuilder = FormBody.Builder()
        params.entries.forEach { entry ->
            bodyBuilder.add(entry.key, entry.value.toString())
        }

        return builder.url(mUrl).post(bodyBuilder.build()).build()
    }
}