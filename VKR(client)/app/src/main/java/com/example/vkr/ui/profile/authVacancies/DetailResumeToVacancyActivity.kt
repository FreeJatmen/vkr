package com.example.vkr.ui.profile.authVacancies

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R
import com.example.vkr.ui.profile.resume.UpdateResumeActivity
import com.example.vkr.ui.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailResumeToVacancyActivity : AppCompatActivity() {
    private lateinit var resumeId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_to_vacancy)

        resumeId = intent.getStringExtra("id").toString()

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        job = CoroutineScope(Dispatchers.IO).launch {
            val resume = RetrofitClient.apiService.getResume(token, resumeId)
            withContext(Dispatchers.Main){
                findViewById<TextView>(R.id.resume_name_vacancy_detail_text_view).text = resume.body()?.resumeName
                findViewById<TextView>(R.id.name_vacancy_detail_text_view).text = resume.body()?.name
                findViewById<TextView>(R.id.surname_vacancy_detail_text_view).text = resume.body()?.surname
                findViewById<TextView>(R.id.contacts_vacancy_detail_text_view).text = resume.body()?.contacts
                findViewById<TextView>(R.id.skills_vacancy_detail_text_view).text = resume.body()?.skills
                findViewById<TextView>(R.id.about_yourself_vacancy_detail_text_view).text = resume.body()?.aboutYourself
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}