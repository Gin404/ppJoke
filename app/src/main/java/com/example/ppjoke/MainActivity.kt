package com.example.ppjoke

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.alibaba.fastjson.JSONObject
import com.didi.libnetwork.ApiResponse
import com.didi.libnetwork.GetRequest
import com.didi.libnetwork.JsonCallback
import com.example.ppjoke.databinding.ActivityMainBinding
import com.example.ppjoke.utils.MyNavGraphBuilder
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

        MyNavGraphBuilder.build(navController, this, binding.container.id)

        navView.setOnItemSelectedListener(this)

        /*val request = GetRequest<JSONObject>("http://www.mooc.com")
        request.execute(object : JsonCallback<JSONObject>() {
            override fun onSuccess(response: ApiResponse<JSONObject>?) {
                super.onSuccess(response)
            }
        })*/

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navController.navigate(item.itemId)
        return !TextUtils.isEmpty(item.title)
    }
}