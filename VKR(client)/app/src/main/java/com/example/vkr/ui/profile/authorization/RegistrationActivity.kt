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

class RegistrationActivity : AppCompatActivity() {

    private lateinit var login: EditText
    private lateinit var password: EditText
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        login = findViewById(R.id.login_registration_edit_text)
        password = findViewById(R.id.password_registration_edit_text)

        val buttonRegisterSubmit = findViewById<Button>(R.id.registration_submit_button)
        buttonRegisterSubmit.setOnClickListener {
            if (validateInput()) {
                val user = MyUser(login.text.toString(), password.text.toString())
                job = CoroutineScope(Dispatchers.IO).launch {
                    val response = apiService.registerUser(user)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val tokenJson = response.body() // Получаем JSON-строку с токеном
                            val authToken = tokenJson?.token
                            saveToken(authToken.toString()) // Сохраняем токен
                            println("Received token: $authToken") // Выводим токен в консоль
                            val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@RegistrationActivity, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
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
