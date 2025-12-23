package com.example.appcomidaa._ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcomidaa.R
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import android.app.AlertDialog
import com.example.appcomidaa.repository.TaskRepositoryImpl


class LoginActivity : AppCompatActivity() {
    private lateinit var userNameEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var buttonIng: Button
    private val repository = TaskRepositoryImpl()

    companion object {
        const val USERNAME_KEY = "USERNAME_LOGIN"
        const val TOKEN_KEY = "TOKEN_LOGIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        initListeners()
    }

    private fun initUI() {
        userNameEt = findViewById(R.id.etUser)
        passwordEt = findViewById(R.id.etPassword)
        buttonIng = findViewById(R.id.buttonIngr)
    }

    private fun initListeners() {
        buttonIng.setOnClickListener {
            val username = userNameEt.text.toString()
            val password = passwordEt.text.toString()

            // Mostrar diálogo
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
            val loadingDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            loadingDialog.show()

            lifecycleScope.launch {
                try {
                    val response = repository.login(username, password)

                    loadingDialog.dismiss()

                    if (response != null && response.token.isNotEmpty()) {
                        // Login correcto
                        val intent = Intent(this@LoginActivity, menuPrincipal::class.java)
                        intent.putExtra(USERNAME_KEY, username)
                        intent.putExtra(TOKEN_KEY, response.token)
                        startActivity(intent)
                        finish()
                    } else {
                        // Error: mostrar el mensaje que vino del backend
                        Toast.makeText(
                            this@LoginActivity,
                            response?.message ?: "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}