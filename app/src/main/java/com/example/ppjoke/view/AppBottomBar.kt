package com.example.ppjoke.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import com.example.ppjoke.R
import com.example.ppjoke.utils.AppConfig
import com.example.ppjoke.utils.dp2px
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

@SuppressLint("RestrictedApi")
class AppBottomBar @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null,
                                             defStyleAttr: Int = 0): BottomNavigationView(context, attrs, defStyleAttr) {
    companion object {
        private val sIcons = arrayOf(
            R.drawable.icon_tab_home,
            R.drawable.icon_tab_sofa,
            R.drawable.icon_tab_publish,
            R.drawable.icon_tab_find,
            R.drawable.icon_tab_mine)
    }

    init {
        val bottomBar = AppConfig.sBottomBar
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf()

        val colors = intArrayOf(Color.parseColor(bottomBar.activeColor), Color.parseColor(bottomBar.inActiveColor))
        val colorStateList = ColorStateList(states, colors)

        itemIconTintList = colorStateList
        itemTextColor = colorStateList

        //任何情况下都显示文本
        //LABEL_VISIBILITY_LABELED:设置按钮的文本为一直显示模式
        //LABEL_VISIBILITY_AUTO:当按钮个数小于三个时一直显示，或者当按钮个数大于3个且小于5个时，被选中的那个按钮文本才会显示
        //LABEL_VISIBILITY_SELECTED：只有被选中的那个按钮的文本才会显示
        //LABEL_VISIBILITY_UNLABELED:所有的按钮文本都不显示
        labelVisibilityMode = LABEL_VISIBILITY_LABELED
        selectedItemId = bottomBar.selectTab

        for (tab in bottomBar.tabs) {
            if (!tab.enable) {
                continue
            }
            val id = getId(tab.pageUrl)
            if (id < 0) {
                continue
            }
            val item = menu.add(0, id, tab.index, tab.title)
            item.setIcon(sIcons[tab.index])
        }
        val menuView = getChildAt(0) as BottomNavigationMenuView

        for (tab in bottomBar.tabs) {
            val iconSize = dp2px(tab.size)

            val itemView = menuView.getChildAt(tab.index) as BottomNavigationItemView

            itemView.setIconSize(iconSize)

            if (TextUtils.isEmpty(tab.title)) {
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)))
                itemView.setShifting(false)
            }
        }
    }

    private fun getId(pageUrl: String): Int {
        AppConfig.sDestConfig[pageUrl]?.apply {
            return id
        }

        return -1
    }
}