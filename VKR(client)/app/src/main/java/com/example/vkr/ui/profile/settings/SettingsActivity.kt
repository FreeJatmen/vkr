package com.example.vkr.ui.profile.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        val changePasswordButton: Button = findViewById(R.id.change_profile_button)
        changePasswordButton.setOnClickListener {
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }

        val logoutButton: Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            sharedPreferences.edit().remove("auth_token").apply()
            finish()
        }

    }
}