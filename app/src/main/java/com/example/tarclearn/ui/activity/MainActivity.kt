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
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tarclearn.R
import com.example.tarclearn.databinding.DrawerHeaderBinding
import com.example.tarclearn.model.UserDetailDto
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialize all the lateinit variables
        setContentView(R.layout.main_activity)
        sharedPref = getSharedPreferences(getString(R.string.pref_user), Context.MODE_PRIVATE)
        val navView: NavigationView = findViewById(R.id.navigationView)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        //check if user session is valid
        val isLoggedIn = sharedPref.getBoolean(getString(R.string.key_is_logged_in), false)
        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //bind the navigation drawer with User session data
        val drawerHeaderBinding: DrawerHeaderBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.drawer_header,
                navView,
                false
            )
        navView.addHeaderView(drawerHeaderBinding.root)
        val uid = sharedPref.getString(getString(R.string.key_user_id), "Nothing")
        val username = sharedPref.getString(getString(R.string.key_username), "Nothing")
        val isLect = sharedPref.getBoolean(getString(R.string.key_is_lecturer), false)
        val currentUser = UserDetailDto(uid!!, "", username!!, isLect)
        drawerHeaderBinding.user = currentUser

        //setup nav host and navigation view menu items
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!! as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.course_list_fragment, R.id.about_fragment, R.id.manage_user_fragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            with(sharedPref.edit()) {
                clear()
                putBoolean(getString(R.string.key_is_logged_in), false)
                commit()
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            true
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

}