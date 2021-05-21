package com.example.tarclearn.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.tarclearn.R
import com.example.tarclearn.databinding.DrawerHeaderBinding
import com.example.tarclearn.databinding.MainActivityBinding
import com.example.tarclearn.model.User
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialize all the lateinit variables
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        sharedPref = getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)

        //check if user session is valid
        val isLoggedIn = sharedPref.getBoolean(getString(R.string.key_is_logged_in), false)
        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //bind the navigation drawer with User session data
        val drawerHeaderBinding:DrawerHeaderBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.drawer_header, binding.navigationView, false)
        binding.navigationView.addHeaderView(drawerHeaderBinding.root)
        val uid = sharedPref.getString(getString(R.string.key_user_id), "Nothing")
        val username = sharedPref.getString(getString(R.string.key_username), "Nothing")
        val isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)
        val currentUser = User(uid!!, "", username!!, isLect)
        drawerHeaderBinding.user = currentUser

        //setup nav host
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!! as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.navigationView.setupWithNavController(navController)

    }
}