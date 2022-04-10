package com.didi.libnetwork

/**
 * @author: zangjin
 * @date: 2022/4/5
 */
class ApiResponse<T> {
    var success = false
    var status = 0
    var message: String? = null
    var body: T? = null
}