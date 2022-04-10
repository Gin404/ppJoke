package com.didi.libnetwork

/**
 * GET请求
 * @author: zangjin
 * @date: 2022/4/5
 */
class GetRequest<T>(mUrl: String) : Request<T, GetRequest<T>>(mUrl) {

    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        return builder.get().url(UrlCreator.createUrlFromParams(mUrl, params)).build()
    }
}