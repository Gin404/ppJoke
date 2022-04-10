package com.example.ppjoke.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.didi.libcommon.utils.AppGlobals
import com.example.ppjoke.model.BottomBar
import com.example.ppjoke.model.Destination
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

object AppConfig {
    //根据json获取Destination实例
    val sDestConfig: HashMap<String, Destination> by lazy {
        val content = parseFile("destination.json")
        JSON.parseObject(content, object : TypeReference<HashMap<String, Destination>>(){}.type)
    }

    //解析底部导航栏配置文件
    val sBottomBar: BottomBar by lazy {
        val content = parseFile("main_tabs_config.json")
        JSON.parseObject(content, BottomBar::class.java)
    }

    private fun parseFile(fileName: String):  String {
        val assets = AppGlobals.getApplication().assets

        var stream: InputStream? = null
        val builder = StringBuilder()
        try {
            stream = assets.open(fileName)
            stream.use {
                InputStreamReader(it).use { isreader ->
                    BufferedReader(isreader).use { reader ->
                        var line: String?
                        do {
                            line = reader.readLine()
                            if (line == null) {
                                break
                            }
                            builder.append(line)
                        } while (true)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return builder.toString()
    }
}
