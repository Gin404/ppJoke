package com.example.ppjoke.utils

import android.app.Application

object AppGlobals {
    val sApplication: Application by lazy {
        Class.forName("android.app.ActivityThread")
            .getMethod("currentApplication")
            .invoke(null, null as Array<Any?>?) as Application
    }
}