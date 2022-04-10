package com.example.ppjoke.utils

import android.content.ComponentName
import androidx.fragment.app.FragmentActivity
import androidx.navigation.*
import com.didi.libcommon.utils.AppGlobals

object MyNavGraphBuilder {
    fun build(controller: NavController, activity: FragmentActivity, containerId: Int) {
        //provider存储各种navigator，
        val provider = controller.navigatorProvider
        //val fragmentNavigator = provider.getNavigator(FragmentNavigator::class.java)
        val fragmentNavigator = FixFragmentNavigator(activity, activity.supportFragmentManager, containerId)
        provider.addNavigator(fragmentNavigator)
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
                destination.setComponentName(ComponentName(AppGlobals.getApplication().packageName, value.className))
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