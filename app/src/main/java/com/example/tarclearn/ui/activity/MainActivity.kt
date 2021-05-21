package com.example.tarclearn.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.tarclearn.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(R.layout.main_activity) {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration:AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sharedPref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean(getString(R.string.key_is_logged_in), false)
        if(!isLoggedIn){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!! as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        findViewById<NavigationView>(R.id.navigationView).setupWithNavController(navController)
    }
}