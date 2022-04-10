package com.didi.libnetwork

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * 拼接url参数
 * @author: zangjin
 * @date: 2022/4/5
 */
object UrlCreator {
    fun createUrlFromParams(url: String, params: Map<String, Any>): String {
        val builder = StringBuilder()
        builder.append(url)

        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            builder.append("&")
        } else {
            builder.append("?")
        }

        params.entries.forEach { entry->
            try {
                val value = URLEncoder.encode(entry.value.toString(), "UTF-8")
                builder.append(entry.key).append("=").append(value).append("&")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace();
            }
        }
        builder.deleteCharAt(builder.length - 1)
        return builder.toString()
    }
}