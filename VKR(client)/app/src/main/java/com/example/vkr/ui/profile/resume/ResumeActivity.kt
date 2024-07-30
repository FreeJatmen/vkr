package com.example.vkr.ui.profile.resume

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.vkr.R
import com.example.vkr.ui.profile.authVacancies.AuthDetailVacancyActivity
import com.example.vkr.ui.retrofit.Resume
import com.example.vkr.ui.retrofit.RetrofitClient
import com.example.vkr.ui.retrofit.Vacancies
import com.example.vkr.ui.utilities.OnResumeClickListener
import com.example.vkr.ui.utilities.OnVacancyClickListener
import com.example.vkr.ui.utilities.ResumeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResumeActivity : AppCompatActivity(), OnResumeClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResumeAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var addResumeButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume)

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        addResumeButton = findViewById(R.id.add_resume_button)

        addResumeButton.setOnClickListener {
            startActivity(Intent(this, AddResumeActivity::class.java))
        }

        recyclerView = findViewById(R.id.resume_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.resume_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            fetchResume(token)
        }

        fetchResume(token)

    }

    override fun onResume() {
        super.onResume()
        fetchResume(token)
    }

    private fun fetchResume(token: String?) {
        swipeRefreshLayout.isRefreshing = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val viewResume = RetrofitClient.apiService.viewResume(token.toString())
            println("Response body: $viewResume")
            val resumeList = viewResume.body()?: emptyList()

            withContext(Dispatchers.Main) {
                adapter = ResumeAdapter(resumeList, this@ResumeActivity)
                recyclerView.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }


    override fun onItemClick(resume: Resume) {
        val intent = Intent(this, DetailResumeActivity::class.java).apply {
            putExtra("id", resume.rowId)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}