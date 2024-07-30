package com.example.vkr.ui.search

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
import com.example.vkr.ui.profile.resume.AddResumeActivity
import com.example.vkr.ui.profile.resume.DetailResumeActivity
import com.example.vkr.ui.profile.resume.UpdateResumeActivity
import com.example.vkr.ui.retrofit.Resume
import com.example.vkr.ui.retrofit.ResumeRoute
import com.example.vkr.ui.retrofit.RetrofitClient
import com.example.vkr.ui.utilities.OnResumeClickListener
import com.example.vkr.ui.utilities.ResumeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReplyResumeActivity : AppCompatActivity(), OnResumeClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResumeAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private lateinit var vacancyId: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_reply)

        vacancyId = intent.getStringExtra("id").toString()

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        recyclerView = findViewById(R.id.resume_reply_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.resume_reply_swipe_refresh)
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
                adapter = ResumeAdapter(resumeList, this@ReplyResumeActivity)
                recyclerView.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }


    override fun onItemClick(resume: Resume) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.apiService.replyToVacancy(token, ResumeRoute("","",
                "", resume.rowId, vacancyId))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ReplyResumeActivity, "Резюме отправлено", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ReplyResumeActivity, "Ошибка при отправке резюме", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}