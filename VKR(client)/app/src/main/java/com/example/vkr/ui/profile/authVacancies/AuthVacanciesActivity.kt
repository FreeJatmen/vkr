package com.example.vkr.ui.profile.authVacancies

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
import com.example.vkr.ui.profile.addVacancy.AddVacancyActivity
import com.example.vkr.ui.retrofit.RetrofitClient
import com.example.vkr.ui.retrofit.Vacancies
import com.example.vkr.ui.utilities.VacancyAdapter
import com.example.vkr.ui.utilities.OnVacancyClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthVacanciesActivity : AppCompatActivity(), OnVacancyClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VacancyAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var postAdButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_vacancies)

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        postAdButton = findViewById(R.id.add_vacancy_button)

        postAdButton.setOnClickListener {
            startActivity(Intent(this, AddVacancyActivity::class.java))
        }

        recyclerView = findViewById(R.id.auth_vacancies_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.auth_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            fetchVacancies(token)
        }

        fetchVacancies(token)

    }

    override fun onResume() {
        super.onResume()
        fetchVacancies(token)
    }

    private fun fetchVacancies(token: String?) {
        swipeRefreshLayout.isRefreshing = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val viewLoginVacancies = RetrofitClient.apiService.viewLoggedVacancies(token.toString())
            println("Response body: $viewLoginVacancies")
            val vacanciesList = viewLoginVacancies.body()?: emptyList()

            withContext(Dispatchers.Main) {
                adapter = VacancyAdapter(vacanciesList, this@AuthVacanciesActivity)
                recyclerView.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }


    override fun onItemClick(vacancy: Vacancies) {
        val intent = Intent(this, AuthDetailVacancyActivity::class.java).apply {
            putExtra("id", vacancy.rowId)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}