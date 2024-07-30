package com.example.vkr.ui.profile.authorization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.MainActivity
import com.example.vkr.R
import com.example.vkr.ui.retrofit.MyUser
import com.example.vkr.ui.retrofit.RetrofitClient.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthActivity: AppCompatActivity() {

    private lateinit var login: EditText
    private lateinit var password: EditText
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        login = findViewById(R.id.login_edit_text)
        password = findViewById(R.id.password_edit_text)

        val buttonLogin = findViewById<Button>(R.id.login_button)
        buttonLogin.setOnClickListener {
            if (validateInput()) {
                val user = MyUser(login.text.toString(), password.text.toString())
                job = CoroutineScope(Dispatchers.IO).launch {
                    val response = apiService.loginUser(user)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val tokenJson = response.body()
                            val authToken = tokenJson?.token
                            saveToken(authToken.toString())
                            println("Received token: $authToken")
                            val intent = Intent(this@AuthActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@AuthActivity, "Ошибка: неверный логин или пароль", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val buttonRegister = findViewById<Button>(R.id.registration_button)
        buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (login.text.toString().isEmpty()) {
            login.error = "Пожалуйста, введите логин"
            isValid = false
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Пожалуйста, введите пароль"
            isValid = false
        }
        return isValid
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        login.text.clear()
        password.text.clear()
        job?.cancel()
    }

}
