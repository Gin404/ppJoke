package com.example.ppjoke.model

data class Destination(
    val asStarter: Boolean,
    val className: String,
    val id: Int,
    val isFragment: Boolean,
    val needLogin: Boolean,
    val pageUrl: String
)