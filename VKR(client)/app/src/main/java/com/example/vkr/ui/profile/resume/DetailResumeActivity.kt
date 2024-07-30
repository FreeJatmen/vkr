package com.example.vkr.ui.profile.resume

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R
import com.example.vkr.ui.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailResumeActivity : AppCompatActivity() {
    private lateinit var resumeId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_detail)

        resumeId = intent.getStringExtra("id").toString()

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        updateResume()

        val deleteButton: Button = findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            deleteResume(resumeId)
        }

        val updateButton: Button = findViewById(R.id.update_detail_resume_button)
        updateButton.setOnClickListener {
            val intent = Intent(this, UpdateResumeActivity::class.java).apply {
                putExtra("id", resumeId)
            }
            startActivity(intent)
        }
    }

    private fun updateResume() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val resume = RetrofitClient.apiService.getResume(token, resumeId)
            updateResumeData(
                resume.body()?.resumeName,
                resume.body()?.name,
                resume.body()?.surname,
                resume.body()?.contacts,
                resume.body()?.skills,
                resume.body()?.aboutYourself
            )
        }
    }

    private fun updateResumeData(resumeName: String?, name: String?, surname: String?,
                                 contacts: String?, skills: String?, aboutYourself: String?) {
        runOnUiThread {
            findViewById<TextView>(R.id.resume_name_detail_text_view).text = resumeName
            findViewById<TextView>(R.id.name_detail_text_view).text = name
            findViewById<TextView>(R.id.surname_detail_text_view).text = surname
            findViewById<TextView>(R.id.contacts_detail_text_view).text = contacts
            findViewById<TextView>(R.id.skills_detail_text_view).text = skills
            findViewById<TextView>(R.id.about_yourself_detail_text_view).text = aboutYourself
        }

    }

    override fun onResume() {
        super.onResume()
        updateResume()
    }

    private fun deleteResume(resumeId: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.deleteResume(resumeId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DetailResumeActivity, "Резюме удалено", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@DetailResumeActivity, "Ошибка при удалении резюме", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailResumeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}