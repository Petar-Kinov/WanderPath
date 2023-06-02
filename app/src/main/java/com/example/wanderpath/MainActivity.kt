package com.example.wanderpath

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wanderpath.databinding.ActivityMainBinding
import com.example.wanderpath.domain.auth.AuthViewModel
import com.example.wanderpath.ui.login.LoginViewModelFactory

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory())[AuthViewModel::class.java]
    }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: Main Activity created")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        binding.popupMenuBtn.setOnClickListener {
            binding.popupMenuBtn.isEnabled = false
            val popupMenu = PopupMenu(this, it)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.pop_up_menu, popupMenu.menu)

            popupMenu.menu.setGroupEnabled(0,false)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.homeBtn -> Toast.makeText(
                        this,
                        "You clicked ${item.title}",
                        Toast.LENGTH_LONG
                    ).show()
//                    R.id.settings -> {
//                        val action = HomePageDirections.actionHomePageToSettingsFragment()
//                        findNavController().navigate(action)
//                    }
                    R.id.logout -> logOut()
                }
                true
            }
            popupMenu.setOnDismissListener {
                popupMenu.menu.setGroupEnabled(0,false)
                binding.popupMenuBtn.isEnabled = true
            }
            popupMenu.show()

            // Enable the menu items after a short delay to avoid
            // accidental clicks during enter animations
            Handler(Looper.getMainLooper()).postDelayed({
                popupMenu.menu.setGroupEnabled(0, true)
            }, 500)
        }
    }

    private fun logOut() {
        authViewModel.logout()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        this.viewModelStore.clear()
        this.finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Main Activity destroyed")
    }
}