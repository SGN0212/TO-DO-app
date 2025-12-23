package com.example.appcomidaa._ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appcomidaa.R
import com.example.appcomidaa._ui.activities.LoginActivity.Companion.TOKEN_KEY
import com.example.appcomidaa._ui.activities.LoginActivity.Companion.USERNAME_KEY
import com.example.appcomidaa._ui.fragments.FavoriteFragment
import com.example.appcomidaa._ui.fragments.HomeFragment
import com.example.appcomidaa._ui.fragments.ProfileFragment
import com.example.appcomidaa.repository.SettingsRepository
import com.example.appcomidaa.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class menuPrincipal : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var userNameTV: TextView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var homeVM: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        homeVM = ViewModelProvider(this)[HomeViewModel::class.java]

        // 1. Inicializa el repositorio (o inyéctalo si usaras Hilt)
        val settingsRepository = SettingsRepository(this)
        // 2. Observa el cambio de modo oscuro
        lifecycleScope.launch {
            // repeatOnLifecycle asegura que dejemos de escuchar si la app se va a background
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsRepository.darkMode.collect { isEnabled ->
                    val mode = if (isEnabled) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                    // 3. Aplica el cambio. Esto es estático y afecta a toda la app.
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }

        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI(savedInstanceState)
        initListeners()

    }

    private fun initUI(savedInstanceState: Bundle?) {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }
        userName = intent.extras?.getString(USERNAME_KEY) ?: "NoUser"
        userNameTV = findViewById(R.id.userTV)
        userNameTV.text = userName
        homeVM.setToken(intent.extras?.getString(TOKEN_KEY) ?: "NoToken")
    }

    private fun initListeners() {
        bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_favorite -> FavoriteFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> null
            }

            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
            }
            true
        }
    }
}