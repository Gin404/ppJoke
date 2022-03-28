package com.example.ppjoke.utils

import android.content.ComponentName
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator

object MyNavGraphBuilder {
    fun build(controller: NavController) {
        //provider存储各种navigator，
        val provider = controller.navigatorProvider
        val fragmentNavigator = provider.getNavigator(FragmentNavigator::class.java)
        val activityNavigator = provider.getNavigator(ActivityNavigator::class.java)

        val navGraph = NavGraph(NavGraphNavigator(provider))
        //遍历配置信息
        for (value in AppConfig.sDestConfig.values) {
            //如果是fragment就用fragmentNavigator
            if (value.isFragment) {
                val destination = fragmentNavigator.createDestination()
                destination.className = value.className
                destination.id = value.id
                destination.addDeepLink(value.pageUrl)
                navGraph.addDestination(destination)
            } else {
                val destination = activityNavigator.createDestination()
                destination.id = value.id
                destination.addDeepLink(value.pageUrl)
                destination.setComponentName(ComponentName(AppGlobals.sApplication.packageName, value.className))
                navGraph.addDestination(destination)
            }

            //默认启动页
            if (value.asStarter) {
                navGraph.startDestination = value.id
            }
        }

        controller.graph = navGraph
    }
}