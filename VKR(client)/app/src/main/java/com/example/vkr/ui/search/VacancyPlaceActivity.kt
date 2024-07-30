package com.example.vkr.ui.search

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R
import com.example.vkr.ui.profile.resume.UpdateResumeActivity
import com.example.vkr.ui.profile.settings.ChangeProfileActivity
import com.example.vkr.ui.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VacancyPlaceActivity : AppCompatActivity() {
    private lateinit var vacancyId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacancy_place)

        vacancyId = intent.getStringExtra("id").toString()

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                val vacancy = RetrofitClient.apiService.getVacancy(vacancyId)
                findViewById<TextView>(R.id.name_place_text_view).text = vacancy.body()?.name
                findViewById<TextView>(R.id.payment_place_text_view).text = vacancy.body()?.payment
                findViewById<TextView>(R.id.expertise_place_text_view).text = vacancy.body()?.expertise
                findViewById<TextView>(R.id.employment_place_text_view).text = vacancy.body()?.employment
                findViewById<TextView>(R.id.description_place_text_view).text = vacancy.body()?.description
            }
        }

        val replyButton: Button = findViewById(R.id.reply_button)
        replyButton.setOnClickListener {
            val intent = Intent(this, ReplyResumeActivity::class.java).apply {
                putExtra("id", vacancyId)
            }
            startActivity(intent)
        }
    }
}