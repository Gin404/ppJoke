package com.example.ppjoke.utils

fun dp2px(dpValue: Int): Int {
    val metrics = AppGlobals.sApplication.resources.displayMetrics
    return (metrics.density * dpValue + 0.5f).toInt()
}