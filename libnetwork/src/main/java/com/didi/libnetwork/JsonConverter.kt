package com.didi.libnetwork

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.lang.reflect.Type

/**
 * @author: zangjin
 * @date: 2022/4/5
 */
class JsonConverter: Convert<Any?> {
    override fun convert(response: String?, type: Type?): Any? {
        val jsonObject: JSONObject = JSON.parseObject(response)
        val data = jsonObject.getJSONObject("data")

        if (data != null) {
            val data1 = data["data"]
            return JSON.parseObject<Any>(data1.toString(), type)
        }
        return null
    }
}