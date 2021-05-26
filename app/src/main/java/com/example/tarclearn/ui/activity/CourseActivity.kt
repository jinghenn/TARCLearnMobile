package com.example.tarclearn.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tarclearn.R
import com.example.tarclearn.databinding.ActivityCourseBinding

class CourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.videoFragment,
                R.id.materialFragment,
                R.id.discussionFragment,
                R.id.quizFragment
//                R.id.courseInfoFragment
            )
        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.course_nav_host_fragment)!! as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}