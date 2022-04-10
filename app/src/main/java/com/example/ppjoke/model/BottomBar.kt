package com.example.ppjoke.model

/**
 * 底部导航栏bean
 */
data class BottomBar(
    val activeColor: String = "#333333",
    val inActiveColor: String = "#666666",
    val selectTab: Int = 0,
    val tabs: List<Tab> = mutableListOf()
)

data class Tab(
    val enable: Boolean = false,
    val index: Int = 0,
    val pageUrl: String = "",
    val size: Int = 0,
    val tintColor: String = "#ff678f",
    val title: String = ""
)