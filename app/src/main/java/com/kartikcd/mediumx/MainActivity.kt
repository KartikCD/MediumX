package com.kartikcd.mediumx

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kartikcd.api.models.entities.User
import com.kartikcd.mediumx.databinding.ActivityMainBinding
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.ui.auth.AuthViewModel
import com.kartikcd.mediumx.ui.auth.AuthViewModelFactory
import com.kartikcd.mediumx.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "prefs_mediumx"
        const val PREFS_TOKEN = "token"
        const val MENU_FEED = 0
        const val MENU_LOGIN_SIGNUP = 1
        const val MENU_ABOUTUS = 2
        const val MENU_PROFILE = 3
        const val MENU_LOGOUT = 4
        const val MENU_CREATE_ARTILE = 5
    }

    private var _binding: ActivityMainBinding? = null
    private lateinit var navController: NavController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPreferences: SharedPreferences
    var _user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        navController = findNavController(R.id.fragment_navigation)

        setSupportActionBar(_binding?.mainToolbar)
        val factory = AuthViewModelFactory(application, MediumXRepository())
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        sharedPreferences.getString(PREFS_TOKEN, null)?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val response = MediumXRepository().getCurrentUser(it)
                if (response.code() == 200) {
                    updateMenu(response.body()?.user)
                } else {
                    updateMenu(null)
                }
            }
        }

        authViewModel.user.observe({ lifecycle }) {
            when(it) {
                is Resource.Success -> {
                    it.data.let { userResponse ->
                        updateMenu(userResponse?.user)
                        sharedPreferences.edit {
                            putString(PREFS_TOKEN, userResponse?.user?.token)
                        }
                        navController.navigate(R.id.gotoFeedFragment)
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

        authViewModel.logoutBool.observe({ lifecycle }) {
            sharedPreferences.edit {
                remove(PREFS_TOKEN)
            }
            updateMenu(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        when(_user) {
            is User -> {
                menu?.add(0, MENU_FEED, Menu.NONE, "Feed")
                menu?.add(0, MENU_PROFILE, Menu.NONE, "Profile")
                menu?.add(0, MENU_CREATE_ARTILE, Menu.NONE, "Create Article")
                menu?.add(0, MENU_ABOUTUS, Menu.NONE, "About Us")
                menu?.add(0, MENU_LOGOUT, Menu.NONE, "Logout")
            }
            else -> {
                menu?.add(0, MENU_FEED, Menu.NONE, "Feed")
                menu?.add(0, MENU_LOGIN_SIGNUP, Menu.NONE, "Login / Signup")
                menu?.add(0, MENU_ABOUTUS, Menu.NONE, "About Us")
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    fun updateMenu(user: User?) {
        _user = user
        invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            MENU_FEED -> {
                println("Debug: main feed")
                navController.navigate(R.id.gotoFeedFragment)
            }
            MENU_LOGIN_SIGNUP -> {
                println("Debug: login feed")
                navController.navigate(R.id.gotoAuthFragment)
            }
            MENU_ABOUTUS -> {
                println("Debug: about feed")
                navController.navigate(R.id.gotoAboutUs)
            }
            MENU_PROFILE -> {
                println("Debug: profile feed")
                navController.navigate(R.id.gotoProfileFragment2)
            }
            MENU_LOGOUT -> {
                println("Debug: logout")
                authViewModel.logout()
            }
            MENU_CREATE_ARTILE -> {
                navController.navigate(R.id.gotoCreateArticle)
            }
        }
        return true
    }

    override fun onBackPressed() {
    }
}