package com.kartikcd.mediumx

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kartikcd.mediumx.databinding.ActivityMainBinding
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.ui.auth.AuthViewModel
import com.kartikcd.mediumx.ui.auth.AuthViewModelFactory
import com.kartikcd.mediumx.util.Resource

class MainActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "prefs_mediumx"
        const val PREFS_TOKEN = "token"
    }

    private var _binding: ActivityMainBinding? = null
    private lateinit var navController: NavController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        navController = findNavController(R.id.fragment_navigation)

        setSupportActionBar(_binding?.mainToolbar)
        val factory = AuthViewModelFactory(application, MediumXRepository())
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        authViewModel.user.observe({ lifecycle }) {
            when(it) {
                is Resource.Success -> {
                    it.data.let { userResponse ->
                        sharedPreferences.edit {
                            putString(PREFS_TOKEN, userResponse?.user?.token)
                        }
                    }
                }
                is Resource.Error -> {
                    sharedPreferences.edit {
                        remove(PREFS_TOKEN)
                    }
                }
                is Resource.Loading -> {

                }
            }
        }

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