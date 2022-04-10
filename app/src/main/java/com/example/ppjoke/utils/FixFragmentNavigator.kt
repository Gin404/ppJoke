package com.example.ppjoke.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

/**
 * 自定义导航器
 * @author: zangjin
 * @date: 2022/4/4
 */
@Navigator.Name("fixfragment")
class FixFragmentNavigator(var context: Context,
                           var manager: FragmentManager,
                           var containerId: Int)
    : FragmentNavigator(context, manager, containerId) {

    companion object {
        const val TAG = "FixFragmentNavigator"
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }

        val ft = manager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        val tag = className.substring(className.lastIndexOf(".") + 1)
        var frag: Fragment? = manager.findFragmentByTag(tag)
        if (frag == null) {
            frag = instantiateFragment(
                context, manager,
                className, args
            )
        }
        frag.arguments = args
        val fragments: List<Fragment> = manager.fragments
        for (fragment in fragments) {
            ft.hide(fragment)
        }

        if (!frag.isAdded) {
            ft.add(containerId, frag, tag)
        }
        ft.show(frag)
        ft.setPrimaryNavigationFragment(frag)

        var mBackStack: java.util.ArrayDeque<Int>? = null

        try {
            val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.isAccessible = true
            mBackStack = field.get(this) as java.util.ArrayDeque<Int>?
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        mBackStack?.apply {
            @IdRes val destId = destination.id
            val initialNavigation = isEmpty()
            val isSingleTopReplacement = (navOptions != null && !initialNavigation
                    && navOptions.shouldLaunchSingleTop()
                    && peekLast() == destId)

            val isAdded: Boolean = if (initialNavigation == true) {
                true
            } else if (isSingleTopReplacement) {
                // Single Top means we only want one instance on the back stack
                if (size > 1) {
                    // If the Fragment to be replaced is on the FragmentManager's
                    // back stack, a simple replace() isn't enough so we
                    // remove it from the back stack and put our replacement
                    // on the back stack in its place
                    manager.popBackStack(
                        generateBackStackName(size, peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(generateBackStackName(size, destId))
                }
                false
            } else {
                ft.addToBackStack(generateBackStackName(size + 1, destId))
                true
            }
            if (navigatorExtras is Extras) {
                for ((key, value) in navigatorExtras.sharedElements) {
                    ft.addSharedElement(key!!, value!!)
                }
            }
            ft.setReorderingAllowed(true)
            ft.commit()
            // The commit succeeded, update our view of the world
            // The commit succeeded, update our view of the world
            return if (isAdded) {
                add(destId)
                destination
            } else {
                null
            }
        }

        return null
    }

    private fun generateBackStackName(backStackIndex: Int?, destId: Int?): String {
        return "$backStackIndex-$destId"
    }
}