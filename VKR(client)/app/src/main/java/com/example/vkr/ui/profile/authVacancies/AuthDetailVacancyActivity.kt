package com.example.vkr.ui.profile.authVacancies

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.vkr.R
import com.example.vkr.ui.profile.resume.DetailResumeActivity
import com.example.vkr.ui.retrofit.Resume
import com.example.vkr.ui.retrofit.RetrofitClient
import com.example.vkr.ui.retrofit.RetrofitClient.apiService
import com.example.vkr.ui.utilities.OnResumeClickListener
import com.example.vkr.ui.utilities.ResumeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthDetailVacancyActivity : AppCompatActivity(), OnResumeClickListener {
    private lateinit var vacancyId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: ResumeAdapter
    private lateinit var token: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacancy_detail)

        vacancyId = intent.getStringExtra("id").toString()

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        recyclerView = findViewById(R.id.resume_vacancy_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.vacancy_detail_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            fetchResume(vacancyId)
        }

        updateVacancy()

        val deleteButton: Button = findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            deleteVacancy(vacancyId)
        }

        val updateButton: Button = findViewById(R.id.update_vacancy_button)
        updateButton.setOnClickListener {
            val intent = Intent(this, AuthUpdateVacancyActivity::class.java).apply {
                putExtra("id", vacancyId)
            }
            startActivity(intent)
        }

        fetchResume(vacancyId)
    }

    private fun fetchResume(vacancyId: String) {
        swipeRefreshLayout.isRefreshing = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val viewResume = apiService.viewResumeToVacancy(token, vacancyId)
            println("Response body: $viewResume")
            val resumeList = viewResume.body()?: emptyList()
            withContext(Dispatchers.Main) {
                adapter = ResumeAdapter(resumeList, this@AuthDetailVacancyActivity)
                recyclerView.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }


    override fun onItemClick(resume: Resume) {
        val intent = Intent(this, DetailResumeToVacancyActivity::class.java).apply {
            putExtra("id", resume.rowId)
        }
        startActivity(intent)
    }

    private fun updateVacancy() {
        job = CoroutineScope(Dispatchers.IO).launch {
            val vacancy = apiService.getAuthVacancy(token, vacancyId)
            updateVacancyData(
                vacancy.body()?.name,
                vacancy.body()?.payment,
                vacancy.body()?.expertise,
                vacancy.body()?.employment,
                vacancy.body()?.description
            )
        }
    }

    private fun updateVacancyData(name: String?, payment: String?, expertise: String?, employment: String?, description: String?) {
        runOnUiThread {
            findViewById<TextView>(R.id.name_detail_text_view).text = name
            findViewById<TextView>(R.id.payment_detail_text_view).text = payment
            findViewById<TextView>(R.id.expertise_detail_text_view).text = expertise
            findViewById<TextView>(R.id.employment_detail_text_view).text = employment
            findViewById<TextView>(R.id.description_detail_text_view).text = description
        }

    }

    override fun onResume() {
        super.onResume()
        updateVacancy()
    }

    private fun deleteVacancy(vacancyId: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.deleteVacancy(vacancyId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AuthDetailVacancyActivity, "Vacancy deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AuthDetailVacancyActivity, "Failed to delete vacancy", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AuthDetailVacancyActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
