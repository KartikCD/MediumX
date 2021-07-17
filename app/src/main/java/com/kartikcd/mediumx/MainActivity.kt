package com.kartikcd.mediumx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kartikcd.mediumx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        navController = findNavController(R.id.fragment_navigation)

        setSupportActionBar(_binding?.mainToolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auth, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_feed -> {
                println("Debug: main feed")
                navController.navigate(R.id.gotoFeedFragment)
            }
            R.id.menu_login_signup -> {
                println("Debug: login feed")
                navController.navigate(R.id.gotoAuthFragment)
            }
            R.id.menu_about -> {
                println("Debug: about feed")
            }
        }
        return true
    }
}