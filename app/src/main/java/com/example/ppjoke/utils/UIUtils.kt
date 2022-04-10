package com.example.ppjoke.utils

import com.didi.libcommon.utils.AppGlobals

fun dp2px(dpValue: Int): Int {
    val metrics = AppGlobals.getApplication().resources.displayMetrics
    return (metrics.density * dpValue + 0.5f).toInt()
}