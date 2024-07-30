package com.example.vkr.ui.profile.settings

import android.content.Context
import android.widget.EditText
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R
import com.example.vkr.ui.retrofit.MyUser
import com.example.vkr.ui.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeProfileActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private lateinit var login: EditText
    private lateinit var oldPassword: EditText
    private lateinit var newFirstPassword: EditText
    private lateinit var newSecondPassword: EditText
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        login = findViewById(R.id.old_login)
        oldPassword = findViewById(R.id.old_password)
        newFirstPassword = findViewById(R.id.new_first_password)
        newSecondPassword = findViewById(R.id.new_second_password)

        val changeProfileButton: Button = findViewById(R.id.change_profile_button)
        changeProfileButton.setOnClickListener {
            if (validateInput()) {
                val user = MyUser(login.text.toString(), oldPassword.text.toString())
                if(newFirstPassword.text.toString() == newSecondPassword.text.toString()){
                    job = CoroutineScope(Dispatchers.IO).launch {
                        val response = RetrofitClient.apiService.changePassword(token, newFirstPassword.text.toString(), user)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@ChangeProfileActivity, "Пароль изменен", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Toast.makeText(this@ChangeProfileActivity, "Ошибка: $errorBody", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@ChangeProfileActivity,"Пароли не совпадают", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val cancelButton: Button = findViewById(R.id.cansel_change_profile_button)
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (login.text.toString().isEmpty()) {
            login.error = "Пожалуйста, введите логин"
            isValid = false
        }
        if (oldPassword.text.toString().isEmpty()) {
            oldPassword.error = "Пожалуйста, введите пароль"
            isValid = false
        }
        if (newFirstPassword.text.toString().isEmpty()) {
            newFirstPassword.error = "Пожалуйста, введите пароль"
            isValid = false
        }
        if (newSecondPassword.text.toString().isEmpty()) {
            newSecondPassword.error = "Пожалуйста, введите пароль"
            isValid = false
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}