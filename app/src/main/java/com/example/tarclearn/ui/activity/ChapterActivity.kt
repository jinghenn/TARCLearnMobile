package com.example.tarclearn.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tarclearn.R
import com.example.tarclearn.databinding.ActivityChapterBinding

class ChapterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChapterBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chapter)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.chapter_nav_host_fragment)!! as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.videoListFragment,
                R.id.materialListFragment,
                R.id.discussionListFragment,
                R.id.quizListFragment
            )
        )

        binding.bottomNav.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfig)
        val title = "Chapter ${intent.getStringExtra("chapterNo")}: ${intent.getStringExtra("chapterTitle")}"
        supportActionBar?.title = title

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}